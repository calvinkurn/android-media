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
import com.tokopedia.shopdiscount.databinding.BottomsheetDiscountBulkApplyBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.extension.digitsOnly
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

    private var onApplyClickListener: () -> Unit = {}

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
        binding?.run {
            chipOneYearPeriod.chipType = ChipsUnify.TYPE_SELECTED
            btnApply.setOnClickListener {  }
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

                val discountSettings = getCurrentSelection()
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

                val discountSettings = getCurrentSelection()
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

                val discountSettings = getCurrentSelection()
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

                val discountSettings = getCurrentSelection()
                viewModel.validateInput(mode ?: return@setOnClickListener, discountSettings)
            }
        }

    }

    private fun getCurrentSelection() : DiscountSettings {
        val discountAmount = binding?.tfuDiscountAmount?.textInputLayout?.editText?.text.toString().trim().digitsOnly()
        val maxPurchaseQuantity = binding?.quantityEditor?.getValue().orZero()
        return DiscountSettings(Date(), Date(), discountAmount, maxPurchaseQuantity)
    }


    fun setOnApplyClickListener(onApplyClickListener: () -> Unit) {
        this.onApplyClickListener = onApplyClickListener
    }
}
