package com.tokopedia.shopdiscount.bulk.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.databinding.BottomsheetDiscountBulkApplyBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
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

    private val mode by lazy { arguments?.getSerializable(BUNDLE_KEY_MODE) as? Mode}

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
        setupView()
        observeBenefit()
        observeInputValidation()
        observeStartDateChange()
        observeEndDateChange()
        viewModel.getSlashPriceBenefit()
    }

    private fun observeBenefit() {
        viewModel.benefit.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {

                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeInputValidation() {
        viewModel.areInputValid.observe(viewLifecycleOwner) { isValid ->
            binding?.btnApply?.isEnabled = isValid
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


    private fun setupView() {
        setupChipsClickListener()
        setupDiscountAmountListener()

        binding?.run {
            chipOneYearPeriod.chipType = ChipsUnify.TYPE_SELECTED
            contentSwitcher.setOnCheckedChangeListener { _, isChecked ->
                val discountType = if (isChecked) DiscountType.PERCENTAGE else DiscountType.RUPIAH
                viewModel.onDiscountTypeChanged(discountType)
            }
            quantityEditor.setValueChangedListener { newValue, oldValue, isOver ->
                viewModel.onMaxPurchaseQuantityChanged(newValue)
                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(mode ?: return@setValueChangedListener, discountSettings)
            }
            btnApply.setOnClickListener {
                val currentSelection = viewModel.getCurrentSelection()
                onApplyClickListener(currentSelection)
                dismiss()
            }
        }
    }

    private fun setupDiscountAmountListener() {
        val pattern = "#,###,###"
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(pattern)

        binding?.run {
            val watcher = NumberThousandSeparatorTextWatcher(
                tfuDiscountAmount.textInputLayout.editText ?: return, numberFormatter
            ) { number, formattedNumber ->
                viewModel.onDiscountAmountChanged(number)

                tfuDiscountAmount.textInputLayout.editText?.setText(formattedNumber)
                tfuDiscountAmount.textInputLayout.editText?.setSelection(
                    tfuDiscountAmount.textInputLayout.editText?.text?.length.orZero()
                )

                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(
                    mode ?: return@NumberThousandSeparatorTextWatcher,
                    discountSettings
                )
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

                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(mode ?: return@setOnClickListener, discountSettings)
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

                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(mode ?: return@setOnClickListener, discountSettings)
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

                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(mode ?: return@setOnClickListener, discountSettings)
            }
        }

    }

    private fun setupCustomSelectionPeriodChipListener() {
        binding?.run {
            chipCustomSelection.selectedChangeListener = { isActive ->
                if (isActive) {
                    viewModel.onCustomSelectionPeriodSelected(Date(), Date())
                }
            }

            chipCustomSelection.chip_container.setOnClickListener {
                chipOneYearPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipSixMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipOneMonthPeriod.chipType = ChipsUnify.TYPE_NORMAL
                chipCustomSelection.chipType = ChipsUnify.TYPE_SELECTED

                val discountSettings = viewModel.getCurrentSelection()
                viewModel.validateInput(mode ?: return@setOnClickListener, discountSettings)
            }
        }

    }

    fun setOnApplyClickListener(onApplyClickListener: (DiscountSettings) -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }
}
