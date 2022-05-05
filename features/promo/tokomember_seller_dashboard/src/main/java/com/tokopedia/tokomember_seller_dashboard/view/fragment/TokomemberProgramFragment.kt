package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.ProgramType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.model.ProgramThreshold
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_TYPE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.ProgramUpdateMapper
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashCreateViewModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import java.util.*
import javax.inject.Inject

class TokomemberProgramFragment : BaseDaggerFragment(), ChipGroupCallback {

    private var selectedTime = ""
    private var fromEdit = false
    private var programType = ProgramType.CREATE
    private var periodInMonth = 0
    private var selectedChipPosition = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashCreateViewModel: TokomemberDashCreateViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashCreateViewModel::class.java)
    }
    private var selectedCalendar: Calendar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getBoolean(BUNDLE_EDIT_PROGRAM, false)?.let {
            fromEdit = it
        }
        arguments?.getInt(BUNDLE_PROGRAM_TYPE, 0)?.let {
            programType = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_progrm_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderHeader()
        observeViewModel()
        callGQL(programType,arguments?.getInt(BUNDLE_SHOP_ID)?:0, arguments?.getInt(BUNDLE_PROGRAM_ID)?:0  )
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashCreateViewModel.tokomemberProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    if (it.data.membershipGetProgramForm?.resultStatus?.code == "200") {
                        renderProgramUI(it.data.membershipGetProgramForm)
                    }
                    else{
                        handleError()
                    }
                }
                is Fail -> {
                    handleError()
                }
            }
        })

        tokomemberDashCreateViewModel.tokomemberProgramUpdateResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    if(it.data.membershipCreateEditProgram?.resultStatus?.code=="200"){
                        onProgramUpdateSuccess()
                    }
                    else{
                        handleError()
                    }
                }
                is Fail ->{
                    handleError()
                }
            }
        })
    }


    private fun handleError(){

    }

    private fun callGQL(programType: Int, shopId: Int , programId:Int = 0){
        when(programType){
            ProgramType.CREATE ->{
                tokomemberDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_CREATE)
            }
            ProgramType.DETAIL ->{
                tokomemberDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_DETAIL)
            }
            ProgramType.EXTEND ->{
                tokomemberDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_EXTEND)
            }
            ProgramType.EDIT ->{
                //TODO actionType edit pending from backend
                tokomemberDashCreateViewModel.getProgramInfo(programId,shopId, ACTION_EDIT)
            }
            ProgramType.CANCEL ->{
                //TODO
            }
        }
    }

    private fun onProgramUpdateSuccess() {
        /* TODO handle by Jayant according to update type */
        when(programType){
            ProgramType.CREATE -> {
                (activity as TokomemberDashCreateActivity).addFragment(
                    TokomemberKuponCreateFragment.newInstance(),
                    "Kupon"
                )
            }
            ProgramType.EXTEND ->{
                (activity as TokomemberDashCreateActivity).addFragment(
                    TokomemberKuponCreateFragment.newInstance(),
                    "Kupon"
                )
            }
            ProgramType.EDIT ->{

            }
        }
    }

    private fun renderHeader() {

        when(programType){
            ProgramType.CREATE ->{
                btnCreateProgram.text = "Buat Program"
                headerProgram?.apply {
                    title = "Daftar TokoMember"
                    subtitle = "Langkah 2 dari 4"
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(50, false)
                }
            }
            ProgramType.EXTEND ->{
                headerProgram?.apply {
                    title = "Perpanjang TokoMember"
                    isShowBackButton = true
                }
                progressProgram?.apply {
                    progressBarColorType = ProgressBarUnify.COLOR_GREEN
                    progressBarHeight = ProgressBarUnify.SIZE_SMALL
                    setValue(33, false)
                }
                btnCreateProgram.text = "Buat Program"
                textFieldTranskVip.isEnabled = false
                textFieldTranskPremium.isEnabled = false
            }
            ProgramType.EDIT ->{
                //TODO actionType edit pending from backend

                headerProgram?.apply {
                    title = "Ubah Program"
                    isShowBackButton = true
                }
                btnCreateProgram.text = "Simpan"
                progressProgram?.hide()
            }
        }
    }

    private fun renderProgramUI(membershipGetProgramForm: MembershipGetProgramForm?) {
        addPremiumTransactionTextListener(membershipGetProgramForm?.programThreshold)
        addVipTransactionTextListener(membershipGetProgramForm?.programThreshold)
        if(programType == ProgramType.EXTEND){
            textFieldTranskVip.isEnabled = false
            textFieldTranskPremium.isEnabled = false
            cardEditInfo.show()
        }
        else{
            textFieldTranskVip.isEnabled = true
            textFieldTranskPremium.isEnabled = true
            cardEditInfo.hide()
        }
        membershipGetProgramForm?.timePeriodList?.getOrNull(0)?.months?.let {
            periodInMonth = it
        }
        membershipGetProgramForm?.programForm?.timeWindow?.startTime?.let {
            selectedTime = it
            textFieldDuration.editText.setText(ProgramUpdateMapper.setDate(selectedTime))
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
            clickDatePicker("Pilih tanggal mulai","Tentukan tanggal mulai untuk kupon TokoMember yang sudah kamu buat.")
        }
        btnCreateProgram?.setOnClickListener {
            initCreateProgram(membershipGetProgramForm)
        }
    }

    private fun addPremiumTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskPremium.editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.toString()
                        .toIntOrZero() >= programThreshold?.maxThresholdLevel1 ?: 0
                ) {
                    tickerInfo.show()
                    textFieldTranskPremium.isInputError = true
                    textFieldTranskPremium.setMessage("Maks ${programThreshold?.maxThresholdLevel1}")
                } else if (s.toString()
                        .toIntOrZero() <= programThreshold?.maxThresholdLevel1 ?: 0
                ) {
                    tickerInfo.hide()
                    textFieldTranskPremium.isInputError = false
                    textFieldTranskPremium.setMessage("")
                }
                else if (textFieldTranskVip.editText.text.toString().isNotEmpty() && s.toString()
                        .toIntOrZero() >= textFieldTranskVip.editText.text.toString().toIntOrZero()
                ) {
                    tickerInfo.show()
                    textFieldTranskPremium.isInputError = true
                    textFieldTranskPremium.setMessage("Tidak boleh melebihi level VIP (Level 2)")
                }
                else if (textFieldTranskVip.editText.text.toString().isNotEmpty() && s.toString()
                        .toIntOrZero() <= textFieldTranskVip.editText.text.toString().toIntOrZero()
                ) {
                    tickerInfo.hide()
                    textFieldTranskPremium.isInputError = false
                    textFieldTranskPremium.setMessage("")
                }
            }
        })
    }

    private fun addVipTransactionTextListener(programThreshold: ProgramThreshold?) {
        textFieldTranskVip.editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    s.toString()
                        .toIntOrZero() >= programThreshold?.maxThresholdLevel2 ?: 0 -> {
                        textFieldTranskVip.isInputError = true
                        textFieldTranskVip.setMessage("Maks ${programThreshold?.maxThresholdLevel2}")
                    }
                    s.toString()
                        .toIntOrZero() <= programThreshold?.minThresholdLevel2 ?: 0 -> {
                        textFieldTranskVip.isInputError = true
                        textFieldTranskVip.setMessage("Min ${programThreshold?.minThresholdLevel1}")
                    }
                    else -> {
                        textFieldTranskVip.isInputError = false
                        textFieldTranskVip.setMessage("")
                    }
                }
            }
        })
    }

    private fun initCreateProgram(membershipGetProgramForm: MembershipGetProgramForm?){
        if(programType != ProgramType.EXTEND) {
            val pre = textFieldTranskPremium.editText.text.toString()
            val vip = textFieldTranskVip.editText.text.toString()
            if (membershipGetProgramForm != null) {
                membershipGetProgramForm.programForm?.tierLevels?.getOrNull(0)?.threshold =
                    pre.toIntSafely()
                membershipGetProgramForm.programForm?.tierLevels?.getOrNull(1)?.threshold =
                    vip.toIntSafely()
            }
        }
        membershipGetProgramForm?.programForm?.timeWindow?.startTime = selectedTime
        membershipGetProgramForm?.timePeriodList?.getOrNull(selectedChipPosition)?.months?.let {
            periodInMonth = it
        }
        val programUpdateResponse = ProgramUpdateMapper.formToUpdateMapper(membershipGetProgramForm, arguments?.getInt(BUNDLE_PROGRAM_TYPE)?:0, periodInMonth)
        tokomemberDashCreateViewModel.updateProgram(programUpdateResponse)
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
        fun newInstance(extras: Bundle?) = TokomemberProgramFragment().apply {
            arguments = extras
        }
    }

    override fun chipSelected(position: Int) {
        this.selectedChipPosition = position
    }
}