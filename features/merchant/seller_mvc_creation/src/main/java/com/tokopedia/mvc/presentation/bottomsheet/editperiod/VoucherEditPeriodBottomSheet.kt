package com.tokopedia.mvc.presentation.bottomsheet.editperiod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.VoucherEditPeriodViewModel
import com.tokopedia.mvc.util.DateTimeUtils
import com.tokopedia.mvc.util.convertUnsafeDateTime
import com.tokopedia.mvc.util.formatTo
import com.tokopedia.unifycomponents.BottomSheetUnify
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

    private val viewModel: VoucherEditPeriodViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(VoucherEditPeriodViewModel::class.java)
    }

    private val locale by lazy {
        LocaleUtils.getIDLocale()
    }

    private var startCalendar: GregorianCalendar? = null
    private var endCalendar: GregorianCalendar? = null
    private var startHour: Int = 0
    private var startMinute: Int = 0
    private var endHour: Int = 0
    private var endMinute: Int = 0
    private var onSuccessListener: () -> Unit = {}
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
        setTitle(context?.getString(R.string.edit_period_title) ?: "")
        initInjector()
        setUpDate()

        initObservers()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        binding?.btnMvcSavePeriod?.setOnClickListener {
            viewModel.validateAndUpdateDateTime(voucher)
            binding?.btnMvcSavePeriod?.isLoading = true
        }
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setUpDate() {
        voucher?.let {
            startCalendar = getGregorianDate(it.startTime)
            viewModel.setStartDateTime(startCalendar)
        }
        voucher?.let {
            endCalendar = getGregorianDate(it.finishTime)
            viewModel.setEndDateTime(endCalendar)
        }
    }

    private fun String.convertDate(): String {
        return this.convertUnsafeDateTime().formatTo(FULL_DAY_FORMAT, locale).toBlankOrString()
    }

    private fun getGregorianDate(date: String): GregorianCalendar {
        return GregorianCalendar().apply {
            time = date.convertUnsafeDateTime()
        }
    }

    private fun initObservers() {
        viewModel.startDateCalendarLiveData.observe(viewLifecycleOwner) {
            binding?.edtMvcStartDate?.setPlaceholder(it.time.formatTo(locale))
        }
        viewModel.endDateCalendarLiveData.observe(viewLifecycleOwner) {
            binding?.edtMvcEndDate?.setPlaceholder(it.time.formatTo(locale))
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
                var timeString = it.split(":").toTypedArray()
                if (timeString.size >= 2) {
                    endHour = timeString[0].toIntOrZero()
                    endMinute = timeString[1].toIntOrZero()
                }
            } catch (_: Exception) {
            }
        }

        viewModel.updateVoucherPeriodStateLiveData.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    onSuccessListener()
                }
                is Fail -> {
                    onFailListener(result.throwable.message.toBlankOrString())
                }
            }
            binding?.btnMvcSavePeriod?.isLoading = false
            dismiss()
        }
    }

    private fun setUpView() {
        binding?.apply {
            voucher?.let {
                edtMvcStartDate.run {
                    editText.run {
                        setOnClickListener {
                            context?.run {
                                DateTimeUtils.getMinDate(startCalendar)?.let { minDate ->
                                    DateTimeUtils.getMaxDate(startCalendar)?.let { maxDate ->
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
                        labelText.text = context?.getString(R.string.edit_period_start_date).toBlankOrString()
                        disableText(this)
                    }
                }

                edtMvcEndDate.run {
                    editText.run {
                        setOnClickListener {
                            context?.run {
                                DateTimeUtils.getMinDate(endCalendar)?.let { minDate ->
                                    DateTimeUtils.getMaxDate(endCalendar)?.let { maxDate ->
                                        voucherEditCalendarBottomSheet =
                                            VoucherEditCalendarBottomSheet.newInstance(
                                                endCalendar,
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
                        labelText.text = context?.getString(R.string.edit_period_end_date).toBlankOrString()
                        disableText(this)
                    }
                }
            }
        }

        setAction(context?.getString(R.string.edit_period_reset).toBlankOrString()) {
            setUpDate()
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

    companion object {
        @JvmStatic
        fun newInstance(voucher: Voucher, onSuccessListener: ()-> Unit = {}, onFailListener: (String) -> Unit = {}): VoucherEditPeriodBottomSheet {
            return VoucherEditPeriodBottomSheet().apply {
                this.voucher = voucher
                this.onSuccessListener = onSuccessListener
                this.onFailListener = onFailListener
            }
        }

        private const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm"
        const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }
}
