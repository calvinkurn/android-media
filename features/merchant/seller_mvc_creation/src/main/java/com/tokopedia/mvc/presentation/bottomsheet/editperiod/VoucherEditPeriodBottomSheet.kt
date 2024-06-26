package com.tokopedia.mvc.presentation.bottomsheet.editperiod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.data.mapper.UpdateVoucherMapper
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.VoucherEditPeriodViewModel
import com.tokopedia.mvc.util.DateTimeUtils
import com.tokopedia.mvc.util.decideCalendarPeriodEndDate
import com.tokopedia.mvc.util.decideCalendarPeriodStartDate
import com.tokopedia.mvc.util.formatTo
import com.tokopedia.mvc.util.getGregorianDate
import com.tokopedia.mvc.util.tracker.ChangePeriodTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class VoucherEditPeriodBottomSheet : BottomSheetUnify() {

    private var voucher: Voucher? = null

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodBinding>()

    private var voucherEditCalendarBottomSheet: VoucherEditCalendarBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mapper: UpdateVoucherMapper

    @Inject
    lateinit var tracker: ChangePeriodTracker

    private val viewModel: VoucherEditPeriodViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(VoucherEditPeriodViewModel::class.java)
    }

    private var startCalendar: GregorianCalendar? = null
    private var endCalendar: GregorianCalendar? = null
    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var endHour: Int = 0
    private var endMinute: Int = 0
    private var tickerVisibility: Boolean = false
    private var onSuccessListener: (Voucher?) -> Unit = {}
    private var onFailListener: (String) -> Unit = {}

    init {
        isFullpage = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetEditPeriodBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.getString(R.string.edit_period_title).toBlankOrString())
        initInjector()
        setUpDate()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        binding?.btnMvcSavePeriod?.setOnClickListener {
            voucher?.let {
                viewModel.validateAndUpdateDateTime(it)
                binding?.btnMvcSavePeriod?.isLoading = true
                clearDismissListener()
                tracker.sendClickOkEvent(createLabelTracker(it))
            }
        }
        initObservers()
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    private fun setUpDate() {
        voucher?.let {
            endCalendar = getGregorianDate(it.finishTime)
            viewModel.setEndDateTime(endCalendar)
        }
        voucher?.let {
            startCalendar = getGregorianDate(it.startTime)
            viewModel.setStartDateTime(startCalendar)
        }
    }

    private fun initObservers() {
        viewModel.startDateCalendarLiveData.observe(viewLifecycleOwner) {
            startCalendar = it as? GregorianCalendar
            binding?.edtMvcStartDate?.setPlaceholder(it.time.formatTo())
        }
        viewModel.endDateCalendarLiveData.observe(viewLifecycleOwner) {
            endCalendar = it as? GregorianCalendar
            binding?.edtMvcEndDate?.setPlaceholder(it.time.formatTo())
        }
        viewModel.hourStartLiveData.observe(viewLifecycleOwner) {
            try {
                val timeString = it.split(":").toTypedArray()
                if (timeString.size >= 2) {
                    startHour = timeString[0].toIntOrZero()
                    startMinute = timeString[1].toIntOrZero()
                }
            } catch (_: Exception) {
            }
        }

        viewModel.hourEndLiveData.observe(viewLifecycleOwner) {
            try {
                val timeString = it.split(":").toTypedArray()
                if (timeString.size >= 2) {
                    endHour = timeString[0].toIntOrZero()
                    endMinute = timeString[1].toIntOrZero()
                }
            } catch (_: Exception) {
            }
        }

        viewModel.updateVoucherPeriodStateLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    onSuccessListener(voucher)
                }
                is Fail -> {
                    onFailListener(result.throwable.message.toBlankOrString())
                }
            }
            binding?.btnMvcSavePeriod?.isLoading = false
            dismiss()
        }

        viewModel.toShowDateToaster.observe(viewLifecycleOwner) { result ->
            if (result) {
                binding?.root?.rootView?.let { view ->
                    Toaster.build(
                        view,
                        getString(R.string.edit_period_date_picker_end_date_warning).toBlankOrString(),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        getString(R.string.smvc_ok).toBlankOrString()
                    ).show()
                }
            }
        }
    }

    private fun setUpView() {
        setDismissListener()
        binding?.apply {
            voucher?.let {
                edtMvcStartDate.run {
                    editText.setOnClickListener {
                        onClickListenerForStartDate()
                        voucher?.let {
                            tracker.sendClickStartEndDateEvent(createLabelTracker(it))
                        }
                    }
                    labelText.text =
                        context?.getString(R.string.edit_period_start_date).toBlankOrString()
                    disableText(editText)
                }

                edtMvcEndDate.run {
                    editText.setOnClickListener {
                        onClickListenerForEndDate()
                        voucher?.let {
                            tracker.sendClickStartEndDateEvent(createLabelTracker(it))
                        }
                    }
                    labelText.text =
                        context?.getString(R.string.edit_period_end_date).toBlankOrString()
                    disableText(editText)
                }
            }

            if (tickerVisibility) {
                informationTicker.show()
            } else {
                informationTicker.hide()
            }
        }

        setAction(context?.getString(R.string.edit_period_reset).toBlankOrString()) {
            setUpDate()
            voucher?.let {
                tracker.sendClickResetEvent(createLabelTracker(it))
            }
        }
    }

    private fun setDismissListener() {
        this.setOnDismissListener {
            voucher?.let {
                tracker.sendClickCloseEvent(createLabelTracker(it))
            }
        }
    }

    private fun clearDismissListener() {
        this.setOnDismissListener { }
    }
    private fun onClickListenerForStartDate() {
        context?.run {
            decideCalendarPeriodStartDate(this, startCalendar)?.let { minDate ->
                DateTimeUtils.getMaxDate(minDate)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            startCalendar,
                            minDate,
                            maxDate,
                            startHour,
                            startMinute,
                            getSelectedDateStarting
                        )
                    voucherEditCalendarBottomSheet?.show(
                        childFragmentManager,
                        ""
                    )
                }
            }
        }
    }

    private fun onClickListenerForEndDate() {
        context?.run {
            startCalendar?.let { minDate ->
                DateTimeUtils.getMaxDate(startCalendar)?.let { maxDate ->
                    voucherEditCalendarBottomSheet =
                        VoucherEditCalendarBottomSheet.newInstance(
                            decideCalendarPeriodEndDate(startCalendar, endCalendar),
                            minDate,
                            maxDate,
                            endHour,
                            endMinute,
                            getSelectedDateEnding
                        )
                    voucherEditCalendarBottomSheet?.show(childFragmentManager, "")
                }
            }
        }
    }

    private var getSelectedDateStarting: (Calendar) -> Unit = {
        viewModel.setStartDateTime(it)
    }

    private var getSelectedDateEnding: (Calendar) -> Unit = {
        viewModel.setEndDateTime(it)
    }

    private fun disableText(autoCompleteTextView: AutoCompleteTextView) {
        autoCompleteTextView.apply {
            isFocusable = false
            isClickable = true
            keyListener = null
        }
    }

    private fun createLabelTracker(voucher: Voucher): String {
        return getString(
            R.string.smvc_tracker_change_pariod_lable,
            voucher.id.toString(),
            voucher.status.name
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(
            voucher: Voucher,
            onSuccessListener: (Voucher?) -> Unit = {},
            onFailListener: (String) -> Unit = {},
            tickerVisibility: Boolean = false
        ): VoucherEditPeriodBottomSheet {
            return VoucherEditPeriodBottomSheet().apply {
                this.voucher = voucher
                this.onSuccessListener = onSuccessListener
                this.onFailListener = onFailListener
                this.tickerVisibility = tickerVisibility
            }
        }
    }
}
