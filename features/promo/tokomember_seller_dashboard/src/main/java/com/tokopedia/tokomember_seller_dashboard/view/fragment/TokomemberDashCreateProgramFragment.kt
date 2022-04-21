package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TimeWindow
import com.tokopedia.tokomember_seller_dashboard.model.MembershipGetProgramForm
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.util.ACTION_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_EDIT_PROGRAM_ITEM
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashProgramViewmodel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.tm_dash_progrm_form.*
import java.util.*
import javax.inject.Inject

class TokomemberDashCreateProgramFragment : BaseDaggerFragment() {

    private var fromEdit = false
    private var editItem: ProgramSellerListItem? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashProgramViewmodel: TokomemberDashProgramViewmodel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashProgramViewmodel::class.java)
    }
    private var selectedCalendar: Calendar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getBoolean(BUNDLE_EDIT_PROGRAM, false)?.let {
            fromEdit = it
        }
        arguments?.getString(BUNDLE_EDIT_PROGRAM_ITEM, "")?.let {
            editItem = Gson().fromJson(it, ProgramSellerListItem::class.java)
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
        tokomemberDashProgramViewmodel.getProgramInfo(0,6553698, ACTION_CREATE)
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    private fun observeViewModel() {
        tokomemberDashProgramViewmodel.tokomemberProgramResultLiveData.observe(viewLifecycleOwner,{
            when(it){
                is Success -> {
                    renderProgramUI(it.data.membershipGetProgramForm)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun renderHeader() {
        if(fromEdit) {
            headerProgram?.apply {
                title = "Ubah Program"
                isShowBackButton = true
            }
            progressProgram?.hide()
            btnCreateProgram.text = "Simpan"
        }
        else{
            btnCreateProgram.text = "Buat Program"
            headerProgram?.apply {
                title = "Buat Program"
                subtitle = "Langkah 2 dari 4"
                isShowBackButton = true
            }
            progressProgram?.apply {
                progressBarColorType = ProgressBarUnify.COLOR_GREEN
                progressBarHeight = ProgressBarUnify.SIZE_SMALL
                setValue(50, false)
            }
        }
    }

    private fun renderProgramUI(membershipGetProgramForm: MembershipGetProgramForm?) {
        chipOne.chipText = membershipGetProgramForm?.timePeriodList?.getOrNull(0)?.name
        chipTwo.chipText = membershipGetProgramForm?.timePeriodList?.getOrNull(1)?.name
        context?.let {
            textFieldDuration?.setFirstIcon(R.drawable.tm_dash_calender)
        }
        textFieldDuration.iconContainer.setOnClickListener {
            clickDatePicker("Pilih tanggal mulai","Tentukan tanggal mulai untuk kupon TokoMember yang sudah kamu buat.")
        }
        chipOne.setOnClickListener {
            chipOne.chipType  = ChipsUnify.TYPE_SELECTED
            chipTwo.chipType = ChipsUnify.TYPE_NORMAL
        }
        chipTwo.setOnClickListener {
            chipOne.chipType  = ChipsUnify.TYPE_NORMAL
            chipTwo.chipType = ChipsUnify.TYPE_SELECTED
        }
        //TODO 1. get program name
        btnCreateProgram?.setOnClickListener {
            if(fromEdit){
                tokomemberDashProgramViewmodel.updateProgram(
                    ProgramUpdateDataInput(
                        actionType = "edit",
                        apiVersion = "3.0",
                        name = editItem?.name,
                        cardID = editItem?.cardID,
                        timeWindow = TimeWindow(startTime = editItem?.timeWindow?.startTime, endTime = editItem?.timeWindow?.endTime)
                    )
                )
            }
            else {
                tokomemberDashProgramViewmodel.updateProgram(
                    ProgramUpdateDataInput(
                        actionType = "create",
                        apiVersion = "3.0",
                        name = "name",
                        )
                )
            }
        }
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
                         date = selectedCalendar?.get(Calendar.DATE).toString()
                         month = selectedCalendar?.getDisplayName(Calendar.MONTH, Calendar.LONG, LocaleUtils.getIDLocale()).toString()
                         year = selectedCalendar?.get(Calendar.YEAR).toString()
                        dismiss()
                    }
                }
            }
            fragmentManager?.let {
                datepickerObject.show(it, "")
            }
            datepickerObject.setOnDismissListener {
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
        fun newInstance(extras: Bundle?) = TokomemberDashCreateProgramFragment().apply {
            arguments = extras
        }

    }
}