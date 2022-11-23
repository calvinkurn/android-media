package com.tokopedia.privacycenter.dsar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.BottomSheetRangePickerBinding
import com.tokopedia.privacycenter.databinding.FragmentDsarLayoutBinding
import com.tokopedia.privacycenter.databinding.ItemTransactionHistoryRangeBinding
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import com.tokopedia.privacycenter.dsar.viewmodel.DsarViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class DsarFragment: BaseDaggerFragment(), OnDateChangedListener {

    private var binding by autoClearedNullable<FragmentDsarLayoutBinding>()

    override fun getScreenName(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    var state = STATE_START

    var startDate: Long = 0L
    var endDate: Long = 0L

    lateinit var rangePickerDialogBinding: BottomSheetRangePickerBinding
    lateinit var rangePickerBottomSheet: BottomSheetUnify
    lateinit var datePicker: DateTimePickerUnify

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            DsarViewModel::class.java
        )
    }

    override fun initInjector() {
        getComponent(PrivacyCenterComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDsarLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel._itemRangeData.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                if(::rangePickerBottomSheet.isInitialized && rangePickerBottomSheet.isVisible) {
                    updateRangeItemsView(it)
                } else {
                    showTransactionHistoryBtmSheet(it)
                }
            }
        }

        viewModel._startDate.observe(viewLifecycleOwner) {
            if (::rangePickerDialogBinding.isInitialized) {
                rangePickerDialogBinding.txtStartDate.editText
                    .setText(it)
            }
        }

        viewModel._endDate.observe(viewLifecycleOwner) {
            if (::rangePickerDialogBinding.isInitialized) {
                rangePickerDialogBinding.txtEndDate.editText.setText(it)
            }
        }
    }

    private fun updateRangeItemsView(items: List<ItemRangeModel>) {
        rangePickerDialogBinding.txtStartDate.editText.setText("")
        rangePickerDialogBinding.txtEndDate.editText.setText("")

        if(items.count() == rangePickerDialogBinding.layoutDateRange.childCount) {
            items.forEach {
                val view = rangePickerDialogBinding.layoutDateRange.getChildAt(it.id)
                val radio = view.findViewById<RadioButtonUnify>(R.id.radioTransactionHistoryItem)
                if(radio is RadioButtonUnify) {
                    radio.isChecked = it.selected
                }
            }
        }
    }

    private fun setupViews() {
        binding?.personalInfo?.apply {
            txtProfileName.text = userSession.name
            txtProfileEmail.text = userSession.email
            txtProfilePhone.text = userSession.phoneNumber
            imgProfilePicture.loadImage(userSession.profilePicture)
        }
        binding?.itemPersonalInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if (!it.isActivated) {
                    checkIcon.invisible()
                    viewModel.removeFilter("personal")
                } else {
                    viewModel.addFilter("personal")
                    checkIcon.visible()
                }
            }
        }
        binding?.itemPaymentInfo?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if(!it.isActivated) {
                    viewModel.removeFilter("payment")
                    checkIcon.invisible()
                } else {
                    viewModel.addFilter("payment")
                    checkIcon.visible()
                }
            }
        }
        binding?.itemTransactionHistory?.apply {
            mainLayout.setOnClickListener {
                it.isActivated = !it.isActivated
                if(!it.isActivated) {
                    viewModel.removeFilter("transaction")
                    checkIcon.invisible()
                } else {
                    viewModel.addFilter("transaction")
                    viewModel.populateRangeItems()
                    checkIcon.visible()
                }
            }
        }
        binding?.btnNext?.setOnClickListener {
            viewModel.showSummary()
        }
    }

    override fun onDateChanged(date: Long) {}

    private fun renderEndState() {
        datePicker.setTitle("Sampai")
    }

    private fun renderInitialState() {
        datePicker.setTitle("Mulai dari")
    }

    private fun showDatePicker() {
        val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext())).apply {
            add(Calendar.YEAR, -15)
        }

        val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(requireContext()))
        val maxDate = defaultDate

        context.let {
            datePicker = DateTimePickerUnify(
                requireContext(),
                minDate,
                defaultDate,
                maxDate,
                this,
                DateTimePickerUnify.TYPE_DATEPICKER
            )

            datePicker.apply {
                setCloseClickListener { dismiss() }
                datePickerButton.text = "Pilih"
                datePickerButton.setOnClickListener {
                    onDateSelected()
                }
            }

            renderInitialState()
            datePicker.show(parentFragmentManager, "DateTimePicker Show")
        }
    }

    private fun onDateSelected() {
        val selected = datePicker.getDate()

        if(state == STATE_START) {
            viewModel.setStartDate(selected.time)
            state = STATE_END
            startDate = selected.time.time
            renderEndState()
        } else {
            viewModel.setEndDate(selected.time)
            endDate = selected.time.time
            state = STATE_START
            renderInitialState()
            datePicker.dismiss()
        }
    }

    private fun onCheckedChanged(id: Int, isChecked: Boolean) {
        if(isChecked) {
            viewModel.setSelectedRangeItems(id)
            if(id == 5) {
                showDatePicker()
                rangePickerDialogBinding.layoutCustomDate.visible()
            } else {
                rangePickerDialogBinding.layoutCustomDate.gone()
            }
        }
    }

    private fun showTransactionHistoryBtmSheet(itemRange: ArrayList<ItemRangeModel>) {
        if(!::rangePickerDialogBinding.isInitialized) {
            rangePickerDialogBinding = BottomSheetRangePickerBinding.inflate(LayoutInflater.from(activity))
        }

        if(!::rangePickerBottomSheet.isInitialized) {
            rangePickerBottomSheet = BottomSheetUnify()

            itemRange.forEachIndexed { index, itemRangeModel ->
                val binding = ItemTransactionHistoryRangeBinding.inflate(LayoutInflater.from(requireContext()))
                binding.root.layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                binding.mainTextTransactionHistoryItem.text = itemRangeModel.title
                binding.radioTransactionHistoryItem.setOnCheckedChangeListener { _, isChecked -> onCheckedChanged(itemRangeModel.id, isChecked) }
                rangePickerDialogBinding.layoutDateRange.addView(binding.root)
            }
            rangePickerBottomSheet.setChild(rangePickerDialogBinding.root)
            rangePickerDialogBinding.btnApplyFilter.setOnClickListener {
                viewModel.applyTransactionHistoryFilter()
            }
        }
        rangePickerBottomSheet.show(parentFragmentManager, "")
    }

    companion object {
        const val STATE_START = 0
        const val STATE_END = 1

        fun newInstance() = DsarFragment()
    }
}
