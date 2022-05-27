package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Html
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
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.ProgramDateType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponValidateRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.model.ValidationError
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TmSingleCouponView
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.android.synthetic.main.tm_dash_kupon_create.*
import kotlinx.android.synthetic.main.tm_dash_kupon_create_container.*
import kotlinx.android.synthetic.main.tm_dash_single_coupon.*
import java.util.*
import javax.inject.Inject

class TokomemberKuponCreateFragment : BaseDaggerFragment(), BottomSheetClickListener {

    private var selectedChipPositionDate: Int = 0
    private var selectedChipPositionLevel: Int = 0
    private var selectedChipPositionKupon: Int = 0
    private var selectedChipPositionCashback: Int = 0
    private var tmCouponPremium: TmSingleCouponView? = null
    private var tmCouponVip: TmSingleCouponView? = null
    private var selectedCalendar: Calendar? = null
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

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
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
        renderHeader()
        renderButton()
        observeViewModel()
        shopName = arguments?.getString(BUNDLE_SHOP_NAME)?:""
        shopAvatar = arguments?.getString(BUNDLE_SHOP_AVATAR)?:""
        programData = arguments?.getParcelable(BUNDLE_PROGRAM_DATA)
        tokomemberDashCreateViewModel.getInitialCouponData("create",3,"")
        renderProgram()
        if (arguments?.getBoolean(IS_SINGLE_COUPON,false) == true){
            renderSingleCoupon()
        } else {
            renderMultipleCoupon()
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        val dagger = DaggerTokomemberDashComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
        dagger.inject(this)
    }

    private fun observeViewModel() {

        tokomemberDashCreateViewModel.tmCouponInitialLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING ->{
                    containerViewFlipper.displayedChild = 2
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    containerViewFlipper.displayedChild = 0
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
                     preValidateCouponVip(couponVip)
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

        tokomemberDashCreateViewModel.tmCouponPreValidateMultipleCouponLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                 TokoLiveDataResult.STATUS.SUCCESS -> {
                     errorState.isPreValidatePremiumError = false
                     if (it.data?.voucherValidationPartial?.header?.messages?.size == 1) {
                        tokomemberDashCreateViewModel.validateProgram(arguments?.getString(
                            BUNDLE_SHOP_ID).toString(),programData?.timeWindow?.startTime?:"",programData?.timeWindow?.endTime?:"")
                    }
                    else {
                         setButtonState()
                         handleProgramValidateError(it.data?.voucherValidationPartial?.data?.validationError,"vip")
                    }
                }
                 TokoLiveDataResult.STATUS.ERROR -> {
                     errorState.isPreValidatePremiumError = true
                     setButtonState()
                     closeLoadingDialog()
                     handleProgramValidateNetworkError()
                }
                else->{}
            }
        })

        tokomemberDashCreateViewModel.tmProgramValidateLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                 TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipValidateBenefit?.resultStatus?.code == "200") {
                        errorState.isValidateCouponError = false
                        uploadImagePremium()
                    } else {
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

        tokomemberDashCreateViewModel.tmCouponUploadLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    errorState.isUploadPremium = false
                    uploadImageVip()
                }
                TokoLiveDataResult.STATUS.ERROR-> {
                    errorState.isUploadPremium = true
                    setButtonState()
                    closeLoadingDialog()
                    view?.let { it1 -> Toaster.build(it1,"Coba Lagi",Toaster.TYPE_ERROR,Toaster.LENGTH_LONG).show() }
                }
                else->{}
            }
        })

        tokomemberDashCreateViewModel.tmCouponUploadMultipleLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    errorState.isUploadVipError = false
                    closeLoadingDialog()
                    openPreviewPage()
                }
                TokoLiveDataResult.STATUS.ERROR-> {
                    errorState.isUploadVipError = true
                    setButtonState()
                    closeLoadingDialog()
                    view?.let { it1 -> Toaster.build(it1,"Coba Lagi",Toaster.TYPE_ERROR,Toaster.LENGTH_LONG).show() }
                }
                else->{}
            }
        })
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
        view?.let { Toaster.build(it,"Coba Lagi" , Toaster.LENGTH_LONG , Toaster.TYPE_ERROR ).show() }
        btnContinue.apply {
            isEnabled = true
            isClickable = true
        }
    }

    private fun handleProgramValidateError(validationError: ValidationError?, couponType: String) {
        when(couponType){

            "premium" -> {
                tmCouponPremium?.setErrorMaxBenefit(validationError?.benefitMax?:"")
                tmCouponPremium?.setErrorCashbackPercentage(validationError?.benefitPercent?:"")
                tmCouponPremium?.setErrorMinTransaction(validationError?.minPurchase?:"")
                tmCouponPremium?.setErrorQuota(validationError?.quota?:"")
                accordionUnifyPremium.expandAllGroup()
            }

            "vip" -> {
                tmCouponVip?.setErrorMaxBenefit(validationError?.benefitMax?:"")
                tmCouponVip?.setErrorCashbackPercentage(validationError?.benefitPercent?:"")
                tmCouponVip?.setErrorMinTransaction(validationError?.minPurchase?:"")
                tmCouponVip?.setErrorQuota(validationError?.quota?:"")
                accordionUnifyVIP.expandAllGroup()
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
        val title = "Oops, tanggal aktif kupon ada di luar periode program"
        val desc = "Pastikan tanggal aktif kupon ada di dalam periode Program TokoMember, ya!"
        val bottomSheetUnify = BottomSheetUnify()
        val view  = View.inflate(this.context,R.layout.tm_dash_intro_bottomsheet,null)
        view.findViewById<ImageUnify>(R.id.img_bottom_sheet).loadImage("https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png")
        view.findViewById<Typography>(R.id.tv_heading).text = title
        view.findViewById<Typography>(R.id.tv_desc).text = desc
        view.findViewById<UnifyButton>(R.id.btn_proceed).apply {
            text = "Ubah Tanggal"
            setOnClickListener {
                bottomSheetUnify.dismiss()
            }
        }
        bottomSheetUnify.apply {
            setChild(view)
            showCloseIcon = true
        }.show(childFragmentManager,"")

        setButtonState()
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
        TokomemberBottomsheet().setUpBottomSheetListener(this)
        TokomemberBottomsheet.show(bundle,childFragmentManager)
        retryCount +=1
    }

    override fun onButtonClick(errorCount: Int) {
        when(errorCount){
            0 -> tokomemberDashCreateViewModel.validateProgram("","","")
            else -> {
                (TokomemberDashIntroActivity.openActivity(
                    0,"","",
                    false,
                    this.context
                ))
            }
        }
    }

    private fun openPreviewPage(){

    }

    private fun renderHeader() {

        headerKupon?.apply {
            title = "Daftar Tokomember"
            subtitle = "Langkah 3 dari 4"
            isShowBackButton = true
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        progressKupon?.apply {
            progressBarColorType = ProgressBarUnify.COLOR_GREEN
            progressBarHeight = ProgressBarUnify.SIZE_SMALL
            setValue(80, false)
        }
    }

    private fun renderButton() {

        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            setTitle("Syarat & ketentuan")
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
        val firstIndex = COUPON_TERMS_CONDITION.indexOf("syarat")
        ss.setSpan(clickableSpan, firstIndex, firstIndex  + "syarat & ketentuan".length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvTermsAndCondition.text = Html.fromHtml(ss.toString())
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
        tvTermsAndCondition.highlightColor = Color.TRANSPARENT
        btnContinue.setOnClickListener {
            it.isEnabled = false
            it.isClickable = false
            couponPremiumData = tmCouponPremium?.getSingleCouponData()
            couponVip = tmCouponVip?.getSingleCouponData()
            preValidateCouponPremium(couponPremiumData)
        }
    }

    private fun preValidateCouponVip(coupon: TmSingleCouponData?) {
        tokomemberDashCreateViewModel.preValidateMultipleCoupon(
            TmCouponValidateRequest(
                targetBuyer = 3,
                benefitType = "idr",
                couponType = coupon?.typeCoupon,
                benefitMax = coupon?.maxCashback.toIntSafely(),
                benefitPercent = coupon?.cashBackPercentage,
                minPurchase = coupon?.minTransaki.toIntSafely(),
                source = "android"
            )
        )
    }

    private fun preValidateCouponPremium(couponPremiumData: TmSingleCouponData?) {
        tokomemberDashCreateViewModel.preValidateCoupon(
            TmCouponValidateRequest(
                targetBuyer = 3,
                benefitType = "idr",
                couponType = couponPremiumData?.typeCoupon,
                benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.maxCashback?:""),
                benefitPercent = couponPremiumData?.cashBackPercentage,
                minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki?:""),
                source = "android"
            )
        )
    }

    private fun uploadImagePremium(){
        context?.let { ctx->
            val file =  tmCouponPremium?.getCouponView()?.let { it1 -> FileUtil.saveBitMap(ctx, it1) }
            if (file != null) {
                tokomemberDashCreateViewModel.uploadImagePremium(file)
            }
        }
    }

    private fun uploadImageVip(){
        context?.let { ctx->
            val file =  tmCouponVip?.getCouponView()?.let { it1 -> FileUtil.saveBitMap(ctx, it1) }
            if (file != null) {
                tokomemberDashCreateViewModel.uploadImageVip(file)
            }
        }
    }

    private fun renderMultipleCoupon() {
        tmCouponPremium = this.context?.let { TmSingleCouponView(it) }
        tmCouponPremium?.setShopData(shopName,shopAvatar)
        val itemAccordionPremium = tmCouponPremium?.let {
            AccordionDataUnify(
                title = "Untuk member Premium",
                expandableView = it,
                isExpanded = false,
                icon = context?.getDrawable(R.drawable.ic_tokomember_premium),
                subtitle = "Cashback Rp10.000"
            ).apply {
                borderBottom = true
                borderTop = true
            }
        }
        tmCouponVip = this.context?.let { TmSingleCouponView(it) }
        tmCouponVip?.setShopData(shopName,shopAvatar)
        val itemAccordionVip = tmCouponVip?.let {
            AccordionDataUnify(
                title = "Untuk member VIP",
                subtitle = "Cashback Rp10.000",
                expandableView = it,
                isExpanded = false,
                icon = context?.getDrawable(R.drawable.ic_tokomember_vip)
            ).apply {
                borderBottom = false
                borderTop = false
            }
        }

        if (itemAccordionPremium != null) {
            accordionUnifyPremium.addGroup(itemAccordionPremium)
        }
        if (itemAccordionVip != null) {
            accordionUnifyVIP.addGroup(itemAccordionVip)
        }
        accordionUnifyPremium.onItemClick = ::handleAccordionClickPremium
        accordionUnifyVIP.onItemClick = ::handleAccordionClickVip

    }

    private fun handleAccordionClickPremium(position:Int, expanded: Boolean){
        if (expanded){
            accordionUnifyPremium.expandGroup(position)
        } else {
            accordionUnifyPremium.collapseGroup(position)
        }
    }

    private fun handleAccordionClickVip(position:Int, expanded: Boolean){
        if (expanded){
            accordionUnifyVIP.expandGroup(position)
        } else {
            accordionUnifyVIP.collapseGroup(position)
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
            clickDatePicker(
                "Pilih tanggal mulai",
                "Tentukan tanggal mulai untuk kupon TokoMember yang sudah kamu buat.",
                textFieldProgramStartDate
            )
        }

        textFieldProgramStartTime.iconContainer.setOnClickListener {
            clickTimePicker(
                "Pilih jam mulai",
                "Tentukan jam mulai untuk kupon TokoMember yang sudah kamu buat.",
                textFieldProgramStartTime
            )
        }

        textFieldProgramEndDate.iconContainer.setOnClickListener {
            clickDatePicker(
                "Pilih tanggal mulai",
                "Tentukan tanggal mulai untuk kupon TokoMember yang sudah kamu buat.",
                textFieldProgramEndDate
            )
        }

        textFieldProgramEndTime.iconContainer.setOnClickListener {
            clickTimePicker(
                "Pilih jam mulai",
                "Tentukan jam mulai untuk kupon TokoMember yang sudah kamu buat.",
                textFieldProgramEndTime
            )
        }
    }

    private fun setProgramDateAuto(){
        when(selectedChipPositionDate){
            ProgramDateType.AUTO -> {

                textFieldProgramStartDate.editText.setText(ProgramUpdateMapper.setDate(programData?.timeWindow?.startTime.toString()))
                textFieldProgramStartTime.editText.setText(ProgramUpdateMapper.setTime(programData?.timeWindow?.startTime.toString()))
                textFieldProgramEndDate.editText.setText(ProgramUpdateMapper.setDate(programData?.timeWindow?.endTime.toString()))
                textFieldProgramEndTime.editText.setText(ProgramUpdateMapper.setTime(programData?.timeWindow?.endTime.toString()))
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
        containerSingleCoupon.show()

        chipGroupLevel.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionLevel = position
            }
        })
        chipGroupLevel.setDefaultSelection(selectedChipPositionLevel)
        chipGroupLevel.addChips(arrayListOf("Premium", "VIP"))

        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
            }
        })
        chipGroupKuponType.setDefaultSelection(selectedChipPositionKupon)
        chipGroupKuponType.addChips(arrayListOf("Cashback", "Gratis Ongkir"))

        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
            }
        })
        chipGroupCashbackType.setDefaultSelection(selectedChipPositionCashback)
        chipGroupCashbackType.addChips(arrayListOf("Rupiah(Rp)", "Persentase (%)"))

    }

    private fun clickDatePicker(
        title: String,
        helpText: String,
        textField: TextFieldUnify2
    ) {
        var date = ""
        var month = ""
        var year = ""
        context?.let{
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.YEAR, TokomemberProgramFragment.MAX_YEAR);
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            val calMin = Calendar.getInstance()
            calMin.add(Calendar.YEAR, TokomemberProgramFragment.MIN_YEAR);
            val yearMin = calMin.get(Calendar.YEAR)
            val monthMin = calMin.get(Calendar.MONTH)
            val dayMin = calMin.get(Calendar.DAY_OF_MONTH)

            val minDate = GregorianCalendar(yearMin, monthMin, dayMin)
            val datepickerObject = DateTimePickerUnify(it, minDate, currentDate, maxDate).apply {
                setTitle(title)
                setInfo(helpText)
                setInfoVisible(true)
                datePickerButton.let { button ->
                    button.setOnClickListener {
                        selectedCalendar = getDate()
                        // TODO convert Sun May 29 00:00:00 GMT+05:30 2022 to 2023-04-01 23:59:59 +07 otherwise invalid parameter coming
                        selectedTime = selectedCalendar?.time.toString()
                        date = selectedCalendar?.get(Calendar.DATE).toString()
                        month = selectedCalendar?.getDisplayName(Calendar.MONTH, Calendar.LONG, LocaleUtils.getIDLocale()).toString()
                        year = selectedCalendar?.get(Calendar.YEAR).toString()
                        dismiss()
                    }
                }
            }
            childFragmentManager.let {
                datepickerObject.show(it, "")
            }
            datepickerObject.setOnDismissListener {
                selectedTime = selectedCalendar?.time.toString()
                textField.textInputLayout.editText?.setText(( "$date $month $year"))
            }
        }
    }

    private fun clickTimePicker(title: String,helpText: String,textField: TextFieldUnify2){
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
                setTitle(title)
                setInfo(helpText)
                setInfoVisible(true)
                datePickerButton.setOnClickListener {
                    startTime = getDate()
                    selectedHour = timePicker.hourPicker.activeValue
                    selectedMinute = timePicker.minutePicker.activeValue
                    dismiss()
                }
            }

            childFragmentManager.let {
                timerPickerUnify.show(it,"")
            }
            timerPickerUnify.setOnDismissListener {
                textField.textInputLayout.editText?.setText(( "$selectedHour : $selectedMinute WIB"))
            }
        }
    }

    companion object {

        const val IS_SINGLE_COUPON = "IS_SINGLE_COUPON"
        fun newInstance(bundle: Bundle): TokomemberKuponCreateFragment {
            return TokomemberKuponCreateFragment().apply {
                arguments = bundle
            }
        }
    }
}