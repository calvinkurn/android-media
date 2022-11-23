package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CouponType
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_common_widget.util.ProgramDateType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponUpdateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailData
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.model.ValidationError
import com.tokopedia.tokomember_seller_dashboard.model.mapper.TmCouponCreateMapper
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DUPLICATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE_OLDER
import com.tokopedia.tokomember_seller_dashboard.util.ANDROID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_IDR
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_PERCENTAGE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_CASHBACK_PREVIEW
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_DISCOUNT_TYPE_IDR
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_DISCOUNT_TYPE_PERCENT
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_TITLE_SINGLE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_TITLE_SINGLE_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_MEMBER
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_SHIPPING_PREVIEW
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TERMS_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TYPE_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TYPE_SHIPPING
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_VIP
import com.tokopedia.tokomember_seller_dashboard.util.CREATE
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC_END
import com.tokopedia.tokomember_seller_dashboard.util.DATE_FORMAT
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE_END
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_NO_INTERNET
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ErrorState
import com.tokopedia.tokomember_seller_dashboard.util.MAX_CASHBACK_LABEL
import com.tokopedia.tokomember_seller_dashboard.util.MAX_GRATIS_LABEL
import com.tokopedia.tokomember_seller_dashboard.util.PREMIUM
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_VALIDATION_CTA_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.SIMPLE_DATE_FORMAT
import com.tokopedia.tokomember_seller_dashboard.util.TERMS
import com.tokopedia.tokomember_seller_dashboard.util.TERNS_AND_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.TIME_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TIME_DESC_END
import com.tokopedia.tokomember_seller_dashboard.util.TIME_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TIME_TITLE_END
import com.tokopedia.tokomember_seller_dashboard.util.TM_ERROR_GEN
import com.tokopedia.tokomember_seller_dashboard.util.TM_ERROR_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUMMARY_DIALOG_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_SUMMARY_DIALOG_TITLE_START_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.TM_TNC
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.getDayOfWeekID
import com.tokopedia.tokomember_seller_dashboard.util.TmFileUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmInternetCheck
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.util.UPDATE
import com.tokopedia.tokomember_seller_dashboard.util.WAITING
import com.tokopedia.tokomember_seller_dashboard.view.activity.TmDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmSingleCouponView
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmEligibilityViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_program_fragment.*
import kotlinx.android.synthetic.main.tm_dash_single_coupon.*
import kotlinx.android.synthetic.main.tm_dash_single_coupon.view.*
import kotlinx.android.synthetic.main.tm_kupon_create_single.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val MINUTE_30 = 30
private const val HOUR_4 = 4
private const val RESULT_SUCCESS = 200

class TmSingleCouponCreateFragment : BaseDaggerFragment() {
    private val CALENDAR_TYPE_START = 0
    private val CALENDAR_TYPE_END = 1
    private var programStatus: Int = ACTIVE
    private var token: String? = ""
    private var tmCouponDetail: TmCouponDetailData? = null
    private var selectedChipPositionDate: Int = 0
    private var selectedChipPositionLevel: Int = 0
    private var selectedChipPositionKupon: Int = 0
    private var selectedChipPositionCashback: Int = 0
    private var selectedCalendar: Calendar? = null
    private var tmCouponStartDateUnix : Calendar? = null
    private var tmCouponEndDateUnix : Calendar? = null
    private var tmCouponStartTimeUnix : Calendar? = null
    private var tmCouponEndTimeUnix : Calendar? = null
    private var selectedTime = ""
    private var startTime : Calendar? = null
    private var programData: ProgramUpdateDataInput? = null
    private var couponPremiumData :TmSingleCouponData? = null
    private var couponVip : TmSingleCouponData? = null
    private var shopName=""
    private var shopAvatar=""
    private var retryCount = 0
    private val errorState = ErrorState()
    private var loaderDialog: LoaderDialog?=null
    private var fromEdit = false
    private var firstTimeStart = false
    private var firstDateStart = false
    private var firstTimeEnd = false
    private var firstDateEnd = false
    private var fromDuplicate = false
    private var voucherId = 0
    private var errorCount = 0
    private var prefManager: TmPrefManager? = null
    private var tmTracker: TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TmDashCreateViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }
    private val tmProgramListViewModel: TmProgramListViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = activity?.let { ViewModelProvider(it, viewModelFactory.get()) }
        viewModelProvider?.get(TmProgramListViewModel::class.java)
    }

    private val tmEligibilityViewModel: TmEligibilityViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmEligibilityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_single_kupon_cotainer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmEligibilityViewModel.getSellerInfo()
        shopName = arguments?.getString(BUNDLE_SHOP_NAME)?:""
        shopAvatar = arguments?.getString(BUNDLE_SHOP_AVATAR)?:""
        fromEdit = arguments?.getBoolean(ACTION_EDIT)?:false
        fromDuplicate = arguments?.getBoolean(ACTION_DUPLICATE)?:false
        programData = arguments?.getParcelable(BUNDLE_PROGRAM_DATA)
        prefManager = context?.let { it1 -> TmPrefManager(it1) }
        tmTracker = TmTracker()

        renderHeader()
        renderButton()
        observeDataFromApi()

        voucherId = arguments?.getInt(BUNDLE_VOUCHER_ID)?:0
        if(fromEdit){
            if (TmInternetCheck.isConnectedToInternet(context)) {
                tokomemberDashCreateViewModel.getInitialCouponData(UPDATE, "")
            }
            else{
                noInternetUi { tokomemberDashCreateViewModel.getInitialCouponData(UPDATE, "") }
            }
        }
        else if(fromDuplicate){
            if (TmInternetCheck.isConnectedToInternet(context)) {
                tokomemberDashCreateViewModel.getInitialCouponData(CREATE, "")
            }
            else{
                noInternetUi{tokomemberDashCreateViewModel.getInitialCouponData(CREATE, "")}
            }
        }
        else{
            if (TmInternetCheck.isConnectedToInternet(context)) {
                tokomemberDashCreateViewModel.getInitialCouponData(CREATE, "")
                renderSingleCoupon()
            }
            else{
                noInternetUi{tokomemberDashCreateViewModel.getInitialCouponData(CREATE, "")}
            }
            prefManager?.shopId?.let { it ->
                prefManager?.cardId?.let { it1 ->
                    if (TmInternetCheck.isConnectedToInternet(context)) {
                        tmProgramListViewModel?.getProgramList(it, it1)
                    }
                    else{
                        noInternetUi{tmProgramListViewModel?.getProgramList(it, it1)}
                    }
                }
            }
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
        bottomsheet.show(childFragmentManager,"")
        setButtonState()
    }

    private fun getCouponDetails() {
        if(TmInternetCheck.isConnectedToInternet(context)) {
            tokomemberDashCreateViewModel.getCouponDetail(voucherId)
        }
        else{
            noInternetUi { tokomemberDashCreateViewModel.getCouponDetail(voucherId) }
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val dagger = DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
        dagger.inject(this)
    }

    private fun observeDataFromApi() {


        tmEligibilityViewModel.sellerInfoResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    shopAvatar = it.data.userShopInfo?.info?.shopAvatar?:""
                    shopName = it.data.userShopInfo?.info?.shopName?:""
                    customViewSingleCoupon.setShopData(shopName, shopAvatar)
                }
                is Fail -> {
                }
            }
        })

        tmProgramListViewModel?.tokomemberProgramListResultLiveData?.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.LOADING ->{
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if(!it.data?.membershipGetProgramList?.programSellerList.isNullOrEmpty()){
                        var startTime: String? = ""
                        var endTime: String? = ""
                        run time@ {
                            it.data?.membershipGetProgramList?.programSellerList?.forEach { item ->
                                if (item?.status == ACTIVE || item?.status == ACTIVE_OLDER) {
                                    //check if starttime < current time
                                    if (TmDateUtil.getTimeInMillis(item.timeWindow?.startTime, "yyyy-MM-dd HH:mm:ss").toLong() < DateUtil.getCurrentDate().time.div(1000)) {
                                        startTime = item.timeWindow?.startTime
                                        endTime = item.timeWindow?.endTime
                                        programData = ProgramUpdateDataInput()
                                        programData?.apply {
                                            timeWindow = TimeWindow(startTime = TmDateUtil.setTimeStartSingle(startTime), endTime = endTime)
                                        }
                                        programStatus = ACTIVE
                                        return@time
                                    }
                                }
                            }
                        }
                        val wait = it.data?.membershipGetProgramList?.programSellerList?.count {
                            it?.status == WAITING
                        }
                        val active = it.data?.membershipGetProgramList?.programSellerList?.count {
                            it?.status == ACTIVE
                        }
                        val active_older = it.data?.membershipGetProgramList?.programSellerList?.count {
                            it?.status == ACTIVE_OLDER
                        }
                        if(wait != 0 && (active_older?.let { it1 -> active?.plus(it1) }) == 0){
                            programStatus = WAITING
                            startTime = it.data.membershipGetProgramList.programSellerList.firstOrNull()?.timeWindow?.startTime
                            endTime = it.data.membershipGetProgramList.programSellerList.firstOrNull()?.timeWindow?.endTime
                            programData = ProgramUpdateDataInput()
                            programData?.apply {
                                timeWindow = TimeWindow(startTime = TmDateUtil.setTimeStartSingle(startTime), endTime = endTime)
                            }
                        }
                        if(startTime.isNullOrEmpty()) {
                                startTime =
                                    it.data?.membershipGetProgramList?.programSellerList?.firstOrNull()?.timeWindow?.startTime
                                endTime =
                                    it.data?.membershipGetProgramList?.programSellerList?.firstOrNull()?.timeWindow?.endTime
                                programData = ProgramUpdateDataInput()
                                programData?.apply {
                                    timeWindow = TimeWindow(startTime = startTime, endTime = endTime)
                                }
                        }
                        if(!fromEdit) {
                            renderProgram()
                        }
                    }
                    else{
                        view?.let { it1 -> Toaster.build(it1, "No program found", Toaster.TYPE_ERROR, Toaster.LENGTH_LONG).show() }
                        Toast.makeText(context, "No program found", Toast.LENGTH_LONG).show()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                }
            }
        })

        tokomemberDashCreateViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{
                    containerViewFlipper.displayedChild = 2
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    containerViewFlipper.displayedChild = 0
                    token = it.data?.getInitiateVoucherPage?.data?.token
                    tmCouponDetail = TmCouponDetailData()
                    tmCouponDetail?.apply {
                        voucherImagePortrait = it.data?.getInitiateVoucherPage?.data?.imgBannerIgPost
                        voucherImageSquare = it.data?.getInitiateVoucherPage?.data?.imgBannerIgStory
                    }
                    if(fromEdit || fromDuplicate){
                        getCouponDetails()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    containerViewFlipper.displayedChild = 1
                }
            }
        })

        tokomemberDashCreateViewModel.tmCouponPreValidateSingleCouponLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{
                    openLoadingDialog()
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    errorState.isPreValidateVipError = false
                    if (it.data?.voucherValidationPartial?.header?.messages?.size == 0){
                        val startTime = if(tmCouponStartTimeUnix != null){
                            tmCouponStartTimeUnix?.timeInMillis?.div(1000).toString()
                        } else{
                            TmDateUtil.getTimeMillisForCouponValidate(programData?.timeWindow?.startTime.toString())
                        }
                        val endTime = if(tmCouponEndTimeUnix != null){
                            tmCouponEndTimeUnix?.timeInMillis?.div(1000).toString()
                        } else{
                            TmDateUtil.getTimeMillisForCouponValidateEnd(programData?.timeWindow?.endTime.toString())
                        }
                        if(TmInternetCheck.isConnectedToInternet(context)) {
                            tokomemberDashCreateViewModel.validateProgram(
                                prefManager?.shopId.toString(),
                                startTime,
                                endTime,
                                ""
                            )
                        }
                        else{
                            noInternetUi {
                                tokomemberDashCreateViewModel.validateProgram(
                                    prefManager?.shopId.toString(),
                                    startTime,
                                    endTime,
                                    ""
                                )
                            }
                        }
                    }
                    else {
                        closeLoadingDialog()
                        handlePreValidateCouponError(it.data?.voucherValidationPartial?.data?.validationError, PREMIUM)
                        setButtonState()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR  -> {
                    setButtonState()
                    errorState.isPreValidateVipError = true
                    closeLoadingDialog()
                    handleProgramValidateNetworkError()
                }
            }
        })

        tokomemberDashCreateViewModel.tmProgramValidateLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipValidateBenefit?.resultStatus?.code == "200") {
                        errorState.isValidateCouponError = false
//                        updateCoupon()
                        uploadImagePremium()
                    } else {
                        closeLoadingDialog()
                        setButtonState()
                        if (it.data?.membershipValidateBenefit?.resultStatus?.code == "42050") {
                            //No Active program available
                            handleProgramValidateError(
                                it.data.membershipValidateBenefit.resultStatus.message?.getOrNull(0),
                                it?.data.membershipValidateBenefit.resultStatus.message?.getOrNull(1),
                                true
                            )
                        }
                        else if (it.data?.membershipValidateBenefit?.resultStatus?.code == "42049") {
                            //Coupon active date outside program time window
                            handleProgramValidateError(
                                it.data.membershipValidateBenefit.resultStatus.message?.getOrNull(0),
                                it?.data.membershipValidateBenefit.resultStatus.message?.getOrNull(1),
                                false
                            )
                        }
                        else if (it.data?.membershipValidateBenefit?.resultStatus?.code == "42039") {
                            handleProgramValidateError(
                                it.data.membershipValidateBenefit.resultStatus.message?.getOrNull(0),
                                it?.data.membershipValidateBenefit.resultStatus.message?.getOrNull(1),
                                false
                            )
                        }
                        else{
                            handleProgramValidateError("",
                                "",
                                false,
                                true
                            )
                        }

                    }
                }
                TokoLiveDataResult.STATUS.ERROR-> {
                    errorState.isValidateCouponError = false
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateNetworkError()
//                    handleProgramValidateServerError()
                }
                else->{}
            }
        })

        tokomemberDashCreateViewModel.tmCouponUploadMultipleLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    when(it.data){
                        is UploadResult.Success ->{
                            if(fromEdit) {
                                updateCoupon(it.data.uploadId)
                            }
                            if(!fromEdit || fromDuplicate){
                                if (token != null && tmCouponDetail?.voucherImagePortrait != null && tmCouponDetail?.voucherImageSquare != null && couponPremiumData?.maxCashback != null) {

                                    val timeEnd = if(tmCouponEndTimeUnix != null){
                                        TmDateUtil.getTimeFromUnix(
                                            tmCouponEndTimeUnix!!
                                        )
                                    }
                                    else{
                                        TmDateUtil.setTime(
                                            programData?.timeWindow?.endTime.toString()
                                        ).replace(" WIB", "")
                                    }
                                    val dateEnd = if(tmCouponEndDateUnix != null){
                                        TmDateUtil.getDateFromUnix(
                                            tmCouponEndDateUnix!!
                                        )
                                    }
                                    else{
                                        programData?.timeWindow?.endTime?.substringBefore("T")
                                    }
                                    val timeStart = if(tmCouponStartTimeUnix != null){
                                        TmDateUtil.getTimeFromUnix(
                                            tmCouponStartTimeUnix!!
                                        )
                                    }
                                    else{
                                        TmDateUtil.setTime(
                                            programData?.timeWindow?.startTime.toString()
                                        ).replace(" WIB", "")
                                    }
                                    val dateStart = if(tmCouponStartDateUnix != null){
                                        TmDateUtil.getDateFromUnix(
                                            tmCouponStartDateUnix!!
                                        )
                                    }
                                    else{
                                        programData?.timeWindow?.startTime?.substringBefore("T")
                                    }

                                    val tmMerchantCouponCreateData =

                                        TmCouponCreateMapper.mapCreateDataSingle(
                                            couponPremiumData = couponPremiumData,
                                            tmCouponPremiumUploadId = it.data.uploadId,
                                            startDate = dateStart,
                                            endDate = dateEnd,
                                            startTime  = timeStart,
                                            endTime = timeEnd,
                                            token = token!!,
                                            imagePortrait = tmCouponDetail?.voucherImagePortrait!!,
                                            imageSquare = tmCouponDetail?.voucherImageSquare!!,
                                            maximumBenefit = couponPremiumData?.maxCashback.toIntSafely(),
                                            tierLevel = selectedChipPositionLevel+1
                                        )
                                    if(TmInternetCheck.isConnectedToInternet(context)) {
                                        tokomemberDashCreateViewModel.createSingleCoupon(
                                            tmMerchantCouponCreateData
                                        )
                                    }
                                    else{
                                        noInternetUi { tokomemberDashCreateViewModel.createSingleCoupon(
                                            tmMerchantCouponCreateData
                                        ) }
                                    }
                                }
                            }
                        }
                        is UploadResult.Error ->{
                            closeLoadingDialog()
                            view?.let { it ->
                                Toaster.build(
                                    it,
                                    RETRY,
                                    Toaster.TYPE_ERROR,
                                    Toaster.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                TokoLiveDataResult.STATUS.ERROR-> {
                    errorState.isUploadPremium = true
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateNetworkError()
                }
                else->{}
            }
        })

        tokomemberDashCreateViewModel.tmCouponDetaillLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    tmCouponDetail = it.data?.merchantPromotionGetMVDataByID?.data
                    renderUIForEdit(it.data?.merchantPromotionGetMVDataByID?.data)
                    renderProgram()
                    renderSingleCoupon()
                    closeLoadingDialog()
                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    closeLoadingDialog()
                    //open error bottom sheet
                    val tmModel = TmIntroBottomsheetModel("", "", "", "", "", errorCount)
                    val bundle = Bundle()
                    bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmModel))
                    val bs = TokomemberBottomsheet.createInstance(bundle)
                    bs.show(childFragmentManager, "")
                    bs.setUpBottomSheetListener(object : BottomSheetClickListener{
                        override fun onButtonClick(errorCount: Int) {
                            this@TmSingleCouponCreateFragment.errorCount = errorCount
                        }
                    })
                }
            }
        })

        tokomemberDashCreateViewModel.tmCouponUpdateLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if(it.data?.merchantPromotionCreateMV?.status == RESULT_SUCCESS) {
                        // Back to coupon list
                        closeLoadingDialog()
                        setButtonState()
                        activity?.finish()
                        tmCouponListRefreshCallback?.refreshCouponList(ACTION_EDIT)
                    }
                    else{
                        closeLoadingDialog()
                        setButtonState()
                        view?.let { it1 -> it.data?.merchantPromotionCreateMV?.message?.let { it2 ->
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR ).show()
                        } }
                    }
                }
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    //handleError
                    handleProgramValidateNetworkError()
                    closeLoadingDialog()
                    setButtonState()
                }
            }
        })

        tokomemberDashCreateViewModel.tmSingleCouponCreateLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    if(it.data.merchantPromotionCreateMV?.status == RESULT_SUCCESS) {
                        //Open Dashboard
                        closeLoadingDialog()
                        if(fromDuplicate){
                            activity?.finish()
                            tmCouponListRefreshCallback?.refreshCouponList(ACTION_DUPLICATE)
                        }
                        else{
                            activity?.finish()
                            tmCouponListRefreshCallback?.refreshCouponList(ACTION_CREATE)
                        }
                    }
                    else{
                        closeLoadingDialog()
                        setButtonState()
                        view?.let { it1 -> it.data.merchantPromotionCreateMV?.message?.let { it2 ->
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_LONG , Toaster.TYPE_ERROR ).show()
                        } }
                    }
                }
                is Fail -> {
                    //handleError
                    closeLoadingDialog()
                    setButtonState()
                    view?.let { it1 -> it.throwable.message.let { it2 ->
                        if (it2 != null) {
                            Toaster.build(it1,
                                it2, Toaster.LENGTH_LONG , Toaster.TYPE_ERROR ).show()
                        }
                    } }
                }
            }
        })
    }

    private fun updateCoupon(uploadId: String) {
        val tmSingleCouponData = customViewSingleCoupon.getSingleCouponData()
        errorState.isUploadPremium = false
        //Handle success
        val level = if(selectedChipPositionLevel == 0){
            1
        }
        else{
            2
        }
        val benefitType = if(selectedChipPositionCashback == 0){
            "idr"
        }
        else{
            "percent"
        }
        val couponType = if(selectedChipPositionKupon == 0){
            "cashback"
        }
        else{
            "shipping"
        }
        val hourEnd = if(tmCouponEndTimeUnix != null){
            TmDateUtil.getTimeFromUnix(
                tmCouponEndTimeUnix!!
            )
        }
        else{
            TmDateUtil.setTime(
                programData?.timeWindow?.endTime.toString()
            ).replace(" WIB", "")
        }
        val dateEnd = if(tmCouponEndDateUnix != null){
            TmDateUtil.getDateFromUnix(
                tmCouponEndDateUnix!!
            )
        }
        else{
            programData?.timeWindow?.endTime?.substringBefore("T")
        }
        val hourStart = if(tmCouponStartTimeUnix != null){
            TmDateUtil.getTimeFromUnix(
                tmCouponStartTimeUnix!!
            )
        }
        else{
            TmDateUtil.setTime(
                programData?.timeWindow?.startTime.toString()
            ).replace(" WIB", "")
        }
        val dateStart = if(tmCouponStartDateUnix != null){
            TmDateUtil.getDateFromUnix(
                tmCouponStartDateUnix!!
            )
        }
        else{
            programData?.timeWindow?.startTime?.substringBefore("T")
        }
        val tmCouponUpdateRequest = TmCouponUpdateRequest(
            benefitMax = tmCouponDetail?.voucherDiscountAmtMax,
            targetBuyer = 3,
            image = uploadId,
            code = "",
            minPurchase = tmCouponDetail?.voucherMinimumAmt,
            minimumTierLevel = level,
            benefitPercent = tmSingleCouponData.cashBackPercentage,
            hourEnd = hourEnd,
            dateEnd = dateEnd,
            hourStart = hourStart,
            dateStart = dateStart,
            source = "android",
            imageSquare = tmCouponDetail?.voucherImageSquare,
            couponName = tmCouponDetail?.voucherName,
            token = token,
            benefitType = benefitType,
            benefitIdr = tmCouponDetail?.voucherDiscountAmtMax,
            imagePortrait = tmCouponDetail?.voucherImagePortrait,
            quota = tmSingleCouponData.quota.toIntSafely(),
            isPublic = tmCouponDetail?.isPublic,
            voucherId = tmCouponDetail?.voucherId,
            couponType = couponType,
            is_lock_to_product = tmCouponDetail?.isLockToProduct,
            product_ids = tmCouponDetail?.productIds.toString().replace("[", "").replace("]", ""),
            warehouse_id = tmCouponDetail?.warehouseId,
            product_ids_csv_url = ""

        )
        if(TmInternetCheck.isConnectedToInternet(context)) {
            tokomemberDashCreateViewModel.updateCoupon(tmCouponUpdateRequest)
        }
        else{
            noInternetUi { tokomemberDashCreateViewModel.updateCoupon(tmCouponUpdateRequest) }
        }
    }

    private fun renderUIForEdit(data: TmCouponDetailData?) {

        chipGroupLevel.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionLevel = position
            }
        })
        if(data?.minimumTierLevel == COUPON_VIP){
            selectedChipPositionLevel = 1
        }
        if(data?.minimumTierLevel == COUPON_MEMBER){
            selectedChipPositionLevel = 0
        }
        chipGroupLevel.setDefaultSelection(selectedChipPositionLevel)

        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
                when(selectedChipPositionKupon){
                    CouponType.CASHBACK -> {
                        textFieldMaxCashback.setLabel(MAX_CASHBACK_LABEL)
                        tvCashbackType.show()
                        chipGroupCashbackType.show()
                        ivPreviewCoupon.showHideCashBackValueView(true)
                        ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
                        if(selectedChipPositionCashback == 0){
                            textFieldPercentCashback.hide()
                        }
                        if(selectedChipPositionCashback == 1){
                            textFieldPercentCashback.show()
                        }
                    }
                    CouponType.SHIPPING -> {
                        textFieldMaxCashback.setLabel(MAX_GRATIS_LABEL)
                        tvCashbackType.hide()
                        chipGroupCashbackType.hide()
                        textFieldPercentCashback.hide()
                        ivPreviewCoupon.showHideCashBackValueView(false)
                        ivPreviewCoupon.setCouponType(COUPON_SHIPPING_PREVIEW)
                    }
                }
            }
        })
        if(data?.voucherType == COUPON_TYPE_CASHBACK){
            chipGroupCashbackType.show()
            ivPreviewCoupon.showHideCashBackValueView(true)
            ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
            selectedChipPositionKupon = 0
        }
        if(data?.voucherType == COUPON_TYPE_SHIPPING){
            chipGroupCashbackType.hide()
            ivPreviewCoupon.showHideCashBackValueView(false)
            ivPreviewCoupon.setCouponType(COUPON_SHIPPING_PREVIEW)
            selectedChipPositionKupon = 1
        }
        chipGroupKuponType.setDefaultSelection(selectedChipPositionKupon)

        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
                if(selectedChipPositionCashback == 0){
                    textFieldPercentCashback.hide()
                }
                if(selectedChipPositionCashback == 1){
                    textFieldPercentCashback.show()
                }
            }
        })

        if(data?.voucherDiscountType == COUPON_DISCOUNT_TYPE_IDR){
            selectedChipPositionCashback = 0
            textFieldPercentCashback.hide()
        }
        if(data?.voucherDiscountType == COUPON_DISCOUNT_TYPE_PERCENT){
            selectedChipPositionCashback = 1
            textFieldPercentCashback.show()
        }
        chipGroupCashbackType.setDefaultSelection(selectedChipPositionCashback)

        textFieldMaxCashback.editText.setText(data?.voucherDiscountAmtMax.toString())
        textFieldMinTransk.editText.setText(data?.voucherMinimumAmt.toString())
        textFieldQuota.editText.setText(data?.voucherQuota.toString())

        ivPreviewCoupon.setCouponValue(data?.voucherDiscountAmtMax.toString())

        ivPreviewCoupon.setInitialData(shopName, shopAvatar)

        textFieldMaxCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    ivPreviewCoupon.setCouponValue(number.toString())
                }
            })
        }
        textFieldPercentCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    ivPreviewCoupon.setCouponBenefit(number.toString())
                }
            })
        }

        try {
            data?.voucherStartTime?.let {
                textFieldProgramStartDate.editText.setText(TmDateUtil.setDateFromDetails(it))
                textFieldProgramStartTime.editText.setText(TmDateUtil.setTimeFromDetails(it))
                tmCouponStartDateUnix = TmDateUtil.getCalendarFromDetailsTime(it)
                tmCouponStartTimeUnix = TmDateUtil.getCalendarFromDetailsTime(it)
            }
            data?.voucherFinishTime?.let{
                textFieldProgramEndDate.editText.setText(TmDateUtil.setDateFromDetails(it))
                textFieldProgramEndTime.editText.setText(TmDateUtil.setTimeFromDetails(it))
                tmCouponEndDateUnix = TmDateUtil.getCalendarFromDetailsTime(it)
                tmCouponEndTimeUnix = TmDateUtil.getCalendarFromDetailsTime(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openLoadingDialog(){
            loaderDialog = context?.let { LoaderDialog(it) }
            loaderDialog?.loaderText?.apply {
                setType(Typography.DISPLAY_2)
            }
//
            val ss = SpannableString(TM_SUMMARY_DIALOG_TITLE)
            ss.setSpan(
                StyleSpan(Typeface.BOLD),
                0, TM_SUMMARY_DIALOG_TITLE_START_TEXT.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            loaderDialog?.setLoadingText(ss)
        loaderDialog?.loaderText?.gravity=Gravity.CENTER_HORIZONTAL

            loaderDialog?.show()
    }

    private fun closeLoadingDialog(){
            loaderDialog?.dialog?.dismiss()
    }

    private fun handleProgramValidateNetworkError(){
        view?.let { Toaster.build(it, RETRY , Toaster.LENGTH_LONG , Toaster.TYPE_ERROR ).show() }
        btnContinue.apply {
            isEnabled = true
            isClickable = true
        }
    }

    private fun handlePreValidateCouponError(validationError: ValidationError?, couponType: String) {
        view?.let { v ->
            Toaster.build(
                v,
                "Cek dan pastikan semua informasi yang kamu isi sudah benar, ya.",
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
        when(couponType){
            PREMIUM -> {
                customViewSingleCoupon?.setErrorMaxBenefit(validationError?.benefitMax?:"")
                customViewSingleCoupon?.setErrorCashbackPercentage(validationError?.benefitPercent?:"")
                customViewSingleCoupon?.setErrorMinTransaction(validationError?.minPurchase?:"")
                customViewSingleCoupon?.setErrorQuota(validationError?.quota?:"")
            }
        }
    }

    private fun setButtonState(){
        btnContinue.apply {
            isClickable = true
            isEnabled = true
        }
    }

    private fun handleProgramValidateError(reason: String?, message: String?, openProgramCreation: Boolean = false, retryFlow: Boolean = false){
        setButtonState()
        val title = if(reason.isNullOrEmpty()) {
            ERROR_CREATING_TITLE
        }
        else{
            reason
        }
        val desc = if(message.isNullOrEmpty()) {
            ERROR_CREATING_DESC
        }
        else{
            message
        }
        var cta = if(openProgramCreation){
            PROGRAM_CTA
        }
        else{
            PROGRAM_VALIDATION_CTA_TEXT
        }
        if(retryCount==1){
            cta = ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(
            title,
            desc ,
            TM_ERROR_PROGRAM,
            cta,
            errorCount = retryCount
        )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomSheetModel))
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                when(errorCount){
                    0 ->{
                        if(openProgramCreation) {
                            val cardId = prefManager?.cardId
                            prefManager?.shopId?.let { shopId ->
                                cardId?.let{
                                    TmDashCreateActivity.openActivity(
                                        shopId,
                                        activity,
                                        CreateScreenType.PROGRAM,
                                        ProgramActionType.CREATE_FROM_COUPON,
                                        null,
                                        null,
                                        cardId = cardId
                                    )
                                    bottomSheet.dismiss()
                                    activity?.finish()
                                }
                            }
                        }
                        else{
                            if(retryFlow){
                                val startTime = if(tmCouponStartTimeUnix != null){
                                    tmCouponStartTimeUnix?.timeInMillis?.div(1000).toString()
                                } else{
                                    TmDateUtil.getTimeMillisForCouponValidate(programData?.timeWindow?.startTime.toString())
                                }
                                val endTime = if(tmCouponEndTimeUnix != null){
                                    tmCouponEndTimeUnix?.timeInMillis?.div(1000).toString()
                                } else{
                                    TmDateUtil.getTimeMillisForCouponValidateEnd(programData?.timeWindow?.endTime.toString())
                                }
                                if(TmInternetCheck.isConnectedToInternet(context)) {
                                    tokomemberDashCreateViewModel.validateProgram(
                                        prefManager?.shopId.toString(),
                                        startTime,
                                        endTime,
                                        ""
                                    )
                                }
                                else{
                                    noInternetUi {  tokomemberDashCreateViewModel.validateProgram(
                                        prefManager?.shopId.toString(),
                                        startTime,
                                        endTime,
                                        ""
                                    ) }
                                }
                            }
                            else {
                                bottomSheet.dismiss()
                            }
                        }
                    }
                    1 ->{
                        activity?.finish()
                    }
                }
            }
        })
        if(retryFlow) {
            retryCount++
        }
        bottomSheet.show(childFragmentManager,"")
    }

    private fun handleProgramValidateServerError(preValidateError: Boolean = false){
        val title = when(retryCount){
            0-> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when(retryCount){
            0-> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(
            title,
            ERROR_CREATING_DESC,
            TM_ERROR_GEN,
            cta,
            errorCount = retryCount)
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                if(preValidateError){
                    btnContinue.isEnabled = false
                    btnContinue.isClickable = false
                    couponPremiumData = customViewSingleCoupon?.getSingleCouponData()
                    preValidateCouponPremium(couponPremiumData)
                }
                else{
                when (errorCount) {
                    0 -> {
                        if(TmInternetCheck.isConnectedToInternet(context)) {
                            tokomemberDashCreateViewModel.validateProgram("", "", "", "")
                        }
                        else{
                            noInternetUi { tokomemberDashCreateViewModel.validateProgram("", "", "", "")}
                        }
                    }
                    else -> {
                        (TokomemberDashIntroActivity.openActivity(
                            0, "", "",
                            false,
                            context
                        ))
                    }
                }
                }
            }})
        bottomsheet.show(childFragmentManager,"")
        retryCount +=1
    }

    private fun renderHeader() {

        headerKupon?.apply {
            title = COUPON_HEADER_TITLE_SINGLE
            if(fromEdit){
                title = COUPON_HEADER_TITLE_SINGLE_EDIT
            }
            isShowBackButton = true
            setNavigationOnClickListener {
                tmTracker?.clickCouponListBack(prefManager?.shopId.toString())
                activity?.onBackPressed()
            }
        }
    }

    private fun renderButton() {

        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            setTitle(TERNS_AND_CONDITION)
            showCloseIcon = true
            isDragable = true
            setCloseClickListener {
                dismiss()
            }
        }
        val ss = SpannableString(COUPON_TERMS_CONDITION)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                RouteManager.route(
                    context,
                    String.format("%s?url=%s", ApplinkConst.WEBVIEW, TM_TNC)
                )
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
            firstIndex + TERNS_AND_CONDITION.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvTermsAndCondition.text = ss
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        if (fromEdit) {
            btnContinue.text = "Ubah Kupon"
        }
        if(fromDuplicate){
            btnContinue.text = "Buat Kupon"
        }
        btnContinue.setOnClickListener {
            tmTracker?.clickCreateCouponList(shopId = prefManager?.shopId.toString())
            continueCoupon(it)
        }
    }

    private fun continueCoupon(it: View) {

        if (TmInternetCheck.isConnectedToInternet(context)) {
            tmCouponStartTimeUnix?.timeInMillis?.let { start ->
                tmCouponEndTimeUnix?.timeInMillis?.let { end ->
                    if (start < end) {
                        it.isEnabled = false
                        it.isClickable = false
                        couponPremiumData = customViewSingleCoupon?.getSingleCouponData()
                        preValidateCouponPremium(couponPremiumData)
                    } else {
                        view?.let { v ->
                            Toaster.build(
                                v,
                                "Pengaturan tidak disimpan. Pastikan tanggal berakhir tidak mendahului tanggal mulai.",
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
            }
        } else {
            noInternetUi { continueCoupon(it) }
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
            quota = couponPremiumData?.quota?.let { CurrencyFormatHelper.convertRupiahToInt(it) }
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
        if(TmInternetCheck.isConnectedToInternet(context)) {
            tokomemberDashCreateViewModel.preValidateCoupon(validationRequest)
        }
        else{
            noInternetUi { tokomemberDashCreateViewModel.preValidateCoupon(validationRequest) }
        }
    }

    private fun uploadImagePremium() {
        context?.let { ctx ->
            CoroutineScope(Dispatchers.IO).launchCatchError(block = {
                withContext(Dispatchers.IO) {
                    val file = customViewSingleCoupon?.getCouponView()
                        ?.let { it1 -> TmFileUtil.saveBitMap(ctx, it1) }
                    if (file != null) {
                        if(TmInternetCheck.isConnectedToInternet(context)) {
                            tokomemberDashCreateViewModel.uploadImagePremium(file)
                        }
                        else{
                            noInternetUi { tokomemberDashCreateViewModel.uploadImagePremium(file)}
                        }
                    }
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    private fun renderProgram() {
        setProgramDateAuto()
        chipDateSelection.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionDate = position
                setProgramDateAuto()
            }
        })
        chipDateSelection.setDefaultSelection(selectedChipPositionDate)
        chipDateSelection.addChips(arrayListOf("Sesuai Periode Program", "Atur Manual"))

        textFieldProgramStartDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramStartTime.setFirstIcon(R.drawable.tm_dash_clock)
        textFieldProgramEndDate.setFirstIcon(R.drawable.tm_dash_calender)
        textFieldProgramEndTime.setFirstIcon(R.drawable.tm_dash_clock)

        textFieldProgramStartDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramStartDate, CALENDAR_TYPE_START)
        }
        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramStartTime, CALENDAR_TYPE_START)
        }
        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramEndDate, CALENDAR_TYPE_END)
        }
        textFieldProgramEndTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramEndTime, CALENDAR_TYPE_END)
        }
    }

    private fun setProgramDateAuto(){
        when(selectedChipPositionDate){
            ProgramDateType.AUTO -> {
                if(programData!= null) {
                    if(programStatus == ACTIVE || programStatus == ACTIVE_OLDER){
                        val todayCalendar =
                            GregorianCalendar(context?.let {
                                LocaleUtils.getCurrentLocale(
                                    it
                                )
                            })
                        todayCalendar.apply {
                            add(Calendar.HOUR_OF_DAY, HOUR_4)
                        }
                        val minuteCurrent = todayCalendar.get(Calendar.MINUTE)
                        if (minuteCurrent <= MINUTE_30){
                            todayCalendar.set(Calendar.MINUTE, MINUTE_30)
                        }
                        else{
                            todayCalendar.add(Calendar.HOUR_OF_DAY, 1)
                            todayCalendar.set(Calendar.MINUTE, 0)
                        }
                        todayCalendar.set(Calendar.SECOND,0)

                        val maxProgramEndDate = GregorianCalendar(context?.let {
                            LocaleUtils.getCurrentLocale(
                                it
                            )
                        })
                        maxProgramEndDate.add(Calendar.YEAR, 1)
                        val endDate = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, com.tokopedia.tokomember_seller_dashboard.util.locale)
                        endDate.time = sdf.parse(programData?.timeWindow?.endTime + "00") ?: Date()
                        if (endDate > maxProgramEndDate) {
                            tmCouponEndTimeUnix = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                            tmCouponEndDateUnix = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                            tmCouponEndTimeUnix?.time = maxProgramEndDate.time
                            tmCouponEndDateUnix?.time = maxProgramEndDate.time
//                            tmCouponEndTimeUnix?.time = TmDateUtil.convertDateTimeRemoveTimeDiff(maxProgramEndDate.time).toDate(SIMPLE_DATE_FORMAT)
//                            tmCouponEndDateUnix?.time = TmDateUtil.convertDateTimeRemoveTimeDiff(maxProgramEndDate.time).toDate(SIMPLE_DATE_FORMAT)

                            textFieldProgramEndDate.editText.setText("${tmCouponEndDateUnix?.get(Calendar.DAY_OF_WEEK)?.let {
                                getDayOfWeekID(it)
                            }}, ${
                                tmCouponEndDateUnix?.let {
                                    TmDateUtil.getDateFromUnix(
                                        it
                                    )
                                }?.let { TmDateUtil.setDatePreview(it, DATE_FORMAT) }
                            }")
                            textFieldProgramEndTime.editText.setText("23:59 WIB")

                        }
                        else {
                            val cal = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) })
                            cal.time = programData?.timeWindow?.endTime.toString().toDate(SIMPLE_DATE_FORMAT)
                            tmCouponEndTimeUnix = cal
                            tmCouponEndDateUnix = cal
                            textFieldProgramEndDate.editText.setText("${TmDateUtil.getDayFromTimeWindow(programData?.timeWindow?.endTime.toString())}, ${TmDateUtil.setDatePreview(programData?.timeWindow?.endTime.toString())}")
                            textFieldProgramEndTime.editText.setText(TmDateUtil.setTime(programData?.timeWindow?.endTime.toString()))
                        }

                        val todayDate = TmDateUtil.setDatePreview(TmDateUtil.getDateFromUnix(todayCalendar), DATE_FORMAT)
                        val day = getDayOfWeekID(todayCalendar.get(Calendar.DAY_OF_WEEK))
                        textFieldProgramStartDate.editText.setText("$day, $todayDate")

                        textFieldProgramStartTime.editText.setText(TmDateUtil.setTime(TmDateUtil.convertDateTime(todayCalendar.time)))
                        tmCouponStartDateUnix = todayCalendar
                        tmCouponStartTimeUnix = todayCalendar
                    }
                    else {
                        val calStart = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) })
                        val calEnd = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) })

                        val currentDate = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                        val currentHour = currentDate.get(Calendar.HOUR_OF_DAY)
                        val minuteCurrent = currentDate.get(Calendar.MINUTE)
                        val currentStartDate = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT,
                            com.tokopedia.tokomember_seller_dashboard.util.locale
                        )
                        currentStartDate.time = sdf.parse(programData?.timeWindow?.startTime ?: "" + "00") ?: Date()
                        if (currentHour >= 20 && checkYesterday(currentDate, currentStartDate)) {
                            currentDate.set(Calendar.HOUR_OF_DAY,currentHour)
                            currentDate.set(Calendar.MINUTE,0)
                            currentDate.add(Calendar.HOUR,4)
                            if (minuteCurrent <= 30) {
                                currentDate.set(Calendar.MINUTE, 30)
                            }
                            else {
                                currentDate.add(Calendar.HOUR, 1)
                                currentDate.set(Calendar.MINUTE, 0)
                            }
                            currentDate.set(Calendar.SECOND,0)
                            tmCouponStartDateUnix = currentDate
                            tmCouponStartTimeUnix = currentDate
                            textFieldProgramStartDate.editText.setText("${TmDateUtil.getDayFromTimeWindow(programData?.timeWindow?.startTime.toString())}, ${TmDateUtil.setDatePreview(TmDateUtil.convertDateTimeRemoveTimeDiff(currentDate.time))}")
                            textFieldProgramStartTime.editText.setText(tmCouponStartTimeUnix?.time?.let { TmDateUtil.convertDateTime(it) }?.let {
                                TmDateUtil.setTime(
                                    it
                                )
                            })
                        }
                        else {
                            calStart.time = programData?.timeWindow?.startTime.toString().toDate(SIMPLE_DATE_FORMAT)
                            tmCouponStartDateUnix = calStart
                            tmCouponStartTimeUnix = calStart
                            textFieldProgramStartDate.editText.setText("${TmDateUtil.getDayFromTimeWindow(programData?.timeWindow?.startTime.toString())}, ${TmDateUtil.setDatePreview(programData?.timeWindow?.startTime.toString())}")
                            textFieldProgramStartTime.editText.setText("00:00 WIB")
                        }

                        val maxProgramEndDate = GregorianCalendar(context?.let {
                            LocaleUtils.getCurrentLocale(
                                it
                            )
                        })
                        maxProgramEndDate.add(Calendar.YEAR, 1)
                        val endDate = GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                        endDate.time = sdf.parse(programData?.timeWindow?.endTime + "00") ?: Date()
                        if (endDate > maxProgramEndDate) {
                            tmCouponEndTimeUnix =
                                GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                            tmCouponEndDateUnix =
                                GregorianCalendar(com.tokopedia.tokomember_seller_dashboard.util.locale)
                            tmCouponEndTimeUnix?.time = maxProgramEndDate.time
                            tmCouponEndDateUnix?.time = maxProgramEndDate.time

                            textFieldProgramEndDate.editText.setText("${tmCouponEndDateUnix?.get(Calendar.DAY_OF_WEEK)?.let {
                                getDayOfWeekID(it)
                            }}, ${
                                tmCouponEndDateUnix?.let {
                                    TmDateUtil.getDateFromUnix(
                                        it
                                    )
                                }?.let { TmDateUtil.setDatePreview(it, DATE_FORMAT) }
                            }")
                            textFieldProgramEndTime.editText.setText(tmCouponEndDateUnix?.time?.let { TmDateUtil.convertDateTime(it) }?.let {
                                TmDateUtil.setTime(
                                    it
                                )
                            })
                        }
                        else {
                            calEnd.time = programData?.timeWindow?.endTime.toString().toDate(SIMPLE_DATE_FORMAT)

                            tmCouponEndDateUnix = calEnd
                            tmCouponEndTimeUnix = calEnd
                            textFieldProgramEndDate.editText.setText(
                                "${
                                    TmDateUtil.getDayFromTimeWindow(
                                        programData?.timeWindow?.endTime.toString()
                                    )
                                }, ${TmDateUtil.setDatePreview(programData?.timeWindow?.endTime.toString())}"
                            )
                            textFieldProgramEndTime.editText.setText(TmDateUtil.setTime(programData?.timeWindow?.endTime.toString()))
                        }
                    }
                }
                textFieldProgramStartDate.isEnabled = false
                textFieldProgramStartDate.iconContainer.isEnabled = false
                textFieldProgramStartTime.isEnabled  = false
                textFieldProgramStartTime.iconContainer.isEnabled = false
                textFieldProgramEndDate.isEnabled = false
                textFieldProgramEndDate.iconContainer.isEnabled = false
                textFieldProgramEndTime.isEnabled  = false
                textFieldProgramEndTime.iconContainer.isEnabled = false
            }
            ProgramDateType.MANUAL -> {
                textFieldProgramStartDate.isEnabled = true
                textFieldProgramStartDate.iconContainer.isEnabled = true
                textFieldProgramStartDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramStartTime.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndDate.editText.inputType = InputType.TYPE_NULL
                textFieldProgramEndTime.editText.inputType = InputType.TYPE_NULL
                textFieldProgramStartTime.isEnabled  = true
                textFieldProgramStartTime.iconContainer.isEnabled = true
                textFieldProgramEndDate.isEnabled = true
                textFieldProgramEndDate.iconContainer.isEnabled = true
                textFieldProgramEndTime.isEnabled  = true
                textFieldProgramEndTime.iconContainer.isEnabled = true

                textFieldProgramStartDate.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                    clickDatePicker(textFieldProgramStartDate, CALENDAR_TYPE_START)
                }
                textFieldProgramStartTime.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                    clickTimePicker(textFieldProgramStartTime, CALENDAR_TYPE_START)
                }
                textFieldProgramEndDate.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                    clickDatePicker(textFieldProgramEndDate, CALENDAR_TYPE_END)
                }
                textFieldProgramEndTime.editText.setOnFocusChangeListener { view, b ->
                    if(b)
                    clickTimePicker(textFieldProgramEndTime, CALENDAR_TYPE_END)
                }
            }
        }
        setTotalTransactionAmount()

    }

    private fun checkYesterday(calendarToday: Calendar, calendarProgram: Calendar): Boolean {
        return calendarToday.get(Calendar.YEAR) == calendarProgram.get(Calendar.YEAR)
                && calendarToday.get(Calendar.DAY_OF_YEAR) == calendarProgram.get(Calendar.DAY_OF_YEAR) - 1
    }

    private fun renderSingleCoupon() {
        tvLevelMember.show()
        chipGroupLevel.show()


        chipGroupLevel.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionLevel = position
            }
        })

        chipGroupLevel.setDefaultSelection(selectedChipPositionLevel)
        chipGroupLevel.addChips(arrayListOf("Premium", "VIP"))

        initTotalTransactionAmount()
    }
    private fun initTotalTransactionAmount() {
        customViewSingleCoupon?.setMaxTransactionListener(object : TmSingleCouponView.MaxTransactionListener{
            override fun onQuotaCashbackChange() {
                setTotalTransactionAmount()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalTransactionAmount() {
        val interMediateVipData = customViewSingleCoupon?.getSingleCouponData()
        textFieldSingleMaxTransaction.text = "Rp" + CurrencyFormatHelper.convertToRupiah(
            (CurrencyFormatHelper.convertRupiahToInt(interMediateVipData?.maxCashback ?: "") * interMediateVipData?.quota.toIntSafely()).toString())
    }

    private fun clickDatePicker(textField: TextFieldUnify2, type: Int) {
        var date = ""
        var month = ""
        var year = ""
        var day = 0
        context?.let{
            val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, com.tokopedia.tokomember_seller_dashboard.util.locale)
            val defaultCalendar = GregorianCalendar(LocaleUtils.getCurrentLocale(it))
            val minCalendar = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            // if active program
            // then default selection is today
            // if upcoming program
            // then start time of the program should be default

            // for both cases
            // user can select end date 1 year from start date
            if(fromEdit || fromDuplicate){
                defaultCalendar.time = tmCouponStartDateUnix?.time
            }
            else {
                if (programStatus != ACTIVE) {
                    defaultCalendar.time = sdf.parse(programData?.timeWindow?.startTime + "00") ?: Date()
                }
            }
            val calendarMax = GregorianCalendar(LocaleUtils.getCurrentLocale(it))
            if (tmCouponStartDateUnix != null && type == CALENDAR_TYPE_START && firstDateStart) {
                defaultCalendar.time = tmCouponStartDateUnix?.time
            }
            if (tmCouponEndDateUnix != null && type == CALENDAR_TYPE_END && firstDateEnd) {
                defaultCalendar.time = tmCouponEndDateUnix?.time
            }
            if (tmCouponStartDateUnix != null && type == CALENDAR_TYPE_END && !firstDateEnd && firstDateStart) {
                defaultCalendar.time = tmCouponStartDateUnix?.time
            }
            calendarMax.time = defaultCalendar.time
            minCalendar.time = defaultCalendar.time
            if(tmCouponStartDateUnix != null){
                calendarMax.time = tmCouponStartDateUnix?.time
                minCalendar.time = tmCouponStartDateUnix?.time
            }

            if(fromEdit || fromDuplicate){
                if(type == CALENDAR_TYPE_START) {
                    calendarMax.time = tmCouponStartDateUnix?.time
                    minCalendar.time = tmCouponStartDateUnix?.time
                }
                if(type == CALENDAR_TYPE_END) {
                    calendarMax.time = tmCouponEndDateUnix?.time
                    minCalendar.time = tmCouponEndDateUnix?.time
                }
            }
            else {
                if(type == CALENDAR_TYPE_START){
                    calendarMax.time = sdf.parse(programData?.timeWindow?.startTime + "00") ?: Date()
                    minCalendar.time = sdf.parse(programData?.timeWindow?.startTime + "00") ?: Date()
                }
            }
            calendarMax.add(Calendar.YEAR, 1)

            val datepickerObject = DateTimePickerUnify(it, minCalendar, defaultCalendar, calendarMax).apply {
                when (type) {
                    CALENDAR_TYPE_START -> {
                        setTitle(DATE_TITLE)
                        setInfo(DATE_DESC)
                    }
                    CALENDAR_TYPE_END -> {
                        setTitle(DATE_TITLE_END)
                        setInfo(DATE_DESC_END)
                    }
                }
                setInfoVisible(true)
                datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        selectedTime = selectedCalendar?.time.toString()
                        date = selectedCalendar?.get(Calendar.DATE).toString()
                        month = selectedCalendar?.getDisplayName(Calendar.MONTH, Calendar.LONG, LocaleUtils.getIDLocale()).toString()
                        year = selectedCalendar?.get(Calendar.YEAR).toString()
                        day = selectedCalendar?.get(Calendar.DAY_OF_WEEK)?:0

                        val dayInId = getDayOfWeekID(day)
                        textField.textInputLayout.editText?.setText(( "$dayInId,$date $month $year"))
                        when (type) {
                            CALENDAR_TYPE_START -> {
                                firstDateStart = true
                                tmCouponStartDateUnix = selectedCalendar
                                selectedCalendar?.get(Calendar.DAY_OF_MONTH)?.let { it1 ->
                                    tmCouponStartTimeUnix?.set(Calendar.DAY_OF_MONTH,
                                        it1
                                    )
                                }
                                selectedCalendar?.get(Calendar.MONTH)?.let { it1 ->
                                    tmCouponStartTimeUnix?.set(Calendar.MONTH,
                                        it1
                                    )
                                }
                                selectedCalendar?.get(Calendar.YEAR)?.let { it1 ->
                                    tmCouponStartTimeUnix?.set(Calendar.YEAR,
                                        it1
                                    )
                                }
                            }
                            CALENDAR_TYPE_END -> {
                                firstDateEnd = true
                                tmCouponEndDateUnix = selectedCalendar
                                selectedCalendar?.get(Calendar.DAY_OF_MONTH)?.let { it1 ->
                                    tmCouponEndTimeUnix?.set(Calendar.DAY_OF_MONTH,
                                        it1
                                    )
                                }
                                selectedCalendar?.get(Calendar.MONTH)?.let { it1 ->
                                    tmCouponEndTimeUnix?.set(Calendar.MONTH,
                                        it1
                                    )
                                }
                                selectedCalendar?.get(Calendar.YEAR)?.let { it1 ->
                                    tmCouponEndTimeUnix?.set(Calendar.YEAR,
                                        it1
                                    )
                                }
                            }
                        }
                        dismiss()
                    }
                }
            }
            childFragmentManager.let { fragmentManager->
                datepickerObject.show(fragmentManager, "")
            }
        }
    }

    private fun clickTimePicker(textField: TextFieldUnify2, type: Int){
        var selectedHour = ""
        var selectedMinute = ""

        // in case of active program, start time should set to 4 hours (round of to nearest multiple of 30 minutes) from now by default
        // example
        // if 4:23 -> 8:30
        // if 4:37 -> 9:00

        // end time same as program end time
        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, com.tokopedia.tokomember_seller_dashboard.util.locale)

        context?.let { ctx ->
            var minTime = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }
            var defaultTime = GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }
            val maxTime = GregorianCalendar( LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                }

            if (tmCouponStartTimeUnix != null && type == CALENDAR_TYPE_START && firstTimeStart) {
                tmCouponStartTimeUnix?.get(Calendar.HOUR_OF_DAY)?.let {
                    defaultTime.set(Calendar.HOUR_OF_DAY,
                        it
                    )
                }
                tmCouponStartTimeUnix?.get(Calendar.MINUTE)?.let {
                    defaultTime.set(Calendar.MINUTE,
                        it
                    )
                }
            }
            if (tmCouponEndTimeUnix != null && type == CALENDAR_TYPE_END && firstTimeEnd) {
                tmCouponEndTimeUnix?.get(Calendar.HOUR_OF_DAY)?.let {
                    defaultTime.set(Calendar.HOUR_OF_DAY,
                        it
                    )
                }
                tmCouponEndTimeUnix?.get(Calendar.MINUTE)?.let {
                    defaultTime.set(Calendar.MINUTE,
                        it
                    )
                }
            }

            val timerPickerUnify = DateTimePickerUnify(
                context = ctx,
                minDate = minTime,
                defaultDate =  defaultTime,
                maxDate = maxTime,
                type = DateTimePickerUnify.TYPE_TIMEPICKER
            ).apply {
                minuteInterval = MINUTE_30
                when(type){
                    CALENDAR_TYPE_START -> {
                        setTitle(TIME_TITLE)
                        setInfo(TIME_DESC)
                    }
                    CALENDAR_TYPE_END -> {
                        setTitle(TIME_TITLE_END)
                        setInfo(TIME_DESC_END)
                    }
                }
                setInfoVisible(true)
                datePickerButton.setOnClickListener {
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue
                    textField.textInputLayout.editText?.setText(( "$selectedHour:$selectedMinute WIB"))
                    when (type) {
                        CALENDAR_TYPE_START -> {
                            startTime = getDate()
                            val day = tmCouponStartDateUnix?.get(Calendar.DAY_OF_MONTH)
                            val month = tmCouponStartDateUnix?.get(Calendar.MONTH)
                            val year = tmCouponStartDateUnix?.get(Calendar.YEAR)
                            if (day != null) {
                                startTime?.set(Calendar.DAY_OF_MONTH, day)
                            }
                            if (month != null) {
                                startTime?.set(Calendar.MONTH, month)
                            }
                            if (year != null) {
                                startTime?.set(Calendar.YEAR, year)
                            }
                            startTime?.set(Calendar.HOUR_OF_DAY, timePicker.hourPicker.activeValue.toIntSafely())
                            startTime?.set(Calendar.MINUTE, timePicker.minutePicker.activeValue.toIntSafely())
                            tmCouponStartTimeUnix = startTime
                            tmCouponStartTimeUnix?.time?.let { it1 -> TmDateUtil.convertDateTime(it1) }
                            firstTimeStart = true
                        }
                        CALENDAR_TYPE_END -> {
                            startTime = getDate()
                            val day = tmCouponEndDateUnix?.get(Calendar.DAY_OF_MONTH)
                            val month = tmCouponEndDateUnix?.get(Calendar.MONTH)
                            val year = tmCouponEndDateUnix?.get(Calendar.YEAR)
                            if (day != null) {
                                startTime?.set(Calendar.DAY_OF_MONTH, day)
                            }
                            if (month != null) {
                                startTime?.set(Calendar.MONTH, month)
                            }
                            if (year != null) {
                                startTime?.set(Calendar.YEAR, year)
                            }
                            startTime?.set(Calendar.HOUR_OF_DAY, timePicker.hourPicker.activeValue.toIntSafely())
                            startTime?.set(Calendar.MINUTE, timePicker.minutePicker.activeValue.toIntSafely())
                            tmCouponEndTimeUnix = startTime
                            tmCouponEndTimeUnix?.time?.let { it1 -> TmDateUtil.convertDateTime(it1) }
                            firstTimeEnd = true
                        }
                    }
                    dismiss()
                }
            }

            childFragmentManager.let {
                timerPickerUnify.show(it,"")
            }
        }
    }

    companion object {
        private var tmCouponListRefreshCallback: TmCouponListRefreshCallback? = null

        fun newInstance(bundle: Bundle, tmCouponListRefreshCallback: TmCouponListRefreshCallback
        ): TmSingleCouponCreateFragment {
            this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
            return TmSingleCouponCreateFragment().apply {
                arguments = bundle
            }
        }
    }
}
