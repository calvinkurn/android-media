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
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.SetPeriodBottomSheetChipsState
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.toDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Date
import javax.inject.Inject

class SetPeriodBottomSheet : BottomSheetUnify() {

    companion object {
        private const val START_DATE_ARG = "START_DATE_ARG"
        private const val END_DATE_ARG = "END_DATE_ARG"
        private const val SLASH_PRICE_STATUS_ID_ARG = "SLASH_PRICE_STATUS_ID_ARG"
        private const val SELECTED_PERIOD_CHIP = "SELECTED_PERIOD_CHIP"
        private const val MODE = "MODE"

        fun newInstance(
            startDateUnix: Long,
            endDateUnix: Long,
            slashPriceStatusId: String,
            selectedPeriodChip: Int,
            mode: String
        ): SetPeriodBottomSheet {
            return SetPeriodBottomSheet().apply {
                val args = Bundle()
                args.putLong(START_DATE_ARG, startDateUnix)
                args.putLong(END_DATE_ARG, endDateUnix)
                args.putString(SLASH_PRICE_STATUS_ID_ARG, slashPriceStatusId)
                args.putInt(SELECTED_PERIOD_CHIP, selectedPeriodChip)
                args.putString(MODE, mode)
                arguments = args
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetSetPeriodBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SetPeriodBottomSheetViewModel::class.java) }

    private var onApplyClickListener: (SetPeriodResultUiModel, Int) -> Unit = { _, _ -> }
    private var startDateUnix: Long = 0
    private var endDateUnix: Long = 0
    private var slashPriceStatusId: String = ""
    private var selectedPeriodChip: Int = 0
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
            slashPriceStatusId = it.getString(SLASH_PRICE_STATUS_ID_ARG).orEmpty()
            selectedPeriodChip = it.getInt(SELECTED_PERIOD_CHIP).orZero()
            mode = it.getString(MODE).orEmpty()
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
        val benefits = data.getSlashPriceBenefit.slashPriceBenefits
        val isUsingVps = data.getSlashPriceBenefit.isUseVps
        if (benefits.isNotEmpty()) {
            val startDate: Date
            val endDate: Date
            val maxDate: Date = if (isUsingVps) {
                hideAllChips()
                val benefit = benefits.firstOrNull {
                    it.packageId.toIntOrZero() != -1
                }
                viewModel.setBenefitPackageName(benefit?.packageName.orEmpty())
                benefit?.expiredAt?.toDate(DateConstant.DATE_TIME) ?: Date()
            } else {
                viewModel.defaultMembershipEndDate
            }
            if (startDateUnix > 0 && endDateUnix > 0) {
                startDate = Date(startDateUnix)
                endDate = Date(endDateUnix)
            } else {
                startDate = viewModel.defaultStartDate
                endDate = maxDate
            }
            when (mode) {
                ShopDiscountManageDiscountMode.UPDATE -> {
                    hideAllChips()
                }
            }
            when (slashPriceStatusId.toIntOrZero()) {
                DiscountStatus.ONGOING, DiscountStatus.PAUSED -> {
                    binding?.tfuStartDate?.isEnabled = false
                }
            }
            viewModel.setSelectedStartDate(startDate)
            viewModel.setSelectedEndDate(endDate)
            viewModel.setMaxDate(maxDate)
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
        setupSelectedPeriodChip()
        setupChipsClickListener()
        binding?.run {
            setupDatePicker()
            btnApply.setOnClickListener {
                val currentSelection = viewModel.getCurrentSelection()
                val selectedChip = getSelectedChip()
                onApplyClickListener(currentSelection, selectedChip)
                dismiss()
            }
        }
    }

    private fun setupSelectedPeriodChip() {
        binding?.let {
            resetChipType()
            when (selectedPeriodChip) {
                SetPeriodBottomSheetChipsState.ONE_YEAR -> {
                    selectChip(it.chipOneYearPeriod)
                }
                SetPeriodBottomSheetChipsState.SIX_MONTH -> {
                    selectChip(it.chipSixMonthPeriod)
                }
                SetPeriodBottomSheetChipsState.ONE_MONTH -> {
                    selectChip(it.chipOneMonthPeriod)
                }
                SetPeriodBottomSheetChipsState.CUSTOM -> {
                    selectChip(it.chipCustomSelection)
                }
                else -> {
                    selectChip(it.chipOneYearPeriod)
                }
            }
        }
    }

    private fun selectChip(chipUnify: ChipsUnify?) {
        chipUnify?.chipType = ChipsUnify.TYPE_SELECTED
    }

    private fun getSelectedChip(): Int {
        return binding?.let {
            when {
                it.chipOneYearPeriod.chipType == ChipsUnify.TYPE_SELECTED -> {
                    SetPeriodBottomSheetChipsState.ONE_YEAR
                }
                it.chipSixMonthPeriod.chipType == ChipsUnify.TYPE_SELECTED -> {
                    SetPeriodBottomSheetChipsState.SIX_MONTH
                }
                it.chipOneMonthPeriod.chipType == ChipsUnify.TYPE_SELECTED -> {
                    SetPeriodBottomSheetChipsState.ONE_MONTH
                }
                else -> {
                    SetPeriodBottomSheetChipsState.CUSTOM
                }
            }
        }.orZero()
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
                resetChipType()
                selectChip(chipOneYearPeriod)
            }
        }
    }

    private fun resetChipType() {
        binding?.apply {
            chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
            chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
            chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
            chipCustomSelection.chipType = ChipsUnify.TYPE_NORMAL
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
                resetChipType()
                selectChip(chipSixMonthPeriod)
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
                resetChipType()
                selectChip(chipOneMonthPeriod)
            }
        }
    }

    private fun setupCustomSelectionPeriodChipListener() {
        binding?.run {
            chipCustomSelection.chip_container.setOnClickListener {
                resetChipType()
                selectChip(chipCustomSelection)
            }
        }
    }

    private fun displayStartDateTimePicker() {
        ShopDiscountDatePicker.show(
            requireContext(),
            childFragmentManager,
            getString(R.string.sd_start_date),
            viewModel.getSelectedStartDate(),
            viewModel.defaultStartDate,
            viewModel.getMaxDate(),
            viewModel.getBenefitPackageName(),
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
                    viewModel.setSelectedStartDate(selectedDate)
                    resetChipType()
                    selectChip(binding?.chipCustomSelection)
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
            viewModel.getMaxDate(),
            viewModel.getBenefitPackageName(),
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
                    viewModel.setSelectedEndDate(selectedDate)
                    resetChipType()
                    selectChip(binding?.chipCustomSelection)
                }
            }
        )
    }

    fun setOnApplyClickListener(onApplyClickListener: (SetPeriodResultUiModel, Int) -> Unit) {
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
