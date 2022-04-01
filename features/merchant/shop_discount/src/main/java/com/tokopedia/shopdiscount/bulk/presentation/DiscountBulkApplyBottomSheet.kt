package com.tokopedia.shopdiscount.bulk.presentation

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.common.bottomsheet.datepicker.ShopDiscountDatePicker
import com.tokopedia.shopdiscount.databinding.BottomsheetDiscountBulkApplyBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


class DiscountBulkApplyBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_MODE = "mode"
        private const val NUMBER_PATTERN = "#,###,###"
        private const val DISCOUNT_PERCENTAGE_MAX_DIGIT = 2

        @JvmStatic
        fun newInstance(mode: Mode = Mode.SHOW_ALL_FIELDS): DiscountBulkApplyBottomSheet {
            val args = Bundle()
            args.putSerializable(BUNDLE_KEY_MODE, mode)

            val bottomSheet = DiscountBulkApplyBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private val mode by lazy { arguments?.getSerializable(BUNDLE_KEY_MODE) as? Mode }

    private var binding by autoClearedNullable<BottomsheetDiscountBulkApplyBinding>()


    enum class Mode {
        SHOW_ALL_FIELDS,
        HIDE_PERIOD_FIELDS
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DiscountBulkApplyViewModel::class.java) }

    private var onApplyClickListener: (DiscountSettings) -> Unit = {}

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
        binding = BottomsheetDiscountBulkApplyBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.sd_bulk_manage))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setMode(mode ?: return)
        setupView()
        observeBenefit()
        observeInputValidation()
        observeStartDateChange()
        observeEndDateChange()
        observeDiscountTypeChange()
        viewModel.getSlashPriceBenefit()
    }

    private fun observeBenefit() {
        viewModel.benefit.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.loader?.gone()
                    binding?.content?.visible()
                    binding?.btnApply?.visible()

                    val benefits = it.data.getSlashPriceBenefit.slashPriceBenefits
                    if (it.data.getSlashPriceBenefit.isUseVps && benefits.isNotEmpty()) {
                        //VPS
                        val vpsPackage = benefits[0]
                        val endDate = Date(vpsPackage.expiredAtUnix)
                        viewModel.setSelectedEndDate(endDate)
                        binding?.groupChipPeriod?.gone()
                        binding?.tfuEndDate?.isEnabled = false
                    } else {
                        //Membership
                        binding?.tfuEndDate?.isEnabled = true
                    }

                }
                is Fail -> {
                    binding?.loader?.gone()
                    binding?.content?.gone()
                    binding?.btnApply?.gone()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeInputValidation() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { validationState ->
            binding?.btnApply?.isEnabled =
                validationState is DiscountBulkApplyViewModel.ValidationState.Valid

            when (validationState) {
                DiscountBulkApplyViewModel.ValidationState.InvalidDiscountAmount -> {
                    showErrorMessage(
                        binding?.tfuDiscountAmount ?: return@observe,
                        getString(R.string.sd_invalid_discount_amount)
                    )
                }
                DiscountBulkApplyViewModel.ValidationState.InvalidDiscountPercentage -> {
                    showErrorMessage(
                        binding?.tfuDiscountAmount ?: return@observe,
                        getString(R.string.sd_invalid_discount_percentage)
                    )
                }
                DiscountBulkApplyViewModel.ValidationState.InvalidMaxPurchase -> {
                    binding?.root showError getString(R.string.sd_invalid_max_purchase)
                }
                DiscountBulkApplyViewModel.ValidationState.Valid -> {
                    clearErrorMessage(binding?.tfuDiscountAmount ?: return@observe)
                }
            }

        }
    }


    private fun observeStartDateChange() {
        viewModel.startDate.observe(viewLifecycleOwner) { startDate ->
            binding?.tfuStartDate?.textInputLayout?.editText?.setText(startDate.parseTo(DateConstant.DATE_MINUTE))
        }
    }

    private fun observeEndDateChange() {
        viewModel.endDate.observe(viewLifecycleOwner) { endDate ->
            binding?.tfuEndDate?.textInputLayout?.editText?.setText(endDate.parseTo(DateConstant.DATE_MINUTE))
        }
    }

    private fun observeDiscountTypeChange() {
        viewModel.discountType.observe(viewLifecycleOwner) { discountType ->
            if (discountType == DiscountType.RUPIAH) {
                binding?.tfuDiscountAmount?.setLabel(getString(R.string.sd_discount_amount))
                binding?.tfuDiscountAmount?.textInputLayout?.editText?.text = null
                binding?.tfuDiscountAmount?.appendText(EMPTY_STRING)
                binding?.tfuDiscountAmount?.prependText(getString(R.string.sd_rupiah))
                binding?.tfuDiscountAmount?.editText?.filters = arrayOf()
            } else {
                binding?.tfuDiscountAmount?.setLabel(getString(R.string.sd_discount_percentage))
                binding?.tfuDiscountAmount?.textInputLayout?.editText?.text = null
                binding?.tfuDiscountAmount?.appendText(getString(R.string.sd_percent))
                binding?.tfuDiscountAmount?.prependText(EMPTY_STRING)
                binding?.tfuDiscountAmount?.editText?.filters =
                    arrayOf(InputFilter.LengthFilter(DISCOUNT_PERCENTAGE_MAX_DIGIT))
            }
            viewModel.onDiscountAmountChanged(Int.ZERO)
        }
    }

    private fun setupView() {
        setupChipsClickListener()
        setupDiscountAmountListener()

        binding?.run {
            chipOneYearPeriod.chipType = ChipsUnify.TYPE_SELECTED
            tfuDiscountAmount.textInputLayout.errorIconDrawable = null

            if (mode == Mode.HIDE_PERIOD_FIELDS) {
                groupDiscountPeriod.gone()
            }

            setupDatePicker()
            setupQuantityEditor()

            contentSwitcher.setOnCheckedChangeListener { _, isChecked ->
                val discountType = if (isChecked) DiscountType.PERCENTAGE else DiscountType.RUPIAH
                viewModel.onDiscountTypeChanged(discountType)
            }

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

    private fun setupQuantityEditor() {
        binding?.run {
            quantityEditor.setValueChangedListener { newValue, _, _ ->
                viewModel.onMaxPurchaseQuantityChanged(newValue)
                viewModel.validateInput()
            }
            quantityEditor.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.onMaxPurchaseQuantityChanged(quantityEditor.getValue())
                    viewModel.validateInput()
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }


    private fun setupDiscountAmountListener() {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_PATTERN)

        binding?.run {
            val watcher = NumberThousandSeparatorTextWatcher(
                tfuDiscountAmount.textInputLayout.editText ?: return, numberFormatter
            ) { number, formattedNumber ->
                viewModel.onDiscountAmountChanged(number)

                tfuDiscountAmount.textInputLayout.editText?.setText(formattedNumber)
                tfuDiscountAmount.textInputLayout.editText?.setSelection(
                    tfuDiscountAmount.textInputLayout.editText?.text?.length.orZero()
                )

                viewModel.validateInput()
            }
            binding?.tfuDiscountAmount?.textInputLayout?.editText?.addTextChangedListener(watcher)
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

                viewModel.onCustomSelectionPeriodSelected()
            }
        }

    }

    private fun displayStartDateTimePicker() {
        ShopDiscountDatePicker.show(
            requireContext(),
            childFragmentManager,
            getString(R.string.sd_start_date),
            viewModel.getSelectedStartDate(),
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
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
            object : ShopDiscountDatePicker.Callback {
                override fun onDatePickerSubmitted(selectedDate: Date) {
                    viewModel.setSelectedEndDate(selectedDate)
                    binding?.chipCustomSelection?.chipType = ChipsUnify.TYPE_SELECTED
                }

            }
        )
    }

    fun setOnApplyClickListener(onApplyClickListener: (DiscountSettings) -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }

    private fun showErrorMessage(view: TextFieldUnify2, errorMessage: String) {
        view.textInputLayout.error = errorMessage
    }

    private fun clearErrorMessage(view: TextFieldUnify2) {
        view.textInputLayout.error = EMPTY_STRING
    }
}
