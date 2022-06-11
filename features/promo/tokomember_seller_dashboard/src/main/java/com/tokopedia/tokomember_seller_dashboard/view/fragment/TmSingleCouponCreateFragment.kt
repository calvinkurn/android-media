package com.tokopedia.tokomember_seller_dashboard.view.fragment

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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CouponType
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
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ANDROID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_VOUCHER_ID
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_CASHBACK_PREVIEW
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_DISCOUNT_TYPE_IDR
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_DISCOUNT_TYPE_PERCENT
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_HEADER_TITLE_SINGLE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_MEMBER
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_SHIPPING_PREVIEW
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TERMS_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TYPE_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_TYPE_SHIPPING
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_VIP
import com.tokopedia.tokomember_seller_dashboard.util.CREATE
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ErrorState
import com.tokopedia.tokomember_seller_dashboard.util.IDR
import com.tokopedia.tokomember_seller_dashboard.util.LOADING_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.PREMIUM
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_VALIDATION_CTA_TEXT
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_VALIDATION_ERROR_DESC
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_VALIDATION_ERROR_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.RETRY
import com.tokopedia.tokomember_seller_dashboard.util.TERMS
import com.tokopedia.tokomember_seller_dashboard.util.TERNS_AND_CONDITION
import com.tokopedia.tokomember_seller_dashboard.util.TIME_DESC
import com.tokopedia.tokomember_seller_dashboard.util.TIME_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmFileUtil
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.util.UPDATE
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_program_fragment.*
import kotlinx.android.synthetic.main.tm_dash_single_coupon.*
import kotlinx.android.synthetic.main.tm_kupon_create_single.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TmSingleCouponCreateFragment : BaseDaggerFragment() {

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
    private var voucherId = 0
    private var errorCount = 0

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_single_kupon_cotainer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderHeader()
        renderButton()
        observeViewModel()
        shopName = arguments?.getString(BUNDLE_SHOP_NAME)?:""
        shopAvatar = arguments?.getString(BUNDLE_SHOP_AVATAR)?:""
        fromEdit = arguments?.getBoolean(ACTION_EDIT)?:false
        programData = arguments?.getParcelable(BUNDLE_PROGRAM_DATA)
        if(fromEdit){
            voucherId = arguments?.getInt(BUNDLE_VOUCHER_ID)?:0
            tokomemberDashCreateViewModel.getInitialCouponData(UPDATE,"")
        }
        else{
            tokomemberDashCreateViewModel.getInitialCouponData(CREATE,"")
            val prefManager = context?.let { it1 -> TmPrefManager(it1) }
            prefManager?.shopId?.let { it ->
                prefManager.cardId?.let { it1 ->
                    tmProgramListViewModel?.getProgramList(it, it1)
                }
            }
            renderSingleCoupon()
        }
    }

    private fun getCouponDetails() {
        tokomemberDashCreateViewModel.getCouponDetail(voucherId)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val dagger = DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
        dagger.inject(this)
    }

    private fun observeViewModel() {

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
                                if (item?.status == 4 || item?.status == 3) {
                                    //check if starttime < current time
                                    if (TmDateUtil.getTimeInMillis(item.timeWindow?.startTime, "yyyy-MM-dd HH:mm:ss").toLong() > Date().time) {
                                        startTime = item.timeWindow?.startTime
                                        endTime = item.timeWindow?.endTime
                                        return@time
                                    }
                                    else{
                                        view?.let { it1 -> Toaster.build(it1, "Select proper start time", Toaster.TYPE_ERROR, Toaster.LENGTH_LONG).show() }
                                    }
                                }
                            }
                        }
                        if(startTime.isNullOrEmpty()) {
                            if (TmDateUtil.getTimeInMillis(
                                    it.data?.membershipGetProgramList?.programSellerList?.firstOrNull()?.timeWindow?.startTime,
                                    "yyyy-MM-dd HH:mm:ss"
                                ).toLong() > Date().time
                            ) {
                                startTime =
                                    it.data?.membershipGetProgramList?.programSellerList?.firstOrNull()?.timeWindow?.startTime
                                endTime =
                                    it.data?.membershipGetProgramList?.programSellerList?.firstOrNull()?.timeWindow?.endTime
                                programData = ProgramUpdateDataInput()
                                programData?.apply {
                                    timeWindow = TimeWindow(startTime = startTime, endTime = endTime)
                                }
                            }
                            else{
                                view?.let { it1 -> Toaster.build(it1, "Select proper start time", Toaster.TYPE_ERROR, Toaster.LENGTH_LONG).show() }

                            }
                        }
                        renderProgram()
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
                    if(fromEdit){
                        token = it.data?.getInitiateVoucherPage?.data?.token
                        getCouponDetails()
                    }
                    else{

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
                    if (it.data?.voucherValidationPartial?.header?.messages?.size == 1){
                        tokomemberDashCreateViewModel.validateProgram(arguments?.getInt(
                            BUNDLE_SHOP_ID).toString(),programData?.timeWindow?.startTime?:"",programData?.timeWindow?.endTime?:"")
                    }
                    else {
                        handleProgramValidateError(it.data?.voucherValidationPartial?.data?.validationError,"premium")
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
                        handleProgramPreValidateError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR-> {
                    errorState.isValidateCouponError = false
                    setButtonState()
                    closeLoadingDialog()
                    handleProgramValidateServerError()
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
                                updateCoupon()
                            }
                            else{

                            }
                        }
                        is UploadResult.Error ->{
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
                    view?.let { it1 -> Toaster.build(it1, RETRY,Toaster.TYPE_ERROR,Toaster.LENGTH_LONG).show() }
                }
                else->{}
            }
        })

        tokomemberDashCreateViewModel.tmCouponDetaillLiveData.observe(viewLifecycleOwner, {
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS ->{
                    programData = ProgramUpdateDataInput()
                    programData?.apply {
                        val startTime = it.data?.merchantPromotionGetMVDataByID?.data?.voucherStartTime
                        val endTime = it.data?.merchantPromotionGetMVDataByID?.data?.voucherFinishTime
                        timeWindow = TimeWindow(startTime = startTime, endTime = endTime)
                    }
                    tmCouponDetail = it.data?.merchantPromotionGetMVDataByID?.data
                    renderUIForEdit(it.data?.merchantPromotionGetMVDataByID?.data)
                    renderProgram()
                    renderSingleCoupon()
                }
                TokoLiveDataResult.STATUS.ERROR ->{
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
                    // Back to coupon list
                    closeLoadingDialog()
                    setButtonState()
                    activity?.finish()
                    tmCouponListRefreshCallback?.refreshCouponList()
                }
                TokoLiveDataResult.STATUS.LOADING ->{

                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    //handleError
                    closeLoadingDialog()
                    setButtonState()
                }
            }
        })
    }

    private fun updateCoupon() {
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
            TmDateUtil.getTimeFromUnix(
                tmCouponStartDateUnix!!
            )
        }
        else{
            programData?.timeWindow?.startTime?.substringBefore("T")
        }
        val maxCashBack = textFieldMaxCashback.editText.text.toString().replace(".", "").toInt()
        val tmCouponUpdateRequest = TmCouponUpdateRequest(
            benefitMax = tmCouponDetail?.voucherDiscountAmtMax,
            targetBuyer = 3,
            image = "it.data.uploadId",
            code = "",
            minPurchase = tmCouponDetail?.voucherMinimumAmt,
            minimumTierLevel = level,
            benefitPercent = tmCouponDetail?.voucherDiscountAmt,
            hourEnd = hourEnd,
            dateEnd = dateEnd,
            hourStart = hourStart,
            dateStart = dateStart,
            source = "android",
            imageSquare = tmCouponDetail?.voucherImageSquare,
            couponName = tmCouponDetail?.voucherName,
            token = token,
            benefitType = benefitType,
            benefitIdr = maxCashBack * textFieldQuota.editText.text.toString().toInt(),
            imagePortrait = tmCouponDetail?.voucherImagePortrait,
            quota = textFieldQuota.editText.text.toString().toInt(),
            isPublic = tmCouponDetail?.isPublic,
            voucherId = tmCouponDetail?.voucherId,
            couponType = couponType,
            is_lock_to_product = tmCouponDetail?.isLockToProduct,
            product_ids = tmCouponDetail?.productIds.toString().replace("[", "").replace("]", ""),
            warehouse_id = tmCouponDetail?.warehouseId,
            product_ids_csv_url = ""

        )
        tokomemberDashCreateViewModel.updateCoupon(tmCouponUpdateRequest)
    }

    private fun renderUIForEdit(data: TmCouponDetailData?) {

        tvLevelMember.show()
        chipGroupLevel.show()

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
        chipGroupLevel.addChips(arrayListOf("Premium", "VIP"))

        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
                when(selectedChipPositionKupon){
                    CouponType.CASHBACK -> {
                        ivPreviewCoupon.showHideCashBackValueView(true)
                        ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
                    }
                    CouponType.SHIPPING -> {
                        ivPreviewCoupon.showHideCashBackValueView(false)
                        ivPreviewCoupon.setCouponType(COUPON_SHIPPING_PREVIEW)
                    }
                }
            }
        })
        if(data?.voucherType == COUPON_TYPE_CASHBACK){
            ivPreviewCoupon.showHideCashBackValueView(true)
            ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
            selectedChipPositionKupon = 0
        }
        if(data?.voucherType == COUPON_TYPE_SHIPPING){
            ivPreviewCoupon.showHideCashBackValueView(false)
            ivPreviewCoupon.setCouponType(COUPON_SHIPPING_PREVIEW)
            selectedChipPositionKupon = 1
        }
        chipGroupKuponType.setDefaultSelection(selectedChipPositionKupon)

        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
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
    }

    private fun openLoadingDialog(){

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.loaderText?.apply {
            setType(Typography.DISPLAY_2)
        }
        loaderDialog?.setLoadingText(Html.fromHtml(LOADING_TEXT))
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

    private fun handleProgramValidateError(validationError: ValidationError?, couponType: String) {
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

    private fun handleProgramPreValidateError(){
        setButtonState()
        val bundle = Bundle()
        val tmIntroBottomSheetModel = TmIntroBottomsheetModel(PROGRAM_VALIDATION_ERROR_TITLE, PROGRAM_VALIDATION_ERROR_DESC , "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png", PROGRAM_VALIDATION_CTA_TEXT )
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomSheetModel))
        val bottomSheet = TokomemberBottomsheet.createInstance(bundle)
        bottomSheet.setUpBottomSheetListener(object : BottomSheetClickListener {
            override fun onButtonClick(errorCount: Int) {
                bottomSheet.dismiss()
            }
        })
        bottomSheet.show(childFragmentManager,"")
    }

    private fun handleProgramValidateServerError(){

        val title = when(retryCount){
            0-> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when(retryCount){
            0-> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(title, ERROR_CREATING_DESC , "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png", cta , errorCount = retryCount)
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.setUpBottomSheetListener(object : BottomSheetClickListener{
            override fun onButtonClick(errorCount: Int) {
                when (errorCount) {
                    0 -> tokomemberDashCreateViewModel.validateProgram("", "", "")
                    else -> {
                        (TokomemberDashIntroActivity.openActivity(
                            0, "", "",
                            false,
                            context
                        ))
                    }
                }
            }})
        bottomsheet.show(childFragmentManager,"")
        retryCount +=1
    }

    private fun renderHeader() {

        headerKupon?.apply {
            title = COUPON_HEADER_TITLE_SINGLE
            isShowBackButton = true
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        progressKupon.hide()
    }

    private fun renderButton() {

        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            setTitle(TERNS_AND_CONDITION)
            showCloseIcon  = true
            isDragable = true
            setCloseClickListener {
                dismiss()
            }
        }
        val ss = SpannableString(COUPON_TERMS_CONDITION)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                bottomSheetUnify.show(childFragmentManager,"")
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val firstIndex = COUPON_TERMS_CONDITION.indexOf(TERMS)
        ss.setSpan(clickableSpan, firstIndex, firstIndex  + TERNS_AND_CONDITION.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvTermsAndCondition.text = Html.fromHtml(ss.toString())
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        btnContinue.setOnClickListener {
            it.isEnabled = false
            it.isClickable = false
            couponPremiumData = customViewSingleCoupon?.getSingleCouponData()
            preValidateCouponPremium(couponPremiumData)
        }
    }

    private fun preValidateCouponPremium(couponPremiumData: TmSingleCouponData?) {
        tokomemberDashCreateViewModel.preValidateCoupon(
            TmCouponValidateRequest(
                targetBuyer = 3,
                benefitType = IDR,
                couponType = couponPremiumData?.typeCoupon,
                benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback?:""),
                benefitPercent = couponPremiumData?.cashBackPercentage,
                minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki?:""),
                source = ANDROID
            )
        )
    }

    private fun uploadImagePremium() {
        context?.let { ctx ->
            CoroutineScope(Dispatchers.IO).launchCatchError(block = {
                withContext(Dispatchers.IO) {
                    val file = customViewSingleCoupon?.getCouponView()
                        ?.let { it1 -> TmFileUtil.saveBitMap(ctx, it1) }
                    if (file != null) {
                        tokomemberDashCreateViewModel.uploadImagePremium(file)
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
            clickDatePicker(textFieldProgramStartDate, 0)
        }

        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramStartTime, 0)
        }

        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(textFieldProgramEndDate, 1)
        }

        textFieldProgramEndTime.iconContainer.setOnClickListener {
            clickTimePicker(textFieldProgramEndTime, 1)
        }
    }

    private fun setProgramDateAuto(){
        when(selectedChipPositionDate){
            ProgramDateType.AUTO -> {

                if(programData!= null) {
                    textFieldProgramStartDate.editText.setText(
                        TmDateUtil.setDate(
                            programData?.timeWindow?.startTime.toString()
                        )
                    )
                    textFieldProgramStartTime.editText.setText(
                        TmDateUtil.setTime(
                            programData?.timeWindow?.startTime.toString()
                        )
                    )
                    textFieldProgramEndDate.editText.setText(TmDateUtil.setDate(programData?.timeWindow?.endTime.toString()))
                    textFieldProgramEndTime.editText.setText(TmDateUtil.setTime(programData?.timeWindow?.endTime.toString()))
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
            }
        }

    }

    private fun renderSingleCoupon() {
        tvLevelMember.show()
        chipGroupLevel.show()
    }

    private fun clickDatePicker(textField: TextFieldUnify2, type: Int) {
        var date = ""
        var month = ""
        var year = ""
        context?.let{
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.YEAR, TmProgramFragment.MAX_YEAR)
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            val calMin = Calendar.getInstance()
            calMin.add(Calendar.YEAR, TmProgramFragment.MIN_YEAR)
            val yearMin = calMin.get(Calendar.YEAR)
            val monthMin = calMin.get(Calendar.MONTH)
            val dayMin = calMin.get(Calendar.DAY_OF_MONTH)

            val minDate = GregorianCalendar(yearMin, monthMin, dayMin)
            val datepickerObject = DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                setTitle(DATE_TITLE)
                setInfo(DATE_DESC)
                setInfoVisible(true)
                datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        selectedTime = selectedCalendar?.time.toString()
                        date = selectedCalendar?.get(Calendar.DATE).toString()
                        month = selectedCalendar?.getDisplayName(Calendar.MONTH, Calendar.LONG, LocaleUtils.getIDLocale()).toString()
                        year = selectedCalendar?.get(Calendar.YEAR).toString()
                        textField.textInputLayout.editText?.setText(( "$date $month $year"))
                        when (type) {
                            0 -> { tmCouponStartDateUnix = selectedCalendar }
                            1 -> { tmCouponEndDateUnix = selectedCalendar }
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
        context?.let { ctx ->
            val minTime =
                GregorianCalendar(LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.MINUTE, 30)
                }
            val defaultTime =
                GregorianCalendar( LocaleUtils.getCurrentLocale(ctx))
            val maxTime =
                GregorianCalendar( LocaleUtils.getCurrentLocale(ctx)).apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 30)
                }

            val timerPickerUnify = DateTimePickerUnify(
                context = ctx,
                minDate = minTime,
                defaultDate = defaultTime,
                maxDate = maxTime,
                type = DateTimePickerUnify.TYPE_TIMEPICKER
            ).apply {
                setTitle(TIME_TITLE)
                setInfo(TIME_DESC)
                setInfoVisible(true)
                datePickerButton.setOnClickListener {
                    startTime = getDate()
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue
                    textField.textInputLayout.editText?.setText(( "$selectedHour : $selectedMinute WIB"))
                    when (type) {
                        0 -> { tmCouponStartTimeUnix = startTime }
                        1 -> { tmCouponEndTimeUnix = startTime }
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