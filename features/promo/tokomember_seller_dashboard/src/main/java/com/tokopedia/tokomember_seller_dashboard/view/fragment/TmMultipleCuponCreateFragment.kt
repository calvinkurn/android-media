package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTime
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTimeRemoveTimeDiff
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getDayOfWeekID
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getTimeInMillis
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDatePreview
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setTimeStart
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
        tmDashCreateViewModel.getInitialCouponData(CREATE, "")
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
                                "Silakan Masukkan masukan yang benar",
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
                            tmDashCreateViewModel.validateProgram(
                                arguments?.getInt(
                                    BUNDLE_SHOP_ID
                                ).toString(),
                                getTimeInMillis(updatedStartTimeCoupon),
                                getTimeInMillis(updatedEndTimeCoupon),
                                source
                            )
                        } else {
                            closeLoadingDialog()
                            view?.let { v ->
                                Toaster.build(
                                    v,
                                    "Silakan Masukkan masukan yang benar",
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
                    }
                }
            })

        tmDashCreateViewModel.tmProgramValidateLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipValidateBenefit?.resultStatus?.code == "200") {
                        errorState.isValidateCouponError = false
                        uploadImageVip()
                    } else {
                        closeLoadingDialog()
                        setButtonState()
                        handleProgramPreValidateError(it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(0), it.data?.membershipValidateBenefit?.resultStatus?.message?.getOrNull(1))
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    errorState.isValidateCouponError = false
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateServerError()
                }
                else -> {
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
                    setButtonState()
                    closeLoadingDialog()
                    view?.let { v ->
                        Toaster.build(
                            v,
                            RETRY,
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            ).show()
                    }
                }
                else -> {
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
                else -> {
                }
            }
        })
    }

    private fun openLoadingDialog() {
        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(TM_SUMMARY_DIALOG_TITLE))
        loaderDialog?.show()
    }

    private fun closeLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    private fun handleDataError(){
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {
            tmDashCreateViewModel.getInitialCouponData(CREATE, "")
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

    private fun handleProgramPreValidateError(reason: String?, message: String?) {
        setButtonState()
        val bundle = Bundle()
        val title = if(reason.isNullOrEmpty()) {
            PROGRAM_VALIDATION_ERROR_TITLE
        }
        else{
            reason
        }
        val desc = if(message.isNullOrEmpty()) {
            PROGRAM_VALIDATION_ERROR_DESC
        }
        else{
            message
        }
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            desc,
            TM_ERROR_PROGRAM,
            PROGRAM_VALIDATION_CTA_TEXT
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

    private fun handleProgramValidateServerError() {

        val title = when (retryCount) {
            0 -> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when (retryCount) {
            0 -> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            ERROR_CREATING_DESC,
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
                    0 -> tmDashCreateViewModel.validateProgram(
                        arguments?.getInt(
                            BUNDLE_SHOP_ID
                        ).toString(),
                        getTimeInMillis(updatedStartTimeCoupon),
                        getTimeInMillis(updatedEndTimeCoupon),
                        SOURCE_MULTIPLE_COUPON_CREATE
                    )
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
        tmOpenFragmentCallback.openFragment(CreateScreenType.PREVIEW, bundle)
        closeLoadingDialog()
        setButtonState()
    }

    private fun setProgramStartEndDate(timeWindow: TimeWindow?) {
        when (programActionType) {
            ProgramActionType.EXTEND -> {
                val startDate = GregorianCalendar(locale)
                startDate.add(Calendar.HOUR, 4)
                val minuteCurrent = startDate.get(Calendar.MINUTE)
                startDate.set(Calendar.MINUTE, 30)
                startDate.set(Calendar.SECOND, 0)
                if (minuteCurrent > 30) {
                    startDate.add(Calendar.MINUTE, 30)
                }
                manualStartTimeProgram = convertDateTimeRemoveTimeDiff(startDate.time)
                manualEndTimeProgram = TmDateUtil.addDuration(timeWindow?.endTime ?: "", periodMonth)

                val maxProgramEndDate = GregorianCalendar(locale)
                maxProgramEndDate.add(Calendar.YEAR, 1)

                val endDate = GregorianCalendar(locale)
                val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                endDate.time = sdf.parse(manualEndTimeProgram + "00") ?: Date()

                manualEndTimeProgram = convertDateTimeRemoveTimeDiff(endDate.time)
            }
            else -> {
                val currentDate = GregorianCalendar(locale)
                val currentTime = currentDate.get(Calendar.HOUR_OF_DAY)
                if(currentTime>=20) {
                    val currentStartDate = GregorianCalendar(locale)
                    val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                    currentStartDate.time = sdf.parse(timeWindow?.startTime ?: "" + "00") ?: Date()
                    currentStartDate.set(Calendar.HOUR,currentTime)
                    currentStartDate.set(Calendar.HOUR,0)
                    currentStartDate.set(Calendar.HOUR,0)
                    currentStartDate.add(Calendar.HOUR,4)
                    manualStartTimeProgram = convertDateTimeRemoveTimeDiff(currentStartDate.time)
                }else {
                    manualStartTimeProgram = timeWindow?.startTime ?: ""
                }
                manualEndTimeProgram = timeWindow?.endTime ?: ""
            }
        }
        updatedStartTimeCoupon = manualStartTimeProgram
        updatedEndTimeCoupon = manualEndTimeProgram
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
                setValue(80, false)
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
                        setValue(67, false)
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

        tvTermsAndCondition.text = ss
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        btnContinue.setOnClickListener {
            if(arguments?.getInt(BUNDLE_CREATE_SCREEN_TYPE) == CreateScreenType.COUPON_MULTIPLE_BUAT) {
                tmTracker?.clickCouponCreationFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }
            else {
                tmTracker?.clickCouponCreationButton(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }
            it.isEnabled = false
            it.isClickable = false
            couponPremiumData = tmPremiumCoupon?.getSingleCouponData()
            couponVip = tmVipCoupon?.getSingleCouponData()
            preValidateCouponPremium(couponPremiumData)
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
        tmDashCreateViewModel.preValidateMultipleCoupon(validationRequest)
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
        tmDashCreateViewModel.preValidateCoupon(validationRequest)
    }

    private fun uploadImagePremium() {
        context?.let { ctx ->
            tmVipCoupon?.post {
                val coupon = tmPremiumCoupon.getCouponView()
                val file =  TmFileUtil.saveBitMap(ctx, coupon)
                tmCouponListPremiumItemPreview = TmCouponListItemPreview(
                    file.absolutePath, "Premium", couponPremiumData?.quota ?: "100"
                )
                tmDashCreateViewModel.uploadImagePremium(file)
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
                tmDashCreateViewModel.uploadImageVip(file)
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
                icArrowPremium?.animate()?.rotation(180f)?.duration = 100L
                expand(tmPremiumCoupon)
                !isCollapsedPremium
            } else{
                icArrowPremium?.animate()?.rotation(0f)?.duration = 100L
                collapse(tmPremiumCoupon)
                !isCollapsedPremium
            }
        }

        icArrowVip.setOnClickListener {
            isCollapsedVip = if (isCollapsedVip) {
                icArrowVip?.animate()?.rotation(180f)?.duration = 100L
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
            clickDatePicker(textFieldProgramStartDate, 0 , DATE_TITLE , DATE_DESC,manualStartTimeProgram)
        }

        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramStartTime, 0 , TIME_TITLE, TIME_DESC)
        }

        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramEndDate, 1, DATE_TITLE_END, DATE_DESC_END,manualEndTimeProgram)
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
                        clickDatePicker(textFieldProgramStartDate, 0, DATE_TITLE , DATE_DESC,manualStartTimeProgram)
                }
                textFieldProgramStartTime.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickTimePicker(textFieldProgramStartTime, 0, TIME_TITLE, TIME_DESC)
                }
                textFieldProgramEndDate.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                        clickDatePicker(textFieldProgramEndDate, 1, DATE_TITLE_END, DATE_DESC_END,manualEndTimeProgram)
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
        textFieldProgramStartTime.editText.setText(setTimeStart(manualStartTimeProgram)+" WIB")

        val endDate = GregorianCalendar(locale)
        val sde = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
        endDate.time = sde.parse(manualEndTimeProgram + "00") ?: Date()
        val dayEnd = endDate.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekEnd = getDayOfWeekID(dayEnd)

        textFieldProgramEndDate.editText.setText("$dayOfWeekEnd, ${setDatePreview(manualEndTimeProgram)}")
        textFieldProgramEndTime.editText.setText(setTimeStart(manualEndTimeProgram)+" WIB")
        couponStartDate = manualStartTimeProgram.substringBefore(" ")
        couponStartTime = setTimeStart(manualStartTimeProgram)
        couponEndDate = manualEndTimeProgram.substringBefore(" ")
        couponEndTime = setTimeStart(manualEndTimeProgram)
    }

    private fun clickDatePicker(
        textField: TextFieldUnify2,
        type: Int,
        title: String,
        desc: String,
        programDate:String
    ) {
        var date = ""
        var month = ""
        var year = ""
        var day = 0
        var dayInId = ""
        context?.let {
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))
            val maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            when(type){
                 0 -> {
                     val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                     currentDate.time = sdf.parse(manualStartTimeProgram + "00") ?: Date()
                     maxDate.time = currentDate.time
                     maxDate.add(Calendar.YEAR, 1)
                 }

                1 -> {
                    val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
                    currentDate.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()
                    maxDate.time = currentDate.time
                    maxDate.add(Calendar.YEAR, 1)
                }
            }

            val datepickerObject = DateTimePickerUnify(it, currentDate, currentDate, maxDate).apply {
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
                                updatedStartTimeCoupon = couponStartDate.substring(0, couponStartDate.length-2)
                                couponStartDate = couponStartDate.substringBefore(" ")
                            }
                            1 -> {
                                couponEndDate = selectedCalendar?.time?.let { dateTime ->
                                    convertDateTime(dateTime)
                                }.toString()
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
            val currentDate =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx))
            val maxDate =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                }

            val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
            currentDate.time = sdf.parse(updatedStartTimeCoupon + "00") ?: Date()

            val timerPickerUnify = DateTimePickerUnify(
                context = ctx,
                minDate = currentDate,
                defaultDate = currentDate,
                maxDate = maxDate,
                type = DateTimePickerUnify.TYPE_TIMEPICKER
            ).apply {
                minuteInterval = 30
                setTitle(title)
                setInfo(desc)
                setInfoVisible(true)
                minuteInterval = 30
                datePickerButton.setOnClickListener {
                    startTime = getDate()
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue

                    currentDate.set(Calendar.HOUR,selectedHour.toIntSafely())
                    currentDate.set(Calendar.MINUTE,selectedMinute.toIntSafely())
                    currentDate.set(Calendar.SECOND,0)

                    when (type) {
                        0 -> {
                            couponStartTime = "$selectedHour:$selectedMinute"
                            updatedStartTimeCoupon = startTime?.time?.let { dateTime ->
                                convertDateTime(dateTime)
                            }.toString()
                        }
                        1 -> {
                            couponEndTime = "$selectedHour:$selectedMinute"
                            updatedEndTimeCoupon = startTime?.time?.let { dateTime ->
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

        fun newInstance(bundle: Bundle): TmMultipleCuponCreateFragment {
            return TmMultipleCuponCreateFragment().apply {
                arguments = bundle
            }
        }
    }
}