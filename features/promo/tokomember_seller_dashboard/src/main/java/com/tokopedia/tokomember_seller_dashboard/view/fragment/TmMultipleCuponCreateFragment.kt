package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_common_widget.util.ProgramDateType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponPreviewData
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.model.ValidationError
import com.tokopedia.tokomember_seller_dashboard.model.mapper.TmCouponCreateMapper
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ANDROID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_CREATE_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_COUPON_PREVIEW_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CREATE_SCREEN_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DURATION
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID_IN_TOOLS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_IDR
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_PERCENTAGE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_SUBTITLE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_SUBTITLE_2
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TERMS_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.CREATE
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC_END
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE_END
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ErrorState
import com.tokopedia.tokomember_seller_dashboard.util.PREMIUM
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_EXTEND_CTA
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_TYPE_AUTO
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_TYPE_MANUAL
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_VALIDATION_CTA_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.SIMPLE_DATE_FORMAT
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE_MULTIPLE_COUPON_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE_MULTIPLE_COUPON_EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.TERMS
import com.tokopedia.tokomember_seller_dashboard.util.TERNS_AND_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.TIME_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TIME_DESC_END
import com.tokopedia.tokomember_seller_dashboard.util.TIME_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TIME_TITLE_END
import com.tokopedia.tokomember_seller_dashboard.util.TM_ERROR_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUMMARY_DIALOG_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUMMARY_DIALOG_TITLE_START_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.TM_TNC
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTime
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTimeRemoveTimeDiff
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getDayOfWeekID
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getTimeInMillis
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDatePreview
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setTime
import com.tokopedia.tokomember_seller_dashboard.util.TmFileUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.util.VIP
import com.tokopedia.tokomember_seller_dashboard.util.locale
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TmCouponListItemPreview
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmExpandView.collapse
import com.tokopedia.tokomember_seller_dashboard.view.animation.TmExpandView.expand
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmSingleCouponView
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmEligibilityViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_kupon_create_multiple.*
import kotlinx.android.synthetic.main.tm_dash_tnc_coupon_creation.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val MINUTE_30 = 30
private const val MINUTE_59 = 59
private const val HOUR_4 = 4
private const val HOUR_20 = 20
private const val HOUR_23 = 23
private const val ROTATION_180 = 180f
private const val PROGRESS_67 = 67
private const val PROGRESS_80 = 80

class TmMultipleCuponCreateFragment : BaseDaggerFragment() {

    private var tmTracker: TmTracker? = null
    private var selectedChipPositionDate: Int = 0
    private var selectedCalendar: Calendar? = null
    private var selectedTime = ""
    private var startTime: Calendar? = null
    private var couponPremiumData: TmSingleCouponData? = null
    private var couponVip: TmSingleCouponData? = null
    private var tmCouponPreviewData = TmCouponPreviewData()
    private var shopName = ""
    private var shopAvatar = ""
    private var retryCount = 0
    private val errorState = ErrorState()
    private var loaderDialog: LoaderDialog? = null
    private var tmCouponListVipItemPreview = TmCouponListItemPreview()
    private var tmCouponListPremiumItemPreview = TmCouponListItemPreview()
    private var tmCouponPremiumUploadId = ""
    private var tmCouponVipUploadId = ""
    private var tmToken = ""
    private var imageSquare = ""
    private var imagePortrait = ""
    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    private var isCollapsedVip = true
    private var isCollapsedPremium = true
    private var manualStartTimeProgram = ""
    private var manualEndTimeProgram = ""
    private var couponStartDate = ""
    private var couponStartTime = ""
    private var couponEndDate = ""
    private var couponEndTime = ""
    private var programActionType = ProgramActionType.CREATE
    private var updatedStartTimeCoupon = ""
    private var updatedEndTimeCoupon = ""
    private var isDateManual = false
    private var programType = ""
    private var periodMonth = 0
    private var firstTimeStart = false
    private var firstTimeEnd = false
    private var tmCouponStartTimeUnix : Calendar? = null
    private var tmCouponEndTimeUnix : Calendar? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }

    private val tmEligibilityViewModel: TmEligibilityViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmEligibilityViewModel::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is TmOpenFragmentCallback) {
            tmOpenFragmentCallback = context
        } else {
            throw Exception(context.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_kupon_create_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmTracker = TmTracker()

        shopName = arguments?.getString(BUNDLE_SHOP_NAME) ?: ""
        shopAvatar = arguments?.getString(BUNDLE_SHOP_AVATAR) ?: ""
        programActionType = arguments?.getInt(BUNDLE_PROGRAM_TYPE, 0)?:0
        periodMonth = arguments?.getInt(BUNDLE_PROGRAM_DURATION, 0)?:0
        setProgramType(programActionType)

        renderHeader()
        renderButton()
        observeViewModel()

        tmEligibilityViewModel.getSellerInfo()
        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.getInitialCouponData(CREATE, "")
        }
        else{
            noInternetUi { tmDashCreateViewModel.getInitialCouponData(CREATE, "") }
        }
    }

    private fun noInternetUi(action: () -> Unit) {
        //show no internet bottomsheet

        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            ERROR_CREATING_TITLE_NO_INTERNET,
            ERROR_CREATING_DESC_NO_INTERNET,
            "",
            RETRY,
            errorCount = retryCount,
            showSecondaryCta = true
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                action()
            }})
        if(programActionType == ProgramActionType.CREATE){
            bottomsheet.setSecondaryCta {
                arguments?.getInt(BUNDLE_SHOP_ID)?.let {
                    TokomemberDashIntroActivity.openActivity(
                        it,
                        arguments?.getString(BUNDLE_SHOP_AVATAR).toString(),
                        arguments?.getString(BUNDLE_SHOP_NAME).toString(),
                        context = context
                    )
                }
                activity?.finish()
            }
        }
        bottomsheet.show(childFragmentManager,"")
        setButtonState()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val dagger = DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
        dagger.inject(this)
    }

    private fun setProgramType(programActionType: Int) {
        when (programActionType) {
            ProgramActionType.CREATE -> {
                programType = "create"
            }
            ProgramActionType.EXTEND -> {
                programType = "extend"
            }
        }
    }

    private fun observeViewModel() {

        tmEligibilityViewModel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    shopAvatar = it.data.userShopInfo?.info?.shopAvatar?:""
                    shopName = it.data.userShopInfo?.info?.shopName?:""
                    tmPremiumCoupon.setShopData(shopName, shopAvatar)
                    tmVipCoupon.setShopData(shopName, shopAvatar)
                }
                is Fail -> {
                }
            }
        })


        tmDashCreateViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    tmToken = it.data?.getInitiateVoucherPage?.data?.token ?: ""
                    imageSquare = it.data?.getInitiateVoucherPage?.data?.imgBannerIgPost ?: ""
                    imagePortrait = it.data?.getInitiateVoucherPage?.data?.imgBannerIgStory ?: ""
                    tmDashCreateViewModel.getProgramInfo(arguments?.getInt(
                        BUNDLE_PROGRAM_ID_IN_TOOLS)?:0,arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,programType)
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleDataError()
                }
            }
        })

        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    containerViewFlipper.displayedChild = DATA
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        renderProgram(it.data.membershipGetProgramForm.programForm?.timeWindow)
                        renderMultipleCoupon()
                    }
                    else{
                        handleDataError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleDataError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponPreValidateSingleCouponLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING -> {
                    openLoadingDialog()
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    errorState.isPreValidateVipError = false
                    if (it.data?.voucherValidationPartial?.header?.messages?.size == 0) {
                        preValidateCouponVip(couponVip)
                    } else {
                        closeLoadingDialog()
                        view?.let { v ->
                            Toaster.build(
                                v,
                                "Cek dan pastikan semua informasi yang kamu isi sudah benar, ya.",
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR
                                ).show()
                        }
                        setButtonState()
                        handleProgramValidateError(
                            it.data?.voucherValidationPartial?.data?.validationError,
                            "premium"
                        )
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    setButtonState()
                    errorState.isPreValidateVipError = true
                    closeLoadingDialog()
                    handleProgramValidateNetworkError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponPreValidateMultipleCouponLiveData.observe(viewLifecycleOwner,
            {
                when (it.status) {
                    TokoLiveDataResult.STATUS.SUCCESS -> {
                        errorState.isPreValidatePremiumError = false
                        if (it.data?.voucherValidationPartial?.header?.messages?.size == 0) {
                            var source = SOURCE_MULTIPLE_COUPON_CREATE
                            if(programActionType == ProgramActionType.EXTEND){
                                source = SOURCE_MULTIPLE_COUPON_EXTEND
                            }
                            validateProgram(source)
                        } else {
                            closeLoadingDialog()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    "Cek dan pastikan semua informasi yang kamu isi sudah benar, ya.",
                                    Toaster.LENGTH_LONG,
                                    Toaster.TYPE_ERROR
                                    ).show()
                            }
                            setButtonState()
                            handleProgramValidateError(
                                it.data?.voucherValidationPartial?.data?.validationError,
                                "vip"
                            )
                        }
                    }
                    TokoLiveDataResult.STATUS.ERROR -> {
                        errorState.isPreValidatePremiumError = true
                        setButtonState()
                        closeLoadingDialog()
                        handleProgramValidateNetworkError()
                    }
                    else -> {
                        handleServerError()
                    }
                }
            })

        tmDashCreateViewModel.tmProgramValidateLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when (it.data?.membershipValidateBenefit?.resultStatus?.code) {
                        CODE_SUCCESS -> {
                            errorState.isValidateCouponError = false
                            uploadImageVip()
                        }
                        CODE_PROGRAM_OUTSIDE,CODE_PROGRAM_OUTSIDE_V2  -> {
                            closeLoadingDialog()
                            setButtonState()
                            handleProgramPreValidateError(it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(0), it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(1) , PROGRAM_VALIDATION_CTA_TEXT)
                        }
                        else -> {
                            closeLoadingDialog()
                            setButtonState()
                            handleProgramValidateServerError(it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(0), it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(1))
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isValidateCouponError = false
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateServerError("","")
                }
                else -> {
                    handleServerError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponUploadLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when (it.data) {
                        is UploadResult.Success -> {
                            errorState.isUploadPremium = false
                            tmCouponPremiumUploadId = it.data.uploadId
                            uploadImagePremium()
                        }
                        is UploadResult.Error -> {
                            closeLoadingDialog()
                            setButtonState()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    it.data.message,
                                    Toaster.LENGTH_LONG,
                                    Toaster.TYPE_ERROR
                                ).show()
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isUploadPremium = true
                    handleServerError()
                }
                else -> {
                    handleServerError()
                }
            }
        })

        tmDashCreateViewModel.tmCouponUploadMultipleLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when (it.data) {
                        is UploadResult.Success -> {
                            errorState.isUploadVipError = false
                            tmCouponVipUploadId = it.data.uploadId
                            openPreviewPage()
                        }
                        is UploadResult.Error -> {
                            closeLoadingDialog()
                            setButtonState()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    it.data.message,
                                    Toaster.LENGTH_LONG,
                                    Toaster.TYPE_ERROR
                                    ).show()
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isUploadVipError = true
                    handleServerError()
                }
                else -> {
                    handleServerError()
                }
            }
        })
    }

    private fun validateProgram(source: String) {
        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.validateProgram(
                arguments?.getInt(
                    BUNDLE_SHOP_ID
                ).toString(),
                getTimeInMillis(updatedStartTimeCoupon),
                getTimeInMillis(updatedEndTimeCoupon),
                source
            )
        }
        else{
            noInternetUi{validateProgram(source)}
        }
    }

    private fun handleServerError(){
        setButtonState()
        closeLoadingDialog()
        view?.let { v ->
            Toaster.build(
                v,
                RETRY,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun openLoadingDialog() {
        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        val ss = SpannableString(TM_SUMMARY_DIALOG_TITLE)
        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            0, TM_SUMMARY_DIALOG_TITLE_START_TEXT.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        loaderDialog?.setLoadingText(ss)
        loaderDialog?.loaderText?.gravity= Gravity.CENTER_HORIZONTAL
        loaderDialog?.show()
    }

    private fun closeLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    private fun handleDataError(){
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {

            if (TmInternetCheck.isConnectedToInternet(context)) {
                tmDashCreateViewModel.getInitialCouponData(CREATE, "")
            }
            else{
                noInternetUi{ handleDataError()}
            }
        }
    }

    private fun handleProgramValidateNetworkError() {
        view?.let { Toaster.build(it, RETRY, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
        btnContinue.apply {
            isEnabled = true
            isClickable = true
        }
    }

    private fun handleProgramValidateError(validationError: ValidationError?, couponType: String) {
        when (couponType) {

            PREMIUM -> {
                if (!validationError?.benefitIdr.isNullOrEmpty()) {
                    tmPremiumCoupon?.setErrorMaxBenefit(validationError?.benefitIdr ?: "")
                }
                if (!validationError?.benefitMax.isNullOrEmpty()) {
                    tmPremiumCoupon?.setErrorMaxBenefit(validationError?.benefitMax ?: "")
                }
                tmPremiumCoupon?.setErrorMaxBenefit(validationError?.benefitIdr ?: "")
                tmPremiumCoupon?.setErrorCashbackPercentage(validationError?.benefitPercent ?: "")
                tmPremiumCoupon?.setErrorMinTransaction(validationError?.minPurchase ?: "")
                tmPremiumCoupon?.setErrorQuota(validationError?.quota ?: "")
                if (!tmPremiumCoupon.isVisible) {
                    icArrowPremium.performClick()
                }
            }

            VIP -> {
                if (!validationError?.benefitIdr.isNullOrEmpty()) {
                    tmVipCoupon?.setErrorMaxBenefit(validationError?.benefitIdr ?: "")
                }
                if (!validationError?.benefitMax.isNullOrEmpty()) {
                    tmVipCoupon?.setErrorMaxBenefit(validationError?.benefitMax ?: "")
                }
                tmVipCoupon?.setErrorCashbackPercentage(validationError?.benefitPercent ?: "")
                tmVipCoupon?.setErrorMinTransaction(validationError?.minPurchase ?: "")
                tmVipCoupon?.setErrorQuota(validationError?.quota ?: "")
                if (!tmVipCoupon.isVisible) {
                    icArrowVip.performClick()
                }
            }
        }
    }

    private fun setButtonState() {
        btnContinue.apply {
            isClickable = true
            isEnabled = true
        }
    }

    private fun handleProgramPreValidateError(reason: String?, message: String? ,ctaError:String ="") {
        val bundle = Bundle()

        val title = if(reason.isNullOrEmpty()) {
            when (retryCount) {
                0 -> ERROR_CREATING_TITLE
                else -> ERROR_CREATING_TITLE_RETRY
            }
        }
        else{
            reason
        }
        var desc = ERROR_CREATING_DESC
        if(!message.isNullOrEmpty()){
            desc = message
        }

        val image: String
        val cta =
            if (ctaError.isEmpty()) {
                image = ""
                when (retryCount) {
                    0 -> RETRY
                    else -> ERROR_CREATING_CTA_RETRY
                }
            } else {
                image = TM_ERROR_PROGRAM
                ctaError
            }

        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            desc,
            image,
            cta
        )
        bundle.putString(
            TokomemberBottomsheet.ARG_BOTTOMSHEET,
            Gson().toJson(tmIntroBottomSheetModel)
        )
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                bottomSheet.dismiss()
            }
        })
        bottomSheet.show(childFragmentManager, "")
    }

    private fun handleProgramValidateServerError(titleError: String?, messageError: String?) {

        val title =  if (titleError.isNullOrEmpty()) {
            when (retryCount) {
                0 -> ERROR_CREATING_TITLE
                else -> ERROR_CREATING_TITLE_RETRY
            }
        } else{
            titleError
        }
        val cta = when (retryCount) {
            0 -> RETRY
            else -> ERROR_CREATING_CTA_RETRY
        }
        var desc = ERROR_CREATING_DESC
        if(!messageError.isNullOrEmpty()){
            desc = messageError
        }
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            desc,
            "",
            cta,
            errorCount = retryCount
        )
        bundle.putString(
            TokomemberBottomsheet.ARG_BOTTOMSHEET,
            Gson().toJson(tmIntroBottomSheetModel)
        )
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                when (errorCount) {
                    0 -> {

                        if (TmInternetCheck.isConnectedToInternet(context)) {
                            tmDashCreateViewModel.validateProgram(
                                arguments?.getInt(
                                    BUNDLE_SHOP_ID
                                ).toString(),
                                getTimeInMillis(updatedStartTimeCoupon),
                                getTimeInMillis(updatedEndTimeCoupon),
                                SOURCE_MULTIPLE_COUPON_CREATE
                            )
                        }
                        else{
                            noInternetUi{
                                tmDashCreateViewModel.validateProgram(
                                    arguments?.getInt(
                                        BUNDLE_SHOP_ID
                                    ).toString(),
                                    getTimeInMillis(updatedStartTimeCoupon),
                                    getTimeInMillis(updatedEndTimeCoupon),
                                    SOURCE_MULTIPLE_COUPON_CREATE
                                )}
                        }
                    }
                    else -> {
                        if (programActionType == ProgramActionType.CREATE) {
                            (TokomemberDashIntroActivity.openActivity(
                                arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,
                                arguments?.getString(BUNDLE_SHOP_AVATAR) ?: "",
                                arguments?.getString(
                                    BUNDLE_SHOP_NAME
                                ) ?: "",
                                false,
                                context
                            ))
                            activity?.finish()
                        } else {
                            activity?.finish()
                        }
                    }
                }
            }
        })
        bottomSheet.show(childFragmentManager, "")
        retryCount += 1
    }

    private fun openPreviewPage() {

        val maxBenefit =
            (CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback?:"")* CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.quota?:"")) +
        CurrencyFormatHelper.convertRupiahToInt(couponVip?.maxCashback?:"") * CurrencyFormatHelper.convertRupiahToInt(couponVip?.quota?:"")

        //GET Coupon Time
        tmCouponPreviewData.voucherList.clear()
        tmCouponPreviewData.apply {
            startDate = textFieldProgramStartDate.editText.text.toString()
            endDate = textFieldProgramEndDate.editText.text.toString()
            startTime = textFieldProgramStartTime.editText.text.toString().replace(" ","").substringBefore("W")
            endTime = textFieldProgramEndTime.editText.text.toString().replace(" ","").substringBefore("W")
            maxValue = maxBenefit.toString()
            voucherList.add(0, tmCouponListPremiumItemPreview)
            voucherList.add(1, tmCouponListVipItemPreview)
        }

        //Map data
        val tmMerchantCouponCreateData = TmCouponCreateMapper.mapCreateData(
            couponPremiumData,
            couponVip,
            tmCouponPremiumUploadId,
            tmCouponVipUploadId,
            couponStartDate,
            couponEndDate,
            couponStartTime,
            couponEndTime,
            tmToken, imageSquare, imagePortrait, maxBenefit, programActionType
        )

        val bundle = Bundle()
        bundle.putInt(BUNDLE_CARD_ID, arguments?.getInt(BUNDLE_CARD_ID)?:0)
        bundle.putString(BUNDLE_SHOP_AVATAR, shopAvatar)
        bundle.putString(BUNDLE_SHOP_NAME, shopName)
        bundle.putInt(BUNDLE_CARD_ID_IN_TOOLS,arguments?.getInt(BUNDLE_CARD_ID_IN_TOOLS) ?: 0)
        bundle.putInt(BUNDLE_SHOP_ID, arguments?.getInt(BUNDLE_SHOP_ID) ?: 0)
        bundle.putInt(BUNDLE_PROGRAM_ID_IN_TOOLS, arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS) ?: 0)
        bundle.putParcelable(BUNDLE_COUPON_PREVIEW_DATA, tmCouponPreviewData)
        bundle.putInt(BUNDLE_PROGRAM_TYPE, programActionType)
        bundle.putParcelable(BUNDLE_COUPON_CREATE_DATA, tmMerchantCouponCreateData)
        tmOpenFragmentCallback.openFragment(CreateScreenType.PREVIEW_EXTEND, bundle)
        closeLoadingDialog()
        setButtonState()
    }

    private fun setProgramStartEndDate(timeWindow: TimeWindow?) {
        when (programActionType) {
            ProgramActionType.EXTEND -> {
                val startDate = GregorianCalendar(locale)
                startDate.add(Calendar.HOUR, HOUR_4)
                val minuteCurrent = startDate.get(Calendar.MINUTE)
                if (minuteCurrent <= MINUTE_30) {
                    startDate.set(Calendar.MINUTE, MINUTE_30)
                } else {
                    startDate.add(Calendar.HOUR_OF_DAY, 1)
                    startDate.set(Calendar.MINUTE, 0)
                }
                startDate.set(Calendar.SECOND, 0)
                manualStartTimeProgram = convertDateTimeRemoveTimeDiff(startDate.time)

                manualEndTimeProgram = TmDateUtil.addDuration(timeWindow?.endTime ?: "", periodMonth)
                val maxProgramEndDate = GregorianCalendar(locale)
                maxProgramEndDate.add(Calendar.YEAR, 1)
                maxProgramEndDate.set(Calendar.HOUR_OF_DAY, HOUR_23)
                maxProgramEndDate.set(Calendar.MINUTE, MINUTE_59)
                val endDate = GregorianCalendar(locale)
                val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                endDate.time = sdf.parse(manualEndTimeProgram + "00") ?: Date()
                manualEndTimeProgram = if (endDate > maxProgramEndDate) {
                    convertDateTimeRemoveTimeDiff(maxProgramEndDate.time)
                } else {
                    convertDateTimeRemoveTimeDiff(endDate.time)
                }
            }
            else -> {
                val currentDate = GregorianCalendar(locale)
                val currentHour = currentDate.get(Calendar.HOUR_OF_DAY)
                val minuteCurrent = currentDate.get(Calendar.MINUTE)
                val currentStartDate = GregorianCalendar(locale)
                val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                currentStartDate.time = sdf.parse(timeWindow?.startTime ?: "" + "00") ?: Date()
                //currentDate 14 july time jo bhi hoga
                //currentStartDate 15 july 00:00
                manualStartTimeProgram =
                    if (currentHour >= HOUR_20 && checkYesterDay(currentDate, currentStartDate)) {
                        currentDate.set(Calendar.HOUR_OF_DAY,currentHour)
                        currentDate.set(Calendar.MINUTE,0)
                        currentDate.add(Calendar.HOUR, HOUR_4)
                        if (minuteCurrent <= MINUTE_30) {
                            currentDate.set(Calendar.MINUTE, MINUTE_30)
                        } else {
                            currentDate.add(Calendar.HOUR_OF_DAY, 1)
                            currentDate.set(Calendar.MINUTE, 0)
                        }
                        currentDate.set(Calendar.SECOND,0)
                        convertDateTimeRemoveTimeDiff(currentDate.time)
                    }else {
                        timeWindow?.startTime ?: ""
                    }
                manualEndTimeProgram = timeWindow?.endTime ?: ""
            }
        }
        updatedStartTimeCoupon = manualStartTimeProgram
        updatedEndTimeCoupon = manualEndTimeProgram
    }

    private fun checkYesterDay(calendarToday: Calendar, calendarProgram: Calendar): Boolean {
        return calendarToday.get(Calendar.YEAR) == calendarProgram.get(Calendar.YEAR)
                && calendarToday.get(Calendar.DAY_OF_YEAR) == calendarProgram.get(Calendar.DAY_OF_YEAR) - 1
    }

    private fun initTotalTransactionAmount() {
        tmPremiumCoupon?.setMaxTransactionListener(object : TmSingleCouponView.MaxTransactionListener{
            override fun onQuotaCashbackChange() {
                setTotalTransactionAmount()
            }
        })

        tmVipCoupon?.setMaxTransactionListener(object : TmSingleCouponView.MaxTransactionListener{
            override fun onQuotaCashbackChange() {
                setTotalTransactionAmount()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalTransactionAmount() {
        couponPremiumData = tmPremiumCoupon?.getSingleCouponData()
        couponVip = tmVipCoupon?.getSingleCouponData()
        tvTotalTransaction.text = "Rp" + CurrencyFormatHelper.convertToRupiah(
            ((CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback ?: "") *
                    couponPremiumData?.quota.toIntSafely()) +
                    (CurrencyFormatHelper.convertRupiahToInt(couponVip?.maxCashback ?: "") *
                            couponVip?.quota.toIntSafely())).toString())
    }

    private fun renderHeader() {
        headerKupon?.apply {

            progressKupon?.apply {
                progressBarColorType = ProgressBarUnify.COLOR_GREEN
                progressBarHeight = ProgressBarUnify.SIZE_SMALL
                setValue(PROGRESS_80, false)
            }

            title = COUPON_HEADER_TITLE
            when(programActionType){
                ProgramActionType.CREATE_BUAT ->{
                    title = PROGRAM_CTA
                    subtitle = COUPON_HEADER_SUBTITLE_2
                }
                ProgramActionType.CREATE ->{
                    subtitle = COUPON_HEADER_SUBTITLE
                }
                ProgramActionType.CREATE_FROM_COUPON ->{
                    title = PROGRAM_CTA
                    subtitle = COUPON_HEADER_SUBTITLE_2

                    progressKupon?.apply {
                        progressBarColorType = ProgressBarUnify.COLOR_GREEN
                        progressBarHeight = ProgressBarUnify.SIZE_SMALL
                        setValue(PROGRESS_67, false)
                    }
                }
                ProgramActionType.EXTEND ->{
                    title = PROGRAM_EXTEND_CTA
                    subtitle = COUPON_HEADER_SUBTITLE_2
                }
            }

            isShowBackButton = true
            setNavigationOnClickListener {
                if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_BUAT){
                    tmTracker?.clickCouponCreationBackFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_EXTEND){
                    tmTracker?.clickProgramExtensionCouponBack(arguments?.getInt(BUNDLE_SHOP_ID).toString(), arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS).toString())
                }
                else{
                    tmTracker?.clickCouponCreationBack(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                }
                activity?.onBackPressed()
            }
        }
    }

    private fun renderButton() {

        val webViewTnc = View.inflate(context,R.layout.tm_dash_tnc_coupon_creation,null)
        webViewTnc.webview.loadUrl(TM_TNC)
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            setTitle(TERNS_AND_CONDITION)
            setChild(webViewTnc)
            showCloseIcon = true
            isDragable = true
            setCloseClickListener {
                dismiss()
            }
        }
        val ss = SpannableString(COUPON_TERMS_CONDITION)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                RouteManager.route(context,String.format("%s?url=%s", ApplinkConst.WEBVIEW, TM_TNC))
               // bottomSheetUnify.show(childFragmentManager, "")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val firstIndex = COUPON_TERMS_CONDITION.indexOf(TERMS)
        ss.setSpan(
            clickableSpan,
            firstIndex,
            firstIndex + TERNS_AND_CONDITION.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ss.setSpan(
            StyleSpan(Typeface.BOLD),
            firstIndex,
            firstIndex + TERMS.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvTermsAndCondition.text = ss
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        btnContinue.setOnClickListener {
            if (getTimeInMillis(updatedStartTimeCoupon) > getTimeInMillis(updatedEndTimeCoupon)) {
                view?.let { it1 -> Toaster.build(it1,"Pengaturan tidak disimpan. Pastikan tanggal berakhir tidak mendahului tanggal mulai.",Toaster.LENGTH_LONG,Toaster.TYPE_ERROR).show() }
            } else {
                if (arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_BUAT) {
                    tmTracker?.clickCouponCreationFromProgramList(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString()
                    )
                }
                else if (arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_EXTEND){
                    tmTracker?.clickProgramExtensionCouponCreation(arguments?.getInt(BUNDLE_SHOP_ID).toString(), arguments?.getInt(BUNDLE_PROGRAM_ID_IN_TOOLS).toString())
                }
                else {
                    tmTracker?.clickCouponCreationButton(
                        arguments?.getInt(BUNDLE_SHOP_ID).toString()
                    )
                }
                it.isEnabled = false
                it.isClickable = false
                couponPremiumData = tmPremiumCoupon?.getSingleCouponData()
                couponVip = tmVipCoupon?.getSingleCouponData()

                preValidateCouponPremium(couponPremiumData)
            }
        }
    }

    private fun preValidateCouponVip(coupon: TmSingleCouponData?) {
        val validationRequest = TmCouponValidateRequest(
            targetBuyer = 3,
            benefitType = coupon?.typeCashback,
            couponType = coupon?.typeCoupon,
            benefitPercent = coupon?.cashBackPercentage,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(coupon?.minTransaki ?: ""),
            source = ANDROID,
            quota = CurrencyFormatHelper.convertRupiahToInt(coupon?.quota?:"")
        )

        val cashBackValue = CurrencyFormatHelper.convertRupiahToInt(coupon?.maxCashback?:"")
        when (coupon?.typeCashback) {
            CASHBACK_IDR -> {
                validationRequest.apply {
                    benefitIdr = cashBackValue
                }
            }
            CASHBACK_PERCENTAGE -> {
                validationRequest.apply {
                    benefitMax = cashBackValue
                }
            }
        }

        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.preValidateMultipleCoupon(validationRequest)
        }
        else{
            noInternetUi{ preValidateCouponVip(coupon) }
        }
    }

    private fun preValidateCouponPremium(couponPremiumData: TmSingleCouponData?) {
        val validationRequest = TmCouponValidateRequest(
            targetBuyer = 3,
            benefitType = couponPremiumData?.typeCashback,
            couponType = couponPremiumData?.typeCoupon,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(
                couponPremiumData?.minTransaki ?: ""
            ),
            source = ANDROID,
            quota = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.quota?:"")
        )
        val cashBackValue = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback?:"")
        when (couponPremiumData?.typeCashback) {
            CASHBACK_IDR -> {
                validationRequest.apply {
                    benefitIdr = cashBackValue
                }
            }
            CASHBACK_PERCENTAGE -> {
                validationRequest.apply {
                    benefitMax = cashBackValue
                }
            }
        }
        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmDashCreateViewModel.preValidateCoupon(validationRequest)
        }
        else{
            noInternetUi{ preValidateCouponPremium(couponPremiumData)}
        }
    }

    private fun uploadImagePremium() {
        context?.let { ctx ->
            tmVipCoupon?.post {
                val coupon = tmPremiumCoupon.getCouponView()
                val file =  TmFileUtil.saveBitMap(ctx, coupon)
                tmCouponListPremiumItemPreview = TmCouponListItemPreview(
                    file.absolutePath, "Premium", couponPremiumData?.quota ?: "100"
                )

                if (TmInternetCheck.isConnectedToInternet(context)) {
                    tmDashCreateViewModel.uploadImagePremium(file)
                }
                else{
                    noInternetUi{ uploadImagePremium() }
                }
            }
        }
    }

    private fun uploadImageVip() {
        context?.let { ctx ->
            tmVipCoupon?.post {
                val coupon = tmVipCoupon.getCouponView()
               val file =  TmFileUtil.saveBitMap(ctx, coupon)
                tmCouponListVipItemPreview = TmCouponListItemPreview(
                    file.absolutePath, "VIP", couponVip?.quota ?: "100"
                )

                if (TmInternetCheck.isConnectedToInternet(context)) {
                    tmDashCreateViewModel.uploadImageVip(file)                }
                else{
                    noInternetUi{tmDashCreateViewModel.uploadImageVip(file)}
                }

            }
        }
    }

    private fun renderMultipleCoupon() {

        setTotalTransactionAmount()
        tvTotalTransaction.text
        tmPremiumCoupon?.setShopData(shopName, shopAvatar)
        tmVipCoupon?.setShopData(shopName, shopAvatar)

        icArrowPremium.setOnClickListener {
            isCollapsedPremium = if (isCollapsedPremium) {
                icArrowPremium?.animate()?.rotation(ROTATION_180)?.duration = 100L
                expand(tmPremiumCoupon)
                !isCollapsedPremium
            } else{
                icArrowPremium?.animate()?.rotation(0f)?.duration = 100L
                collapse(tmPremiumCoupon)
                !isCollapsedPremium
            }
        }
        setTickerText(context?.resources?.getString(R.string.tm_vip_coupon_ticker_text).orEmpty())
        icArrowVip.setOnClickListener {
            isCollapsedVip = if (isCollapsedVip) {
                icArrowVip?.animate()?.rotation(ROTATION_180)?.duration = 100L
                expand(tmVipCoupon)
                !isCollapsedVip
            } else{
                icArrowVip?.animate()?.rotation(0f)?.duration = 100L
                collapse(tmVipCoupon)
                !isCollapsedVip
            }
        }

        initTotalTransactionAmount()

    }

    private fun setTickerText(text:String){
        ticker.setTextDescription(text)
    }

    private fun renderProgram(timeWindow: TimeWindow?) {
        setProgramStartEndDate(timeWindow)
        setProgramDateAuto()
        chipDateSelection.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionDate = position
                setProgramStartEndDate(timeWindow)
                setProgramDateAuto()
            }
        })
        chipDateSelection.setDefaultSelection(selectedChipPositionDate)
        chipDateSelection.addChips(arrayListOf(PROGRAM_TYPE_AUTO, PROGRAM_TYPE_MANUAL))

        textFieldProgramStartDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramStartTime.setFirstIcon(R.drawable.tm_dash_clock)
        textFieldProgramEndDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramEndTime.setFirstIcon(R.drawable.tm_dash_clock)

        textFieldProgramStartDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramStartDate, 0 , DATE_TITLE , DATE_DESC)
        }

        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramStartTime, 0 , TIME_TITLE, TIME_DESC)
        }

        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramEndDate, 1, DATE_TITLE_END, DATE_DESC_END)
        }

        textFieldProgramEndTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramEndTime, 1, TIME_TITLE_END, TIME_DESC_END)
        }

    }

    private fun setProgramDateAuto() {
        when (selectedChipPositionDate) {
            ProgramDateType.AUTO -> {
                setProgramCouponDate()
                textFieldProgramStartDate.isEnabled = false
                textFieldProgramStartDate.iconContainer.isEnabled = false
                textFieldProgramStartTime.isEnabled = false
                textFieldProgramStartTime.iconContainer.isEnabled = false
                textFieldProgramEndDate.isEnabled = false
                textFieldProgramEndDate.iconContainer.isEnabled = false
                textFieldProgramEndTime.isEnabled = false
                textFieldProgramEndTime.iconContainer.isEnabled = false
            }
            ProgramDateType.MANUAL -> {
                textFieldProgramStartDate.isEnabled = true
                textFieldProgramStartDate.iconContainer.isEnabled = true
                textFieldProgramStartTime.isEnabled = true
                textFieldProgramStartTime.iconContainer.isEnabled = true
                textFieldProgramEndDate.isEnabled = true
                textFieldProgramEndDate.iconContainer.isEnabled = true
                textFieldProgramEndTime.isEnabled = true
                textFieldProgramEndTime.iconContainer.isEnabled = true
                textFieldProgramStartDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramStartTime.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndTime.editText.inputType = InputType.TYPE_NULL

                textFieldProgramStartDate.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickDatePicker(textFieldProgramStartDate, 0, DATE_TITLE , DATE_DESC)
                }
                textFieldProgramStartTime.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickTimePicker(textFieldProgramStartTime, 0, TIME_TITLE, TIME_DESC)
                }
                textFieldProgramEndDate.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickDatePicker(textFieldProgramEndDate, 1, DATE_TITLE_END, DATE_DESC_END)
                }
                textFieldProgramEndTime.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickTimePicker(textFieldProgramEndTime, 1, TIME_TITLE_END, TIME_DESC_END)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProgramCouponDate(){

        val startDate = GregorianCalendar(locale)
        val sds = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
        startDate.time = sds.parse(manualStartTimeProgram + "00") ?: Date()
        val dayStart = startDate.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekStart = getDayOfWeekID(dayStart)

        textFieldProgramStartDate.editText.setText("$dayOfWeekStart, ${setDatePreview(manualStartTimeProgram)}")
        textFieldProgramStartTime.editText.setText(setTime(manualStartTimeProgram))

        val endDate = GregorianCalendar(locale)
        val sde = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
        endDate.time = sde.parse(manualEndTimeProgram + "00") ?: Date()
        val dayEnd = endDate.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekEnd = getDayOfWeekID(dayEnd)

        textFieldProgramEndDate.editText.setText("$dayOfWeekEnd, ${setDatePreview(manualEndTimeProgram)}")
        textFieldProgramEndTime.editText.setText(setTime(manualEndTimeProgram))
        couponStartDate = manualStartTimeProgram.substringBefore(" ")
        couponStartTime = setTime(manualStartTimeProgram).replace(" WIB", "")
        couponEndDate = manualEndTimeProgram.substringBefore(" ")
        couponEndTime = setTime(manualEndTimeProgram).replace(" WIB", "")
    }

    private fun clickDatePicker(
        textField: TextFieldUnify2,
        type: Int,
        title: String,
        desc: String,
    ) {
        var date = ""
        var month = ""
        var year = ""
        var day = 0
        var dayInId = ""
        context?.let {
            val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))
            val maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            when(type){
                 0 -> {
                     val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                     minDate.time = sdf.parse(manualStartTimeProgram + "00") ?: Date()
                     currentDate.time = sdf.parse(manualStartTimeProgram + "00") ?: Date()
                     maxDate.time = currentDate.time
                     maxDate.add(Calendar.YEAR, 1)

                     if(updatedStartTimeCoupon.isNotEmpty() && firstTimeStart){
                         currentDate.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()
                     }
                 }

                1 -> {
                    val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                    minDate.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()
                    currentDate.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()
                    maxDate.time = currentDate.time
                    maxDate.add(Calendar.YEAR, 1)

                    if(updatedEndTimeCoupon.isNotEmpty() && firstTimeEnd){
                        currentDate.time = sdf.parse(updatedEndTimeCoupon + "00") ?: Date()
                    }
                }
            }

            val datepickerObject = DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                setTitle(title)
                setInfo(desc)
                setInfoVisible(true)
                datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        selectedTime = selectedCalendar?.time.toString()
                        date = selectedCalendar?.get(Calendar.DATE).toString()
                        day = selectedCalendar?.get(Calendar.DAY_OF_WEEK)?:0
                        month = selectedCalendar?.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            LocaleUtils.getIDLocale()
                        ).toString()
                        year = selectedCalendar?.get(Calendar.YEAR).toString()
                        when (type) {
                            0 -> {
                                couponStartDate = selectedCalendar?.time?.let { dateTime ->
                                    convertDateTime(dateTime)
                                }.toString()

                                firstTimeStart = true
                                updatedStartTimeCoupon = couponStartDate.substring(0, couponStartDate.length-2)
                                couponStartDate = couponStartDate.substringBefore(" ")
                            }
                            1 -> {
                                couponEndDate = selectedCalendar?.time?.let { dateTime ->
                                    convertDateTime(dateTime)
                                }.toString()
                                firstTimeEnd = true
                                updatedEndTimeCoupon = couponEndDate.substring(0, couponEndDate.length-2)
                                couponEndDate = couponEndDate.substringBefore(" ")
                            }
                        }
                        dayInId =  getDayOfWeekID(day)
                        selectedTime = selectedCalendar?.time.toString()
                        dismiss()
                    }
                }
            }
            childFragmentManager.let { fragmentManager ->
                datepickerObject.show(fragmentManager, "")
            }
            datepickerObject.setOnDismissListener {
                if (dayInId.isNotEmpty() && date.isNotEmpty() && month.isNotEmpty() && year.isNotEmpty()) {
                    textField.textInputLayout.editText?.setText(("$dayInId, $date $month $year"))
                }
            }
        }
    }

    private fun clickTimePicker(
        textField: TextFieldUnify2,
        type: Int,
        title: String,
        desc: String
    ) {
        var selectedHour = ""
        var selectedMinute = ""
        context?.let { ctx ->
            val defaultTime = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx))
            when(type){
                0 -> {
                    val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                    defaultTime.time = sdf.parse(manualStartTimeProgram + "00") ?: Date()
                }
                1 -> {
                    val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                    defaultTime.time = sdf.parse(manualEndTimeProgram + "00") ?: Date()
                }
            }
            val maxDate =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, MINUTE_59)
                }
            val minDate =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                }

            if(tmCouponStartTimeUnix != null && type == 0 && firstTimeStart){
                defaultTime.time = tmCouponStartTimeUnix?.time
            }
            if(tmCouponEndTimeUnix != null && type == 1 && firstTimeEnd){
                defaultTime.time = tmCouponEndTimeUnix?.time
            }

            val timerPickerUnify = DateTimePickerUnify(
                context = ctx,
                minDate = minDate,
                defaultDate = defaultTime,
                maxDate = maxDate,
                type = DateTimePickerUnify.TYPE_TIMEPICKER
            ).apply {
                setTitle(title)
                setInfo(desc)
                setInfoVisible(true)
                minuteInterval = 30
                datePickerButton.setOnClickListener {
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue

                    when (type) {
                        0 -> {
                            startTime = getDate()
                            firstTimeStart = true
                            couponStartTime = "$selectedHour:$selectedMinute"
                            tmCouponStartTimeUnix = startTime
                            val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                            tmCouponStartTimeUnix?.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()
                            tmCouponStartTimeUnix?.set(Calendar.HOUR_OF_DAY, selectedHour.toIntOrZero())
                            tmCouponStartTimeUnix?.set(Calendar.MINUTE, selectedMinute.toIntOrZero())
                            updatedStartTimeCoupon = tmCouponStartTimeUnix?.time?.let { dateTime ->
                                convertDateTime(dateTime)
                            }.toString()
                        }
                        1 -> {
                            startTime = getDate()
                            firstTimeEnd = true
                            couponEndTime = "$selectedHour:$selectedMinute"
                            tmCouponEndTimeUnix = startTime
                            val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                            tmCouponEndTimeUnix?.time = sdf.parse(updatedEndTimeCoupon + "00") ?: Date()
                            tmCouponEndTimeUnix?.set(Calendar.HOUR_OF_DAY, selectedHour.toIntOrZero())
                            tmCouponEndTimeUnix?.set(Calendar.MINUTE, selectedMinute.toIntOrZero())
                            updatedEndTimeCoupon = tmCouponEndTimeUnix?.time?.let { dateTime ->
                                convertDateTime(dateTime)
                            }.toString()
                        }
                    }
                    dismiss()
                }
            }

            childFragmentManager.let {
                timerPickerUnify.show(it, "")
            }
            timerPickerUnify.setOnDismissListener {
                if (selectedHour.isNotEmpty() && selectedMinute.isNotEmpty()) {
                    textField.textInputLayout.editText?.setText(("$selectedHour : $selectedMinute WIB"))
                }
            }
        }
    }

    companion object {
        const val DATA = 1
        const val SHIMMER = 0
        const val ERROR = 2
        const val CODE_SUCCESS= "200"
        const val CODE_PROGRAM_OUTSIDE  = "42039"
        const val CODE_PROGRAM_OUTSIDE_V2  = "42049"

        fun newInstance(bundle: Bundle): TmMultipleCuponCreateFragment {
            return TmMultipleCuponCreateFragment().apply {
                arguments = bundle
            }
        }
    }
}
