package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.viewmodel.PromotionBudgetAndTypeViewModel
import kotlinx.android.synthetic.main.mvc_banner_voucher_fragment.*
import javax.inject.Inject

class PromotionBudgetAndTypeFragment(private val onNextStep: (VoucherImageType) -> Unit = {},
                                     private val getVoucherUiModel: () -> BannerVoucherUiModel,
                                     private val setVoucherBitmap: (Bitmap) -> Unit)
    : BaseCreateMerchantVoucherFragment<PromotionTypeBudgetTypeFactory, PromotionTypeBudgetAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNext: (VoucherImageType) -> Unit,
                           getVoucherUiModel: () -> BannerVoucherUiModel,
                           setVoucherBitmap: (Bitmap) -> Unit) = PromotionBudgetAndTypeFragment(onNext, getVoucherUiModel, setVoucherBitmap)

        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"
        private const val FREE_DELIVERY = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_gratis_ongkir.png"
        private const val CASHBACK = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback.png"
        private const val CASHBACK_UNTIL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/label/label_cashback_hingga.png"

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override var layoutRes: Int = R.layout.mvc_banner_voucher_fragment

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(PromotionBudgetAndTypeViewModel::class.java)
    }

    private val promotionTypeInputWidget by lazy {
        PromotionTypeInputUiModel()
    }

    private val generalExpensesInfoBottomSheetFragment by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context).apply {
            setTitle(context?.resources?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    private var bannerVoucherUiModel: BannerVoucherUiModel = getVoucherUiModel()

    private var painter: VoucherPreviewPainter? = null

    private var voucherPreviewBitmap: Bitmap? = null

    private var isWaitingForShopInfo = false
    private var isReadyToDraw = false

    private var tempVoucherType: VoucherImageType = VoucherImageType.FreeDelivery(0)

    override var extraWidget: List<Visitable<PromotionTypeBudgetTypeFactory>> =
            listOf(promotionTypeInputWidget)

    override fun onResume() {
        bannerVoucherUiModel = getVoucherUiModel()
        super.onResume()
    }

    override fun onDismissBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {}

    override fun onBeforeShowBottomSheet(bottomSheetType: CreateVoucherBottomSheetType) {}

    override fun onFinishRenderInitial() {}

    override fun getAdapterTypeFactory(): PromotionTypeBudgetAdapterTypeFactory =
            PromotionTypeBudgetAdapterTypeFactory(this, onNextStep, ::onShouldChangeBannerValue)

    override fun onItemClicked(t: Visitable<CreateVoucherTypeFactory>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun setupView() {
        super.setupView()
        setupBottomSheet()
        observeLiveData()
        initiateVoucherPreview()
    }

    private fun setupBottomSheet() {
        context?.run {
            addBottomSheetView(CreateVoucherBottomSheetType.GENERAL_EXPENSE_INFO, generalExpensesInfoBottomSheetFragment)
        }
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.basicShopInfoLiveData) { result ->
                if (isWaitingForShopInfo) {
                    when(result) {
                        is Success -> {
                            bannerVoucherUiModel.shopName = result.data.shopName
                            bannerVoucherUiModel.shopAvatar = result.data.shopAvatar
                            drawInitialVoucherPreview()
                        }
                        is Fail -> {
                            val error = result.throwable.message.toBlankOrString()
                            view?.showErrorToaster(error)
                        }
                    }
                    isWaitingForShopInfo = false
                }
            }
            observe(viewModel.bannerBitmapLiveData) { bitmap ->
                bannerImage?.setImageBitmap(bitmap)
                setVoucherBitmap(bitmap)
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
                    .load(BANNER_BASE_URL)
                    .signature(ObjectKey(System.currentTimeMillis().toString()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val bitmap = resource.toBitmap()
                            voucherPreviewBitmap = bitmap
                            if (painter == null) {
                                painter = VoucherPreviewPainter(context, bitmap, ::onSuccessGetBanner)
                            }
                            bannerVoucherUiModel.let {
                                painter?.drawInitial(it)
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
                is VoucherImageType.FreeDelivery -> FREE_DELIVERY
                else -> CASHBACK
            }
            activity?.runOnUiThread {
                if (voucherImageType is VoucherImageType.Percentage) {
                    bannerInfo?.setPreviewInfo(voucherImageType, labelUrl, CASHBACK_UNTIL)
                } else {
                    bannerInfo?.setPreviewInfo(voucherImageType, labelUrl)
                }
            }
        } else {
            tempVoucherType = voucherImageType
        }
    }

    private fun onSuccessGetBanner(bitmap: Bitmap) {
        bannerImage?.setImageBitmap(bitmap)
        isReadyToDraw = true
        onShouldChangeBannerValue(tempVoucherType)
    }
}
