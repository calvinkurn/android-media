package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.TokomemberLoaderDialog
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CreateScreenType
import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmOpenFragmentCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.model.ProgramThreshold
import com.tokopedia.tokomember_seller_dashboard.model.TmIntroBottomsheetModel
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_DATA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.util.DATE_DESC
import com.tokopedia.tokomember_seller_dashboard.util.DATE_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_CTA_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_DESC
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.ERROR_CREATING_TITLE_RETRY
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA
import com.tokopedia.tokomember_seller_dashboard.util.PROGRAM_CTA_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.TM_PROGRAM_EDIT_DIALOG_TITLE
import com.tokopedia.tokomember_seller_dashboard.util.TM_PROGRAM_MIN_PURCHASE_ERROR
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.convertDateTime
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil.setDate
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashIntroActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.customview.BottomSheetClickListener
import com.tokopedia.tokomember_seller_dashboard.view.customview.TokomemberBottomsheet
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_program_form_container.*
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import java.util.*
import javax.inject.Inject

class TmProgramFragment : BaseDaggerFragment(), ChipGroupCallback ,
    BottomSheetClickListener {

    private lateinit var tmOpenFragmentCallback: TmOpenFragmentCallback
    private var selectedTime = ""
    private var fromEdit = false
    private var programActionType = ProgramActionType.CREATE
    private var periodInMonth = 0
    private var selectedChipPosition = 0
    private var errorCount = 0
    private var programUpdateResponse = ProgramUpdateDataInput()
    private var tmTracker: TmTracker? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmDashCreateViewModel: TmDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmDashCreateViewModel::class.java)
    }
    private var selectedCalendar: Calendar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getBoolean(BUNDLE_EDIT_PROGRAM, false)?.let {
            fromEdit = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_TYPE, 0)?.let {
            programActionType = it
        }

        if (context is TmOpenFragmentCallback) {
            tmOpenFragmentCallback =  context as TmOpenFragmentCallback
        } else {
            throw Exception(context.toString() )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_program_form_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tmTracker = TmTracker()
        renderHeader()
        observeViewModel()
        callGQL(programActionType,arguments?.getInt(BUNDLE_SHOP_ID)?:0, arguments?.getInt(BUNDLE_PROGRAM_ID)?:0  )
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun observeViewModel() {
        tmDashCreateViewModel.tmProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.LOADING -> {
                    containerViewFlipper.displayedChild = SHIMMER
                }
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if (it.data?.membershipGetProgramForm?.resultStatus?.code == "200") {
                        renderProgramUI(it.data.membershipGetProgramForm)
                    }
                    else{
                        handleErrorOnDataError()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR -> {
                    handleErrorOnDataError()
                }
            }
        })

        tmDashCreateViewModel.tokomemberProgramUpdateResultLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                TokoLiveDataResult.STATUS.SUCCESS -> {
                    if(it.data?.membershipCreateEditProgram?.resultStatus?.code=="200"){
                        onProgramUpdateSuccess()
                    }
                    else{
                        onProgramUpdateSuccess()
                        //  handleErrorOnUpdate()
                    }
                }
                TokoLiveDataResult.STATUS.ERROR ->{
                    onProgramUpdateSuccess()
                    //handleErrorOnUpdate()
                }
            }
        })
    }

    private fun handleErrorOnDataError(){
        containerViewFlipper.displayedChild = ERROR
        globalError.setActionClickListener {
            callGQL(programActionType,arguments?.getInt(BUNDLE_SHOP_ID)?:0, arguments?.getInt(BUNDLE_PROGRAM_ID)?:0)
        }
    }

    private fun handleErrorOnUpdate() {
        val title = when(errorCount){
            0-> ERROR_CREATING_TITLE
            else -> ERROR_CREATING_TITLE_RETRY
        }
        val cta = when(errorCount){
            0-> ERROR_CREATING_CTA
            else -> ERROR_CREATING_CTA_RETRY
        }
        //TODO remote res
        val bundle = Bundle()
        val tmIntroBottomsheetModel = TmIntroBottomsheetModel(title, ERROR_CREATING_DESC , "https://images.tokopedia.net/img/android/res/singleDpi/quest_widget_nonlogin_banner.png", cta , errorCount = errorCount)
        bundle.putString(TokomemberBottomsheet.ARG_BOTTOMSHEET, Gson().toJson(tmIntroBottomsheetModel))
        val bottomsheet = TokomemberBottomsheet.createInstance(bundle)
        bottomsheet.show(childFragmentManager,"")
        errorCount+=1
    }

    private fun callGQL(programType: Int, shopId: Int , programId:Int = 0){
        when(programType){
            ProgramActionType.CREATE ->{
                tmDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_CREATE)
            }
            ProgramActionType.DETAIL ->{
                tmDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_DETAIL)
            }
            ProgramActionType.EXTEND ->{
                tmDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_EXTEND)
            }
            ProgramActionType.EDIT ->{
                tmDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_EDIT)
            }
            ProgramActionType.CANCEL ->{
                //TODO
            }
        }
    }

    private fun onProgramUpdateSuccess() {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_PROGRAM_TYPE, programActionType)
        bundle.putString(BUNDLE_SHOP_AVATAR, arguments?.getString(BUNDLE_SHOP_AVATAR))
        bundle.putString(BUNDLE_SHOP_NAME, arguments?.getString(BUNDLE_SHOP_NAME))
        bundle.putInt(BUNDLE_SHOP_ID, arguments?.getInt(BUNDLE_SHOP_ID) ?: 0)
        bundle.putInt(BUNDLE_CARD_ID , arguments?.getInt(BUNDLE_CARD_ID)?:0)
        bundle.putParcelable(BUNDLE_CARD_DATA , arguments?.getParcelable(BUNDLE_CARD_DATA))
        bundle.putParcelable(BUNDLE_PROGRAM_DATA, programUpdateResponse)
        when(programActionType){
            ProgramActionType.CREATE -> {
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE, bundle)
            }
            ProgramActionType.EXTEND ->{
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE, bundle)
            }
            ProgramActionType.EDIT ->{
                val intent = Intent()
                intent.putExtra("REFRESH_STATE", REFRESH)
                activity?.setResult(RESULT_OK, intent)
                activity?.finish()
            }
            ProgramActionType.CREATE_BUAT ->{
                tmOpenFragmentCallback.openFragment(CreateScreenType.COUPON_MULTIPLE_BUAT, bundle)
            }
        }
    }

    private fun renderHeader() {

        headerProgram.setNavigationOnClickListener {
            if(programActionType == ProgramActionType.CREATE) {
                tmTracker?.clickProgramCreationBack(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }
            if(programActionType == ProgramActionType.CREATE_BUAT) {
                tmTracker?.clickProgramCreationBackFromProgramList(arguments?.getInt(BUNDLE_SHOP_ID).toString())
            }

            val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
            dialog?.setTitle("Yakin batalkan program?")
            dialog?.setDescription("Pengaturan yang dibuat akan hilang kalau kamu batalkan proses pengaturan TokoMember, lho.")
            dialog?.setPrimaryCTAText("Lanjut")
            dialog?.setSecondaryCTAText("Batalkan Program")
            dialog?.setPrimaryCTAClickListener {
                tmTracker?.clickProgramCreationCancelPopupPrimary(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                dialog.dismiss()
            }
            dialog?.setSecondaryCTAClickListener {
                tmTracker?.clickProgramCreationCancelPopupSecondary(arguments?.getInt(BUNDLE_SHOP_ID).toString())
                activity?.onBackPressed()
            }
            dialog?.show()
        }

        when(programActionType){
            ProgramActionType.CREATE ->{
                btnCreateProgram.text = PROGRAM_CTA
                headerProgram?.apply {
                    title = HEADER_TITLE_CREATE
                    subtitle = HEADER_TITLE_DESC
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(50, false)
                }
            }
            ProgramActionType.EXTEND ->{
                headerProgram?.apply {
                    title = HEADER_TITLE_EXTEND
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(33, false)
                }
                btnCreateProgram.text = PROGRAM_CTA
                textFieldTranskVip.isEnabled = false
                textFieldTranskPremium.isEnabled = false
            }
            ProgramActionType.EDIT ->{
                //TODO actionType edit pending from backend
                headerProgram?.apply {
                    title = HEADER_TITLE_EDIT
                    isShowBackButton = true
                }
                btnCreateProgram.text = PROGRAM_CTA_EDIT
                progressProgram?.hide()
            }
        }
    }

    private fun renderProgramUI(membershipGetProgramForm: MembershipGetProgramForm?) {
        textFieldDuration.editText.inputType = InputType.TYPE_NULL
        containerViewFlipper.displayedChild = DATA
        addPremiumTransactionTextListener(membershipGetProgramForm?.programThreshold)
        addVipTransactionTextListener(membershipGetProgramForm?.programThreshold)
        if(programActionType == ProgramActionType.EXTEND){
            textFieldTranskVip.isEnabled = false
            textFieldTranskPremium.isEnabled = false
            cardEditInfo.show()
        }
        else{
            textFieldTranskVip.isEnabled = true
            textFieldTranskPremium.isEnabled = true
            textFieldTranskPremium.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel1.toString() ?: "")
            textFieldTranskVip.editText.setText(membershipGetProgramForm?.programThreshold?.minThresholdLevel2.toString() ?: "")
            cardEditInfo.hide()
        }
        membershipGetProgramForm?.timePeriodList?.getOrNull(0)?.months?.let {
            periodInMonth = it
        }
        membershipGetProgramForm?.programForm?.timeWindow?.startTime?.let {
            selectedTime = it
            textFieldDuration.editText.setText(setDate(selectedTime))
        }
        membershipGetProgramForm?.timePeriodList?.forEach {
            if(it?.isSelected == true){
                selectedChipPosition = membershipGetProgramForm.timePeriodList.indexOf(it)
            }
        }
        chipGroup.setCallback(this)
        chipGroup.setDefaultSelection(selectedChipPosition)
        membershipGetProgramForm?.timePeriodList?.forEach {
            if (it != null) {
                it.name?.let { it1 -> chipGroup.addChip(it1) }
            }
        }
        context?.let {
            textFieldDuration?.setFirstIcon(R.drawable.tm_dash_calender)
        }
        textFieldDuration.iconContainer.setOnClickListener {
            clickDatePicker(DATE_TITLE,DATE_DESC)
        }
        btnCreateProgram?.setOnClickListener {
            if(programActionType == ProgramActionType.CREATE) {
                tmTracker?.clickProgramCreationButton(
                    arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                    arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                )
            }
            if(programActionType == ProgramActionType.CREATE_BUAT) {
                tmTracker?.clickProgramCreationButtonFromProgramList(
                    arguments?.getInt(BUNDLE_SHOP_ID).toString(),
                    arguments?.getInt(BUNDLE_PROGRAM_ID).toString()
                )
            }

                initCreateProgram(membershipGetProgramForm)
        }
    }

    private fun addPremiumTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskPremium.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    if (number >= programThreshold?.maxThresholdLevel1 ?: 0
                    ) {
                        tickerInfo.show()
                        textFieldTranskPremium.isInputError = true
                        textFieldTranskPremium.setMessage("Maks ${programThreshold?.maxThresholdLevel1}")
                    } else if (number <= programThreshold?.maxThresholdLevel1 ?: 0
                    ) {
                        tickerInfo.hide()
                        textFieldTranskPremium.isInputError = false
                        textFieldTranskPremium.setMessage("")
                    }
                    else if (textFieldTranskVip.editText.text.toString().isNotEmpty() && number>= textFieldTranskVip.editText.text.toString().toIntOrZero()
                    ) {
                        tickerInfo.show()
                        textFieldTranskPremium.isInputError = true
                        textFieldTranskPremium.setMessage(TM_PROGRAM_MIN_PURCHASE_ERROR)
                    }
                    else if (textFieldTranskVip.editText.text.toString().isNotEmpty() && number<= textFieldTranskVip.editText.text.toString().toIntOrZero()
                    ) {
                        tickerInfo.hide()
                        textFieldTranskPremium.isInputError = false
                        textFieldTranskPremium.setMessage("")
                    }
                }
            })
        }
    }

    private fun addVipTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskVip.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText) {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number>= programThreshold?.maxThresholdLevel2 ?: 0 -> {
                            textFieldTranskVip.isInputError = true
                            textFieldTranskVip.setMessage("Maks ${programThreshold?.maxThresholdLevel2}")
                        }
                        number <= programThreshold?.minThresholdLevel2 ?: 0 -> {
                            textFieldTranskVip.isInputError = true
                            textFieldTranskVip.setMessage("Min ${programThreshold?.minThresholdLevel2}")
                        }
                        else -> {
                            textFieldTranskVip.isInputError = false
                            textFieldTranskVip.setMessage("")
                        }
                    }
                }
            })
        }
    }

    private fun initCreateProgram(membershipGetProgramForm: MembershipGetProgramForm?){
        val pre = textFieldTranskPremium.editText.text.toString()
        val vip = textFieldTranskVip.editText.text.toString()
        if (membershipGetProgramForm != null) {
            membershipGetProgramForm.programForm?.tierLevels?.getOrNull(0)?.threshold =
                pre.replace(".","").toIntSafely()
            membershipGetProgramForm.programForm?.tierLevels?.getOrNull(1)?.threshold =
                vip.replace(".","").toIntSafely()
        }
        if(selectedCalendar != null) {
            membershipGetProgramForm?.programForm?.timeWindow?.startTime =
                selectedCalendar?.time?.let {
                    convertDateTime(it)
                }
        }
        membershipGetProgramForm?.timePeriodList?.getOrNull(selectedChipPosition)?.months?.let {
            periodInMonth = it
        }
        if(programActionType == ProgramActionType.EDIT){
            context?.let { TokomemberLoaderDialog.showLoaderDialog(it, TM_PROGRAM_EDIT_DIALOG_TITLE) }
        }
        programUpdateResponse = ProgramUpdateMapper.formToUpdateMapper(
            membershipGetProgramForm,
            arguments?.getInt(BUNDLE_PROGRAM_TYPE) ?: 0,
            periodInMonth,
            arguments?.getInt(BUNDLE_CARD_ID) ?: 0
        )
        tmDashCreateViewModel.updateProgram(programUpdateResponse)
    }

    private fun clickDatePicker(title: String, helpText: String) {
         var date = ""
         var month = ""
         var year = ""
        context?.let{
            val calMax = Calendar.getInstance()
            calMax.add(Calendar.YEAR, MAX_YEAR);
            val yearMax = calMax.get(Calendar.YEAR)
            val monthMax = calMax.get(Calendar.MONTH)
            val dayMax = calMax.get(Calendar.DAY_OF_MONTH)

            val maxDate = GregorianCalendar(yearMax, monthMax, dayMax)
            val currentDate = GregorianCalendar(LocaleUtils.getCurrentLocale(it))

            val calMin = Calendar.getInstance()
            calMin.add(Calendar.YEAR, MIN_YEAR);
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
                textFieldDuration?.textInputLayout?.editText?.setText(( "$date $month $year"))
            }
        }
    }

     fun getDate(): Calendar? {
        return selectedCalendar
    }

    companion object {
        const val MAX_YEAR = 10
        const val MIN_YEAR = -90
        const val HEADER_TITLE_CREATE = "Daftar TokoMember"
        const val HEADER_TITLE_DESC = "Langkah 2 dari 4"
        const val HEADER_TITLE_EXTEND = "Perpanjang TokoMember"
        const val HEADER_TITLE_EDIT =  "Ubah Program"

        const val DATA = 1
        const val SHIMMER = 0
        const val ERROR = 2
        fun newInstance(extras: Bundle?) = TmProgramFragment().apply {
            arguments = extras
        }
    }

    override fun chipSelected(position: Int) {
        this.selectedChipPosition = position
    }

    override fun onButtonClick(errorCount: Int) {
        if (errorCount == 0) {
            tmDashCreateViewModel.updateProgram(programUpdateResponse)
        } else {
            (TokomemberDashIntroActivity.openActivity(
                arguments?.getInt(BUNDLE_SHOP_ID) ?: 0,arguments?.getString(BUNDLE_SHOP_AVATAR)?:"" ,arguments?.getString(
                    BUNDLE_SHOP_NAME)?:"",
                false,
                this.context
            ))
        }
    }
}