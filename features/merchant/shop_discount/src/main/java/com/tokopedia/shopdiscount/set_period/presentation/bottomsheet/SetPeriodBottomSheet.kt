package com.tokopedia.shopdiscount.set_period.presentation.bottomsheet

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.common.bottomsheet.datepicker.ShopDiscountDatePicker
import com.tokopedia.shopdiscount.databinding.BottomsheetSetPeriodBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.set_period.presentation.viewmodel.SetPeriodBottomSheetViewModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.toDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject


class SetPeriodBottomSheet : BottomSheetUnify() {

    companion object {
        private const val START_DATE_ARG = "START_DATE_ARG"
        private const val END_DATE_ARG = "END_DATE_ARG"
        private const val MODE_ARG = "MODE_ARG"

        fun newInstance(
            startDateUnix: Long,
            endDateUnix: Long,
            mode: String
        ): SetPeriodBottomSheet {
            return SetPeriodBottomSheet().apply {
                val args = Bundle()
                args.putLong(START_DATE_ARG, startDateUnix)
                args.putLong(END_DATE_ARG, endDateUnix)
                args.putString(MODE_ARG, mode)
                arguments = args
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetSetPeriodBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SetPeriodBottomSheetViewModel::class.java) }

    private var onApplyClickListener: (SetPeriodResultUiModel) -> Unit = {}
    private var startDateUnix: Long = 0
    private var endDateUnix: Long = 0
    private var mode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetSetPeriodBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.shop_discount_set_period_bottomsheet_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        setupView()
        observeBenefit()
        observeStartDateChange()
        observeEndDateChange()
        viewModel.getSlashPriceBenefit()
    }

    private fun getArgumentData() {
        arguments?.let {
            startDateUnix = it.getLong(START_DATE_ARG).orZero()
            endDateUnix = it.getLong(END_DATE_ARG).orZero()
            mode = it.getString(MODE_ARG).orEmpty()
        }
    }

    private fun observeBenefit() {
        viewModel.benefit.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    configDatePeriod(it.data)
                    showScreenContent()
                }
                is Fail -> {
                    hideScreenContent()
                    binding?.loader?.gone()
                    binding?.content?.gone()
                    binding?.btnApply?.gone()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun configDatePeriod(data: GetSlashPriceBenefitResponse) {
        when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                val benefits = data.getSlashPriceBenefit.slashPriceBenefits
                val isUsingVps = data.getSlashPriceBenefit.isUseVps
                if (benefits.isNotEmpty()) {
                    val startDate: Date
                    val endDate: Date
                    if (isUsingVps) {
                        hideAllChips()
                        val benefit = benefits.firstOrNull {
                            it.packageId.toIntOrZero() != -1
                        }
                        if (startDateUnix > 0 && endDateUnix > 0) {
                            startDate = Date(startDateUnix)
                            endDate = Date(endDateUnix)
                        } else {
                            startDate = viewModel.defaultStartDate
                            endDate = benefit?.expiredAt?.toDate(DateConstant.DATE_TIME) ?: Date()
                        }
                    } else {
                        if (startDateUnix > 0 && endDateUnix > 0) {
                            startDate = Date(startDateUnix)
                            endDate = Date(endDateUnix)
                        } else {
                            startDate = viewModel.defaultStartDate
                            endDate = viewModel.defaultMembershipEndDate
                            binding?.chipOneYearPeriod?.chipType = ChipsUnify.TYPE_SELECTED
                        }
                    }
                    viewModel.setSelectedStartDate(startDate)
                    viewModel.setSelectedEndDate(endDate)
                }
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                hideAllChips()
                binding?.tfuStartDate?.isEnabled = false
                viewModel.setSelectedStartDate(Date(startDateUnix))
                viewModel.setSelectedEndDate(Date(endDateUnix))
            }
        }
    }

    private fun observeStartDateChange() {
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
            binding?.tfuStartDate?.textInputLayout?.editText?.setText(startDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION))
        }
    }

    private fun observeEndDateChange() {
        viewModel.endDate.observe(viewLifecycleOwner) { endDate ->
            binding?.tfuEndDate?.textInputLayout?.editText?.setText(endDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION))
        }
    }

    private fun setupView() {
        setupChipsClickListener()
        binding?.run {
            setupDatePicker()
            btnApply.setOnClickListener {
                val currentSelection = viewModel.getCurrentSelection()
                onApplyClickListener(currentSelection)
                dismiss()
            }
        }
    }

    private fun setupDatePicker() {
        binding?.run {
            tfuStartDate.editText.inputType = InputType.TYPE_NULL
            tfuStartDate.editText.setOnClickListener {
                displayStartDateTimePicker()
            }

            tfuEndDate.editText.inputType = InputType.TYPE_NULL
            tfuEndDate.editText.setOnClickListener {
                displayEndDateTimePicker()
            }
        }
    }

    private fun setupChipsClickListener() {
        setupOneYearAheadPeriodChipListener()
        setupSixMonthPeriodChipListener()
        setupOneMonthPeriodChipListener()
        setupCustomSelectionPeriodChipListener()
    }

    private fun setupOneYearAheadPeriodChipListener() {
        binding?.run {
            chipOneYearPeriod.selectedChangeListener = { isActive ->
                if (isActive) {
                    viewModel.onOneYearPeriodSelected()
                }
            }

            chipOneYearPeriod.chip_container.setOnClickListener {
                chipOneYearPeriod.chipType = ChipsUnify.TYPE_SELECTED
                chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipCustomSelection.chipType = ChipsUnify.TYPE_NORMAL
            }
        }

    }

    private fun setupSixMonthPeriodChipListener() {
        binding?.run {
            chipSixMonthPeriod.selectedChangeListener = { isActive ->
                if (isActive) {
                    viewModel.onSixMonthPeriodSelected()
                }
            }

            chipSixMonthPeriod.chip_container.setOnClickListener {
                chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipSixMonthPeriod.chipType = ChipsUnify.TYPE_SELECTED
                chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipCustomSelection.chipType = ChipsUnify.TYPE_NORMAL
            }
        }

    }

    private fun setupOneMonthPeriodChipListener() {
        binding?.run {
            chipOneMonthPeriod.selectedChangeListener = { isActive ->
                if (isActive) {
                    viewModel.onOneMonthPeriodSelected()
                }
            }

            chipOneMonthPeriod.chip_container.setOnClickListener {
                chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipOneMonthPeriod.chipType = ChipsUnify.TYPE_SELECTED
                chipCustomSelection.chipType = ChipsUnify.TYPE_NORMAL
            }
        }

    }

    private fun setupCustomSelectionPeriodChipListener() {
        binding?.run {
            chipCustomSelection.selectedChangeListener = { isActive ->
                if (isActive) {
                    chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
                    chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                    chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                }
            }

            chipCustomSelection.chip_container.setOnClickListener {
                chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipCustomSelection.chipType = ChipsUnify.TYPE_SELECTED
            }
        }

    }

    private fun displayStartDateTimePicker() {
        ShopDiscountDatePicker.show(
            requireContext(),
            childFragmentManager,
            getString(R.string.sd_start_date),
            Date(),
            viewModel.defaultStartDate,
            viewModel.getSelectedEndDate(),
            viewModel.getBenefitPackageName(),
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
                    selectCustomRangeChip()
                    viewModel.setSelectedStartDate(selectedDate)
                    binding?.chipCustomSelection?.chipType = ChipsUnify.TYPE_SELECTED
                }
            }
        )
    }

    private fun displayEndDateTimePicker() {
        ShopDiscountDatePicker.show(
            requireContext(),
            childFragmentManager,
            getString(R.string.sd_end_date),
            viewModel.getSelectedEndDate(),
            viewModel.defaultStartDate,
            viewModel.getSelectedEndDate(),
            viewModel.getBenefitPackageName(),
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
                    selectCustomRangeChip()
                    viewModel.setSelectedEndDate(selectedDate)
                    binding?.chipCustomSelection?.chipType = ChipsUnify.TYPE_SELECTED
                }

            }
        )
    }

    private fun selectCustomRangeChip() {
        binding?.chipCustomSelection?.isSelected = true
    }

    fun setOnApplyClickListener(onApplyClickListener: (SetPeriodResultUiModel) -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }

    private fun showScreenContent() {
        binding?.loader?.gone()
        binding?.content?.visible()
        binding?.btnApply?.visible()
    }

    private fun hideScreenContent() {
        binding?.loader?.gone()
        binding?.content?.visible()
        binding?.btnApply?.visible()
    }

    private fun hideAllChips() {
        binding?.chipOneYearPeriod?.gone()
        binding?.chipSixMonthPeriod?.gone()
        binding?.chipOneMonthPeriod?.gone()
        binding?.chipCustomSelection?.gone()
    }

}
