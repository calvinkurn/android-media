package com.tokopedia.vouchercreation.create.view.fragment.step

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.domain.usecase.CreateVoucherUseCase
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.dialog.FailedCreateVoucherDialog
import com.tokopedia.vouchercreation.create.view.dialog.LoadingDialog
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TermsAndConditionBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.ReviewVoucherViewModel
import com.tokopedia.vouchercreation.detail.model.DividerUiModel
import com.tokopedia.vouchercreation.detail.model.FooterButtonUiModel
import com.tokopedia.vouchercreation.detail.model.FooterUiModel
import com.tokopedia.vouchercreation.detail.model.TipsUiModel
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactoryImpl
import com.tokopedia.vouchercreation.detail.view.fragment.BaseDetailFragment
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import kotlinx.android.synthetic.main.fragment_base_list.*
import javax.inject.Inject

class ReviewVoucherFragment : BaseDetailFragment() {

    companion object {
        @JvmStatic
        fun createInstance(getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                           getToken: () -> String,
                           getPostBaseUiModel: () -> PostBaseUiModel,
                           onReturnToStep: (Int) -> Unit,
                           getBannerBitmap: () -> Bitmap?,
                           getVoucherId: () -> Int?,
                           getPromoCodePrefix: () -> String): ReviewVoucherFragment = ReviewVoucherFragment().apply {
            this.getVoucherReviewUiModel = getVoucherReviewUiModel
            this.getToken = getToken
            this.getPostBaseUiModel = getPostBaseUiModel
            this.onReturnToStep = onReturnToStep
            this.getBannerBitmap = getBannerBitmap
            this.getVoucherId = getVoucherId
            this.getPromoCodePrefix = getPromoCodePrefix
        }

        private const val VOUCHER_TIPS_INDEX = 1

        private const val NO_PROMO_CODE_DISPLAY = "-"
    }

    private var getVoucherReviewUiModel: () -> VoucherReviewUiModel = { VoucherReviewUiModel() }
    private var getToken: () -> String = { "" }
    private var getPostBaseUiModel: () -> PostBaseUiModel = {
        PostBaseUiModel(
                CreateMerchantVoucherStepsActivity.POST_IMAGE_URL,
                CreateMerchantVoucherStepsActivity.FREE_DELIVERY_URL,
                CreateMerchantVoucherStepsActivity.CASHBACK_URL,
                CreateMerchantVoucherStepsActivity.CASHBACK_UNTIL_URL
        )}
    private var onReturnToStep: (Int) -> Unit = { _ -> }
    private var getBannerBitmap: () -> Bitmap? = { null }
    private var getVoucherId: () -> Int? = { null }
    private var getPromoCodePrefix: () -> String = { "" }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ReviewVoucherViewModel::class.java)
    }

    private val termsAndConditionBottomSheet by lazy {
        context?.run {
            TermsAndConditionBottomSheetFragment.createInstance(this).apply {
                setCloseClickListener {
                    this.dismiss()
                }
            }
        }
    }

    private val generalExpenseBottomSheet by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context)
    }

    private val publicVoucherTipsAndTrickBottomSheet by lazy {
        VoucherDisplayBottomSheetFragment.createInstance(context, ::getPublicVoucherDisplay)
    }

    private val failedCreateVoucherDialog by lazy {
        context?.run {
            FailedCreateVoucherDialog(this, ::onDialogTryAgain, ::onDialogRequestHelp)
        }
    }

    private val loadingDialog by lazy {
        context?.run {
            LoadingDialog(this)
        }
    }

    private val buttonUiModel by lazy {
        FooterButtonUiModel(context?.getString(R.string.mvc_add_voucher).toBlankOrString(), "", true)
    }

    private var isWaitingForResult = false

    private var squareVoucherBitmap: Bitmap? = null
        set(value){
            if (field == null) {
                activity?.intent?.getBooleanExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, false)?.let { isDuplicate ->
                    if (isDuplicate) {
                        recycler_view?.scrollToPosition(adapter.dataSize - 1)
                    }
                }
            }
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    override fun onResume() {
        super.onResume()
        renderReviewInformation(getVoucherReviewUiModel())
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun showDownloadBottomSheet() {}

    override fun getAdapterTypeFactory(): VoucherDetailAdapterFactoryImpl {
        return VoucherDetailAdapterFactoryImpl(this, activity)
    }

    override fun showTipsAndTrickBottomSheet() {
        publicVoucherTipsAndTrickBottomSheet.run {
            setTitle(this@ReviewVoucherFragment.resources.getString(R.string.mvc_create_public_voucher_display_title).toBlankOrString())
            show(this@ReviewVoucherFragment.childFragmentManager, VoucherDisplayBottomSheetFragment.TAG)
        }
    }

    override fun onInfoContainerCtaClick(dataKey: String) {
        val step = when(dataKey) {
            VOUCHER_INFO_DATA_KEY -> VoucherCreationStep.TARGET
            VOUCHER_BENEFIT_DATA_KEY -> VoucherCreationStep.BENEFIT
            PERIOD_DATA_KEY -> VoucherCreationStep.PERIOD
            DATA_KEY_VOUCHER_PERIOD -> VoucherCreationStep.PERIOD
            else -> VoucherCreationStep.REVIEW
        }
        onReturnToStep(step)
    }

    override fun onFooterCtaTextClickListener() {
        termsAndConditionBottomSheet?.show(childFragmentManager, TermsAndConditionBottomSheetFragment.TAG)
    }

    override fun onTickerClicked() {
        generalExpenseBottomSheet.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    override fun onFooterButtonClickListener() {
        if (getVoucherReviewUiModel().startDate.isBlank()) {
            view?.run {
                Toaster.make(this,
                        context?.getString(R.string.mvc_period_alert).toBlankOrString(),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        context?.getString(R.string.mvc_oke).toBlankOrString(),
                        View.OnClickListener { })
            }
            return
        }
        isWaitingForResult = true
        activity?.intent?.getBooleanExtra(CreateMerchantVoucherStepsActivity.IS_EDIT, false)?.let { isEdit ->
            if (isEdit) {
                updateVoucher()
            } else {
                createVoucher()
            }
            return
        }
        createVoucher()
    }

    override fun onSuccessDrawPostVoucher(postVoucherBitmap: Bitmap) {
        squareVoucherBitmap = postVoucherBitmap
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.createVoucherResponseLiveData) { result ->
                if (isWaitingForResult) {
                    when(result) {
                        is Success -> {
                            if (result.data.status != CreateVoucherUseCase.STATUS_SUCCESS) {
                                failedCreateVoucherDialog?.show()
                            } else {
                                context?.run {
                                    val intent = RouteManager.getIntent(this, ApplinkConstInternalSellerapp.VOUCHER_LIST).apply {
                                        putExtra(VoucherListActivity.SUCCESS_VOUCHER_ID_KEY, result.data.voucherId)
                                    }
                                    startActivity(intent)
                                    activity?.finish()
                                }
                            }
                        }
                        is Fail -> {
                            failedCreateVoucherDialog?.show()
                        }
                    }
                    with(adapter) {
                        notifyItemChanged(data.indexOf(buttonUiModel))
                    }
                    loadingDialog?.dismiss()
                }
                isWaitingForResult = false
            }
            observe(viewModel.updateVoucherSuccessLiveData) { result ->
                if (isWaitingForResult) {
                    when(result) {
                        is Success -> {
                            RouteManager.route(context, ApplinkConstInternalSellerapp.VOUCHER_LIST)
                            activity?.finish()
                        }
                        is Fail -> {
                            failedCreateVoucherDialog?.show()
                        }
                    }
                }
                isWaitingForResult = false
            }
        }
    }

    private fun renderReviewInformation(voucherReviewUiModel: VoucherReviewUiModel) {
        voucherReviewUiModel.run {
            val postDisplayedDate = getDisplayedDateString(startDate, endDate)
            val fullDisplayedDate: String? = if (startDate.isEmpty()) {
                null
            } else {
                getDisplayedDateString(startDate, startHour, endDate, endHour)
            }

            val reviewInfoList = mutableListOf(
                    with(voucherReviewUiModel) {
                        getVoucherPreviewSection(voucherType, voucherName, shopAvatarUrl, shopName, getPromoCodePrefix() + promoCode, postDisplayedDate)
                    },
                    getVoucherInfoSection(targetType, voucherName, getPromoCodePrefix() + promoCode, true),
                    DividerUiModel(DividerUiModel.THIN),
                    getVoucherBenefitSection(voucherType, minPurchase, voucherQuota, true),
                    getExpenseEstimationSection(voucherType.value, voucherQuota),
                    DividerUiModel(DividerUiModel.THIN),
                    getPeriodSection(fullDisplayedDate, true),
                    DividerUiModel(DividerUiModel.THICK),
                    buttonUiModel,
                    FooterUiModel(
                            context?.getString(R.string.mvc_review_agreement).toBlankOrString(),
                            context?.getString(R.string.mvc_review_terms).toBlankOrString())
            )

            if (targetType == VoucherTargetType.PUBLIC) {
                context?.run {
                    val tipsUiModel = TipsUiModel(getString(R.string.mvc_detail_tips), getString(R.string.mvc_detail_tips_clickable))
                    val dividerUiModel = DividerUiModel(DividerUiModel.THIN)
                    reviewInfoList.addAll(
                            VOUCHER_TIPS_INDEX,
                            listOf(
                                    tipsUiModel,
                                    dividerUiModel
                            ))
                }
            }
            adapter.data.clear()
            renderList(reviewInfoList)
        }
    }

    private fun getVoucherPreviewSection(voucherType: VoucherImageType,
                                         promoName: String,
                                         shopAvatar: String,
                                         shopName: String,
                                         promoCode: String,
                                         promoPeriod: String) : PostVoucherUiModel {
        var promoCodeString = promoCode
        if (promoCode.isBlank()) {
            promoCodeString = NO_PROMO_CODE_DISPLAY
        }
        return PostVoucherUiModel(
                voucherType,
                promoName,
                shopAvatar,
                shopName,
                promoCodeString,
                promoPeriod,
                getPostBaseUiModel())
    }

    private fun getPublicVoucherDisplay() = VoucherTargetCardType.PUBLIC

    private fun createVoucher() {
        getBannerBitmap()?.let startCheck@ { bannerBitmap ->
            squareVoucherBitmap?.let { squareBitmap ->
                viewModel.createVoucher(
                        bannerBitmap,
                        squareBitmap,
                        CreateVoucherParam.mapToParam(
                                getVoucherReviewUiModel(), getToken()
                        ))
            }
        }
    }

    private fun updateVoucher() {
        getBannerBitmap()?.let startCheck@ { bannerBitmap ->
            squareVoucherBitmap?.let { squareBitmap ->
                getVoucherId()?.let { voucherId ->
                    viewModel.updateVoucher(
                            bannerBitmap,
                            squareBitmap,
                            getVoucherReviewUiModel(),
                            getToken(),
                            voucherId
                    )
                }
            }
        }
    }

    private fun onDialogTryAgain() {
        failedCreateVoucherDialog?.dismiss()
        loadingDialog?.show()
        isWaitingForResult = true
        getBannerBitmap()?.let { bannerBitmap ->
            squareVoucherBitmap?.let { squareBitmap ->
                viewModel.createVoucher(
                        bannerBitmap,
                        squareBitmap,
                        CreateVoucherParam.mapToParam(
                                getVoucherReviewUiModel(), getToken()
                        ))
            }
        }
    }

    private fun onDialogRequestHelp() {
        failedCreateVoucherDialog?.dismiss()
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, VoucherUrl.HELP_URL)
    }

}