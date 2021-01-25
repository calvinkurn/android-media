package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.vouchertype.CashbackVoucherCreateFragment
import com.tokopedia.vouchercreation.create.view.painter.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.PromotionBudgetAndTypeViewModel
import kotlinx.android.synthetic.main.mvc_banner_voucher_fragment.*
import javax.inject.Inject

class PromotionBudgetAndTypeFragment : BaseDaggerFragment() {

    companion object {
        private const val ERROR_MESSAGE = "Error validate voucher type"

        @JvmStatic
        fun createInstance(onNext: (VoucherImageType, Int, Int) -> Unit,
                           setRecommendationStatus: (Int) -> Unit,
                           getVoucherUiModel: () -> BannerVoucherUiModel,
                           getBannerBaseUiModel: () -> BannerBaseUiModel,
                           onSetShopInfo: (String, String) -> Unit,
                           getVoucherReviewData: () -> VoucherReviewUiModel,
                           isCreateNew: Boolean) = PromotionBudgetAndTypeFragment().apply {
            this.onNextStep = onNext
            this.setRecommendationStatus = setRecommendationStatus
            this.getVoucherUiModel = getVoucherUiModel
            this.getBannerBaseUiModel = getBannerBaseUiModel
            this.onSetShopInfo = onSetShopInfo
            this.getVoucherReviewData = getVoucherReviewData
            this.isCreateNew = isCreateNew
        }
    }

    private var onNextStep: (VoucherImageType, Int, Int) -> Unit = { _,_,_ ->  }
    private var setRecommendationStatus: (Int) -> Unit = { _ -> }
    private var getVoucherUiModel: () -> BannerVoucherUiModel = {
        BannerVoucherUiModel(
                VoucherImageType.FreeDelivery(0),
                "",
                "",
                "")
    }
    private var getBannerBaseUiModel: () -> BannerBaseUiModel = {
        BannerBaseUiModel(
                VoucherUrl.BANNER_BASE_URL,
                VoucherUrl.FREE_DELIVERY_URL,
                VoucherUrl.CASHBACK_URL,
                VoucherUrl.CASHBACK_UNTIL_URL
        )}
    private var onSetShopInfo: (String, String) -> Unit = { _,_ -> }
    private var getVoucherReviewData: () -> VoucherReviewUiModel? = { null }
    private var isCreateNew: Boolean = true

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(PromotionBudgetAndTypeViewModel::class.java)
    }

    private val promotionTypeInputWidget by lazy {
        PromotionTypeInputUiModel().apply {
            if (!isCreateNew) {
                getVoucherReviewData()?.run {
                    isChecked = when(voucherType) {
                        is VoucherImageType.FreeDelivery -> false
                        is VoucherImageType.Rupiah -> true
                        is VoucherImageType.Percentage -> true
                    }
                }
            }
        }
    }

    private val cashbackVoucherCreateFragment by lazy {
        context?.let { CashbackVoucherCreateFragment.createInstance(onNextStep, setRecommendationStatus, ::onShouldChangeBannerValue, it, getVoucherReviewData, isCreateNew) }
    }

    private val impressHolder = ImpressHolder()

    private var bannerVoucherUiModel: BannerVoucherUiModel = getVoucherUiModel()

    private var painter: VoucherPreviewPainter? = null

    private var voucherPreviewBitmap: Bitmap? = null

    private var isWaitingForShopInfo = false
    private var isReadyToDraw = false

    private var tempVoucherType: VoucherImageType = VoucherImageType.Rupiah(0)

    override fun onResume() {
        bannerVoucherUiModel = getVoucherUiModel()
        bannerInfo?.setPromoName(bannerVoucherUiModel.promoName)
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mvc_banner_voucher_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnImpressionListener(impressHolder) {
            VoucherCreationTracking.sendOpenScreenTracking(
                    VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.TYPE_BUDGET,
                    userSession.isLoggedIn,
                    userSession.userId)
        }
        observeLiveData()
        initiateVoucherPreview()
        val fragmentTransaction = childFragmentManager.beginTransaction().apply {
            cashbackVoucherCreateFragment?.let {
                if (childFragmentManager.findFragmentByTag(CashbackVoucherCreateFragment::javaClass.name) == null) {
                    add(R.id.cashbackFragmentContainer, it, CashbackVoucherCreateFragment::javaClass.name)
                } else {
                    replace(R.id.cashbackFragmentContainer, it, CashbackVoucherCreateFragment::javaClass.name)
                }
            }
        }
        fragmentTransaction.commit()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.basicShopInfoLiveData) { result ->
                if (isWaitingForShopInfo) {
                    when(result) {
                        is Success -> {
                            with(result.data) {
                                onSetShopInfo(shopName, shopAvatar)
                            }
                            bannerVoucherUiModel = getVoucherUiModel()
                            drawInitialVoucherPreview()
                        }
                        is Fail -> {
                            val error = result.throwable.message.toBlankOrString()
                            view?.showErrorToaster(error)
                            MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                        }
                    }
                    isWaitingForShopInfo = false
                }
            }
        }

    }

    private fun initiateVoucherPreview() {
        isWaitingForShopInfo = true
        viewModel.getBasicShopInfo()
        drawInitialVoucherPreview()
    }

    private fun drawInitialVoucherPreview() {
        bannerInfo?.setPromoName(bannerVoucherUiModel.promoName)
        bannerImage?.run {
            Glide.with(context)
                    .asDrawable()
                    .load(getBannerBaseUiModel().bannerBaseUrl)
                    .signature(ObjectKey(System.currentTimeMillis().toString()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmap = resource.toBitmap()
                            voucherPreviewBitmap = bitmap
                            if (painter == null) {
                                painter = VoucherPreviewPainter(context, bitmap, ::onSuccessGetBanner, getBannerBaseUiModel())
                            }
                            activity?.runOnUiThread {
                                bannerVoucherUiModel.let {
                                    painter?.drawInitial(it)
                                }
                            }
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun onShouldChangeBannerValue(voucherImageType: VoucherImageType) {
        if (isReadyToDraw) {
            val labelUrl = when(voucherImageType) {
                is VoucherImageType.FreeDelivery -> getBannerBaseUiModel().freeDeliveryLabelUrl
                else -> getBannerBaseUiModel().cashbackLabelUrl
            }
            Handler().post {
                if (voucherImageType is VoucherImageType.Percentage) {
                    bannerInfo?.setPreviewInfo(voucherImageType, labelUrl, getBannerBaseUiModel().cashbackUntilLabelUrl)
                } else {
                    bannerInfo?.setPreviewInfo(voucherImageType, labelUrl)
                }
            }
        } else {
            tempVoucherType = voucherImageType
        }
    }

    private fun onSuccessGetBanner(bitmap: Bitmap) {
        activity?.runOnUiThread {
            bannerImage?.setImageBitmap(bitmap)
            isReadyToDraw = true
            onShouldChangeBannerValue(tempVoucherType)
        }
    }
}
