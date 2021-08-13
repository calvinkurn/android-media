package com.tokopedia.vouchercreation.create.view.fragment.step

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getDisplayedDateString
import com.tokopedia.vouchercreation.common.utils.dismissBottomSheetWithTags
import com.tokopedia.vouchercreation.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.dialog.FailedCreateVoucherDialog
import com.tokopedia.vouchercreation.create.view.dialog.LoadingDialog
import com.tokopedia.vouchercreation.create.view.dialog.PromoCodeErrorDialog
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.TermsAndConditionBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.VoucherDisplayBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.painter.SquareVoucherPainter
import com.tokopedia.vouchercreation.create.view.painter.VoucherPreviewPainter
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.BannerBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.ReviewVoucherViewModel
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactoryImpl
import com.tokopedia.vouchercreation.detail.view.fragment.BaseDetailFragment
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import kotlinx.android.synthetic.main.fragment_mvc_base_list.*
import javax.inject.Inject

class ReviewVoucherFragment : BaseDetailFragment() {

    companion object {
        @JvmStatic
        fun createInstance(getVoucherReviewUiModel: () -> VoucherReviewUiModel,
                           getToken: () -> String,
                           getRecommendationStatus: () -> Int,
                           getPostBaseUiModel: () -> PostBaseUiModel,
                           onReturnToStep: (Int) -> Unit,
                           getBannerBitmap: () -> Bitmap?,
                           getVoucherId: () -> Int?,
                           getPromoCodePrefix: () -> String,
                           getBannerBaseUiModel: () -> BannerBaseUiModel,
                           getVoucherBanner: () -> BannerVoucherUiModel,
                           isEdit: Boolean): ReviewVoucherFragment = ReviewVoucherFragment().apply {
            this.getVoucherReviewUiModel = getVoucherReviewUiModel
            this.getToken = getToken
            this.getRecommendationStatus = getRecommendationStatus
            this.getPostBaseUiModel = getPostBaseUiModel
            this.onReturnToStep = onReturnToStep
            this.getBannerBitmap = getBannerBitmap
            this.getVoucherId = getVoucherId
            this.getPromoCodePrefix = getPromoCodePrefix
            this.getBannerBaseUiModel = getBannerBaseUiModel
            this.getVoucherBanner = getVoucherBanner
            this.isEdit = isEdit
        }

        private const val VOUCHER_TIPS_INDEX = 1

        private const val NO_PROMO_CODE_DISPLAY = "-"

        private const val PROMO_CODE_ERROR_RESPONSE = "Kode voucher sudah digunakan."

        private const val IS_MVC_FIRST_TIME = "is_mvc_first_time"
        private const val VOUCHER_CREATION = "voucher_creation"

        private const val CREATE_VOUCHER_ERROR = "Create voucher error"
        private const val UPDATE_VOUCHER_ERROR = "Update voucher error"
        private const val LOAD_IMAGE_ERROR = "Load image  error"
    }

    private var getVoucherReviewUiModel: () -> VoucherReviewUiModel = { VoucherReviewUiModel() }
    private var getToken: () -> String = { "" }
    private var getRecommendationStatus: () -> Int = { 0 }
    private var getPostBaseUiModel: () -> PostBaseUiModel = {
        PostBaseUiModel(
                VoucherUrl.POST_IMAGE_URL,
                VoucherUrl.FREE_DELIVERY_URL,
                VoucherUrl.CASHBACK_URL,
                VoucherUrl.CASHBACK_UNTIL_URL
        )}
    private var onReturnToStep: (Int) -> Unit = { _ -> }
    private var getBannerBitmap: () -> Bitmap? = { null }
    private var getVoucherId: () -> Int? = { null }
    private var getPromoCodePrefix: () -> String = { "" }
    private var getBannerBaseUiModel: () -> BannerBaseUiModel = {
        BannerBaseUiModel(
                VoucherUrl.BANNER_BASE_URL,
                VoucherUrl.FREE_DELIVERY_URL,
                VoucherUrl.CASHBACK_URL,
                VoucherUrl.CASHBACK_UNTIL_URL
        )}
    private var getVoucherBanner: () -> BannerVoucherUiModel = {
        BannerVoucherUiModel(
                VoucherImageType.FreeDelivery(0),
                "",
                "",
                "")
    }
    private var isEdit: Boolean = false

    @Inject
    lateinit var userSession: UserSessionInterface

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
        VoucherDisplayBottomSheetFragment.createInstance(context, ::getPublicVoucherDisplay, userSession.userId)
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

    private val promoCodeErrorDialog by lazy {
        context?.run {
            PromoCodeErrorDialog(this, ::onDialogChangePromoCode, ::onPromoCodeErrorBack)
        }
    }

    private val buttonUiModel by lazy {
        val res =
                if (isEdit) {
                    R.string.mvc_save_voucher
                } else {
                    R.string.mvc_add_voucher
                }
        FooterButtonUiModel(context?.getString(res).toBlankOrString(), "", true)
    }

    private val sharedPref by lazy {
        context?.getSharedPreferences(VOUCHER_CREATION, Context.MODE_PRIVATE)
    }

    private val isDuplicate by lazy {
        activity?.intent?.getBooleanExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, false)
                ?: false
    }

    private val isFromVoucherList by lazy {
        activity?.intent?.getBooleanExtra(CreateMerchantVoucherStepsActivity.FROM_VOUCHER_LIST, false) ?: false
    }

    private val impressHolder = ImpressHolder()

    private var isWaitingForResult = false

    private var postVoucherUiModel: PostVoucherUiModel? = null

    private var squareVoucherBitmap: Bitmap? = null
        set(value) {
            if (field == null && isDuplicate) {
                recycler_view?.scrollToPosition(adapter.dataSize - 1)
            }
            field = value
        }

    private var voucherInfoSection: InfoContainerUiModel = InfoContainerUiModel(R.string.mvc_detail_voucher_info, listOf())

    private var isPromoCodeEligible = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addOnImpressionListener(impressHolder) {
            VoucherCreationTracking.sendOpenScreenTracking(
                    if (isDuplicate) {
                        VoucherCreationAnalyticConstant.ScreenName.VOUCHER_DUPLICATE_REVIEW
                    } else {
                        VoucherCreationAnalyticConstant.ScreenName.VoucherCreation.REVIEW
                    },
                    userSession.isLoggedIn,
                    userSession.userId)
        }
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
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_DISPLAY_PUBLIC,
                userId = userSession.userId
        )
        publicVoucherTipsAndTrickBottomSheet.run {
            setTitle(this@ReviewVoucherFragment.resources.getString(R.string.mvc_create_public_voucher_display_title).toBlankOrString())
            show(this@ReviewVoucherFragment.childFragmentManager, VoucherDisplayBottomSheetFragment.TAG)
        }
    }

    override fun onInfoContainerCtaClick(dataKey: String) {
        with(dataKey) {
            val eventAction =
                    when (this) {
                        VOUCHER_INFO_DATA_KEY -> VoucherCreationAnalyticConstant.EventAction.Click.EDIT_INFO_VOUCHER
                        VOUCHER_BENEFIT_DATA_KEY -> VoucherCreationAnalyticConstant.EventAction.Click.EDIT_VOUCHER_BENEFIT
                        PERIOD_DATA_KEY -> VoucherCreationAnalyticConstant.EventAction.Click.EDIT_PERIOD
                        DATA_KEY_VOUCHER_PERIOD -> VoucherCreationAnalyticConstant.EventAction.Click.PERIOD
                        else -> return@with
                    }
            VoucherCreationTracking.sendCreateVoucherClickTracking(
                    step = VoucherCreationStep.REVIEW,
                    action = eventAction,
                    userId = userSession.userId,
                    isDuplicate = isDuplicate
            )
        }
        val step = when (dataKey) {
            VOUCHER_INFO_DATA_KEY -> VoucherCreationStep.TARGET
            VOUCHER_BENEFIT_DATA_KEY -> VoucherCreationStep.BENEFIT
            PERIOD_DATA_KEY -> VoucherCreationStep.PERIOD
            DATA_KEY_VOUCHER_PERIOD -> VoucherCreationStep.PERIOD
            PROMO_CODE_KEY -> VoucherCreationStep.TARGET
            else -> VoucherCreationStep.REVIEW
        }
        onReturnToStep(step)
    }

    override fun onFooterCtaTextClickListener() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.TNC,
                userId = userSession.userId,
                isDuplicate = isDuplicate
        )
        termsAndConditionBottomSheet?.show(childFragmentManager, TermsAndConditionBottomSheetFragment.TAG)
    }

    override fun onTickerClicked() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.TOOLTIP_SPENDING_ESTIMATION,
                userId = userSession.userId,
                isDuplicate = isDuplicate
        )
        generalExpenseBottomSheet.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    override fun onFooterButtonClickListener() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.ADD_VOUCHER,
                userId = userSession.userId,
                isDuplicate = isDuplicate
        )
        if (getVoucherReviewUiModel().startDate.isBlank()) {
            view?.run {
                Toaster.make(this,
                        context?.getString(R.string.mvc_period_alert).toBlankOrString(),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        context?.getString(R.string.mvc_oke).toBlankOrString(),
                        View.OnClickListener { })
            }
            refreshFooterButton()
            return
        }
        if (!isPromoCodeEligible) {
            view?.run {
                Toaster.make(this,
                        context?.getString(R.string.mvc_promo_code_alert).toBlankOrString(),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        context?.getString(R.string.mvc_oke).toBlankOrString(),
                        View.OnClickListener { })
            }
            refreshFooterButton()
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

    override fun onImpression(dataKey: String) {
        when (dataKey) {
            PERIOD_DATA_KEY -> {
                if (!isEdit) {
                    VoucherCreationTracking.sendCreateVoucherImpressionTracking(
                            step = VoucherCreationStep.REVIEW,
                            action = VoucherCreationAnalyticConstant.EventAction.Impression.DISPLAY_PERIOD,
                            userId = userSession.userId
                    )
                }
            }
            else -> return
        }
    }

    override fun onPause() {
        super.onPause()
        childFragmentManager.dismissBottomSheetWithTags(
                GeneralExpensesInfoBottomSheetFragment.TAG,
                TermsAndConditionBottomSheetFragment.TAG,
                VoucherDisplayBottomSheetFragment.TAG
        )
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.createVoucherResponseLiveData) { result ->
                if (isWaitingForResult) {
                    when (result) {
                        is Success -> {
                            context?.run {
                                val eventLabel =
                                        when (getRecommendationStatus()) {
                                            VoucherRecommendationStatus.WITH_RECOMMENDATION -> VoucherCreationAnalyticConstant.EventLabel.WITH_RECOMMENDATION + result.data.toString()
                                            VoucherRecommendationStatus.EDITED_RECOMMENDATION -> VoucherCreationAnalyticConstant.EventLabel.EDITED_RECOMMENDATION + result.data.toString()
                                            VoucherRecommendationStatus.NO_RECOMMENDATION -> VoucherCreationAnalyticConstant.EventLabel.NO_RECOMMENDATION + result.data.toString()
                                            else -> VoucherCreationAnalyticConstant.EventLabel.NO_RECOMMENDATION + result.data.toString()
                                        }
                                VoucherCreationTracking.sendVoucherRecommendationStatus(eventLabel, userSession.shopId, userSession.userId)

                                // Send success voucher id to voucher list to display success bottomsheet/toaster
                                val intent = VoucherListActivity.createInstance(this, true).apply {
                                    putExtra(VoucherListActivity.SUCCESS_VOUCHER_ID_KEY, result.data)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                activity?.run {
                                    if (isFromVoucherList) {
                                        setResult(Activity.RESULT_OK, intent)
                                    }
                                    finish()
                                }
                                if (!isFromVoucherList) {
                                    startActivity(intent)
                                }

                                // Disable showing create voucher dialog upon accessing mvc after first time success
                                sharedPref?.run {
                                    if (getBoolean(IS_MVC_FIRST_TIME, true)) {
                                        edit().putBoolean(IS_MVC_FIRST_TIME, false).apply()
                                    }
                                }
                            }
                        }
                        is Fail -> {
                            if (result.throwable.message == PROMO_CODE_ERROR_RESPONSE) {
                                promoCodeErrorDialog?.show()
                            } else {
                                failedCreateVoucherDialog?.show()
                            }
                            // send crash report to firebase crashlytics
                            MvcErrorHandler.logToCrashlytics(result.throwable, CREATE_VOUCHER_ERROR)
                            // log error type to scalyr
                            val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                            ServerLogger.log(Priority.P2, "MVC_CREATE_VOUCHER_ERROR", mapOf("type" to errorMessage))
                        }
                    }
                    refreshFooterButton()
                    loadingDialog?.dismiss()
                }
                isWaitingForResult = false
            }
            observe(viewModel.updateVoucherSuccessLiveData) { result ->
                if (isWaitingForResult) {
                    when (result) {
                        is Success -> {
                            context?.run {
                                val intent = VoucherListActivity.createInstance(this, true).apply {
                                    putExtra(VoucherListActivity.UPDATE_VOUCHER_KEY, true)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                activity?.run {
                                    if (isFromVoucherList) {
                                        setResult(Activity.RESULT_OK, intent)
                                    }
                                    finish()
                                }
                                if (!isFromVoucherList) {
                                    startActivity(intent)
                                }
                            }
                        }
                        is Fail -> {
                            if (result.throwable.message == PROMO_CODE_ERROR_RESPONSE) {
                                promoCodeErrorDialog?.show()
                            } else {
                                failedCreateVoucherDialog?.show()
                            }
                            // send crash report to firebase crashlytics
                            MvcErrorHandler.logToCrashlytics(result.throwable, UPDATE_VOUCHER_ERROR)
                            // log error type to scalyr
                            val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                            ServerLogger.log(Priority.P2, "MVC_UPDATE_VOUCHER_ERROR", mapOf("type" to errorMessage))
                        }
                    }
                    refreshFooterButton()
                    loadingDialog?.dismiss()
                }
                isWaitingForResult = false
            }
        }
    }

    private fun renderReviewInformation(voucherReviewUiModel: VoucherReviewUiModel) {
        voucherReviewUiModel.run {
            val postDisplayedDate = getDisplayedDateString(context, startDate, endDate)
            val fullDisplayedDate: String? = if (startDate.isEmpty()) {
                null
            } else {
                getDisplayedDateString(context, startDate, startHour, endDate, endHour)
            }
            val displayedPromoCode =
                    when {
                        targetType == VoucherTargetType.PUBLIC -> ""
                        promoCode.isBlank() -> promoCode
                        else -> getPromoCodePrefix() + promoCode
                    }

            voucherInfoSection = getVoucherInfoSection(targetType, voucherName, displayedPromoCode, true)

            val reviewInfoList = mutableListOf(
                    voucherInfoSection,
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

            with(voucherReviewUiModel) {
                postVoucherUiModel = getVoucherPreviewSection(voucherType, voucherName, shopAvatarUrl, shopName, displayedPromoCode, postDisplayedDate).also {
                    reviewInfoList.add(0, it)
                }
            }

            isPromoCodeEligible = true

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
                                         promoPeriod: String): PostVoucherUiModel {
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
            getSquareVoucherBitmap { squareBitmap ->
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
        if (getBannerBitmap() == null) {
            drawNullBanner()
        } else {
            getBannerBitmap()?.let { bannerBitmap ->
                getVoucherId()?.let { voucherId ->
                    getSquareVoucherBitmap { squareBitmap ->
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
    }

    private fun onDialogTryAgain() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.FAILED_POP_UP_TRY_AGAIN,
                userId = userSession.userId
        )
        failedCreateVoucherDialog?.dismiss()
        loadingDialog?.show()
        isWaitingForResult = true
        createVoucher()
    }

    private fun onDialogRequestHelp() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.FAILED_POP_UP_HELP,
                userId = userSession.userId
        )
        failedCreateVoucherDialog?.dismiss()
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, VoucherUrl.HELP_URL)
    }

    private fun onDialogChangePromoCode() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.ERROR_CREATION_EDIT_PROMO_CODE,
                userId = userSession.userId
        )
        onReturnToStep(VoucherCreationStep.TARGET)
    }

    private fun onPromoCodeErrorBack() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.REVIEW,
                action = VoucherCreationAnalyticConstant.EventAction.Click.ERROR_CREATION_BACK_BUTTON,
                userId = userSession.userId
        )
        adapter.data.indexOf(voucherInfoSection).let { index ->
            voucherInfoSection.informationList.last().isWarning = true
            val warningIndex = index + 1
            adapter.data.add(warningIndex, WarningTickerUiModel(PROMO_CODE_KEY))
        }
        adapter.notifyDataSetChanged()
        isPromoCodeEligible = false
    }

    private fun drawNullBanner() {
        context?.run {
            Glide.with(this)
                    .asDrawable()
                    .load(getBannerBaseUiModel().bannerBaseUrl)
                    .signature(ObjectKey(System.currentTimeMillis().toString()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            showDrawingError(e)
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            activity?.runOnUiThread {
                                val bitmap = resource.toBitmap()
                                val painter = VoucherPreviewPainter(this@run, bitmap, ::onSuccessGetUpdateBitmap, getBannerBaseUiModel()) {
                                    showDrawingError(it)
                                }
                                painter.drawFull(getVoucherBanner(), bitmap)
                            }
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun onSuccessGetUpdateBitmap(bannerBitmap: Bitmap) {
        getBannerBitmap = { bannerBitmap }
        updateVoucher()
    }

    private fun refreshFooterButton() {
        adapter?.run {
            data?.indexOf(buttonUiModel)?.let { index ->
                (adapter.data[index] as? FooterButtonUiModel)?.isLoading = false
                notifyItemChanged(index)
            }
        }
    }

    /**
     * This method is used to create reliable voucher image every time square voucher bitmap is needed.
     */
    private fun getSquareVoucherBitmap(onSuccessGetBitmap: (Bitmap) -> Unit) {
        context?.run {
            Glide.with(this)
                    .asDrawable()
                    .load(getPostBaseUiModel().postBaseUrl)
                    .signature(ObjectKey(System.currentTimeMillis().toString()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            refreshFooterButton()
                            loadingDialog?.dismiss()
                            failedCreateVoucherDialog?.show()
                            e?.run {
                                MvcErrorHandler.logToCrashlytics(this, LOAD_IMAGE_ERROR)
                            }
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            activity?.runOnUiThread {
                                val bitmap = resource.toBitmap()
                                val painter = SquareVoucherPainter(this@run, bitmap, onSuccessGetBitmap)
                                postVoucherUiModel?.let {
                                    painter.drawInfo(it)
                                }
                            }
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun showDrawingError(error: Throwable?) {
        view?.run {
            Toaster.make(this,
                    context?.getString(R.string.mvc_general_error).toBlankOrString(),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_ERROR)
        }
        refreshFooterButton()
        error?.run {
            MvcErrorHandler.logToCrashlytics(this, LOAD_IMAGE_ERROR)
        }
    }

}