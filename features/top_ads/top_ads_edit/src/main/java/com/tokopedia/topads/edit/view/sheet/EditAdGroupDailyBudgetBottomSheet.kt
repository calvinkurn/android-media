package com.tokopedia.topads.edit.view.sheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_2
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.view.sheet.CreatePotentialPerformanceSheet
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupDailyBudgetBinding
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants.MAX_BUDGET_AUTOMATIC
import com.tokopedia.topads.edit.utils.Constants.MAX_BUDGET_MANUAL
import com.tokopedia.topads.edit.utils.Constants.MULTIPLIER
import com.tokopedia.topads.edit.viewmodel.EditAdGroupDailyBudgetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.edit.R as topadseditR
import com.tokopedia.topads.common.R as topadscommonR

class EditAdGroupDailyBudgetBottomSheet : BottomSheetUnify() {

    private var performanceData: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>? = null
    private var isBidAutomatic: Boolean = false
    private var minBudget: Int = Int.ZERO
    private var maxBudget: Int = Int.ZERO
    private var bids: MutableList<Float?> = mutableListOf()
    private var productIds: List<String> = mutableListOf()
    private var suggestedDailyBudget: String = Int.ZERO.toString()
    private var dailyBudget: String = Int.ZERO.toString()
    private var clickListener: ((dailyBudget: String, isToggleOn: Boolean) -> Unit)? = null
    private var binding: TopadsEditSheetEditAdGroupDailyBudgetBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditAdGroupDailyBudgetViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        DaggerTopAdsEditComponent.builder().baseAppComponent(((activity as Activity).application as BaseMainApplication)
            .baseAppComponent).topAdEditModule(context?.let { TopAdEditModule(it) }).build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsEditSheetEditAdGroupDailyBudgetBinding.inflate(inflater, container, false)
        binding = viewBinding
        initChildLayout(viewBinding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout(root: LinearLayout) {
        isHideable = true
        showCloseIcon = true
        setChild(root)
        setTitle(getString(topadseditR.string.top_ads_edit_ad_group_daily_budget_bottom_sheet_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
        viewModel.getPerformanceData(productIds, bids, CurrencyFormatHelper.convertRupiahToDouble(dailyBudget).toFloat())
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.performanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    this.performanceData = it.data
                    val data = it.data.getOrNull(CONST_2)
                    data?.let { performanceData ->
                        binding?.amount?.text = String.format(getString(topadscommonR.string.top_ads_performce_count_prefix), performanceData.retention)
                        if (performanceData.percentage.toIntOrZero() != Int.ZERO) {
                            binding?.percentage?.text = String.format(getString(topadseditR.string.top_ads_performance_increment_text), performanceData.percentage)
                            binding?.percentage?.show()
                        } else {
                            binding?.percentage?.hide()
                        }
                    }
                }

                else -> {}
            }
        }
    }

    private fun initView() {
        setBudget()
        if (dailyBudget == Int.ZERO.toString()) {
            binding?.toggle?.isChecked = false
            binding?.textField?.hide()
            binding?.editAdGroupNamCta?.isEnabled = false
        } else {
            binding?.toggle?.isChecked = true
            binding?.textField?.show()
            binding?.editAdGroupNamCta?.isEnabled = true
            binding?.textField?.editText?.setText(dailyBudget)
        }

    }

    private fun setBudget() {
        minBudget = if (!isBidAutomatic) {
            val searchBid = bids.firstOrNull()
            val browseBid = bids.getOrNull(CONST_1)
            if ((searchBid ?: Int.ZERO.toFloat()) > (browseBid
                    ?: Int.ZERO.toFloat())) (searchBid?.toInt())?.times(MULTIPLIER)
                ?: Int.ZERO else (browseBid?.toInt())?.times(MULTIPLIER) ?: Int.ZERO
        } else {
            MAX_BUDGET_AUTOMATIC
        }
        maxBudget = MAX_BUDGET_MANUAL
    }

    private fun setListeners() {
        binding?.textField?.editText?.addTextChangedListener(object : NumberTextWatcher(binding?.textField?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < minBudget) {
                    binding?.textField?.isInputError = true
                    binding?.textField?.setMessage(String.format(getString(topadseditR.string.top_ads_minimum_budget), CurrencyFormatHelper.convertToRupiah(minBudget.toString())))
                    binding?.editAdGroupNamCta?.isEnabled = false
                } else if (number > maxBudget) {
                    binding?.textField?.isInputError = true
                    binding?.textField?.setMessage(String.format(getString(topadseditR.string.top_ads_maximum_budget), CurrencyFormatHelper.convertToRupiah(maxBudget.toString())))
                    binding?.editAdGroupNamCta?.isEnabled = false
                } else {
                    binding?.editAdGroupNamCta?.isEnabled = true
                    binding?.textField?.isInputError = false
                    binding?.textField?.setMessage(String.EMPTY)
                    viewModel.getPerformanceData(productIds, bids, number.toFloat())
                }
            }
        })

        binding?.editAdGroupNamCta?.setOnClickListener {
            clickListener?.invoke(binding?.textField?.editText?.text.toString().removeCommaRawString(), binding?.toggle?.isChecked
                ?: false)
            dismiss()
        }

        binding?.toggle?.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            if (state) {
                binding?.textField?.show()
                if (dailyBudget.removeCommaRawString().toFloatOrZero() < suggestedDailyBudget.removeCommaRawString().toFloatOrZero()) {
                    binding?.editAdGroupNamCta?.isEnabled = true
                }
                binding?.textField?.editText?.setText(suggestedDailyBudget)
            } else {
                binding?.textField?.hide()
                binding?.editAdGroupNamCta?.isEnabled = false
                binding?.editAdGroupNamCta?.isEnabled = true
                viewModel.getPerformanceData(productIds, bids, Int.ZERO.toFloat())
            }
        }
        binding?.icon?.setOnClickListener {
            CreatePotentialPerformanceSheet.newInstance(
                performanceData?.firstOrNull()?.retention.toIntOrZero(),
                performanceData?.getOrNull(CONST_1)?.retention.toIntOrZero()
            ).show(childFragmentManager)
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        clickListener: (dailyBudget: String, isToggleOn: Boolean) -> Unit
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
        this.clickListener = clickListener
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "EDIT_AD_GROUP_DAILY_BUDGET_BOTTOM_SHEET_TAG"
        fun newInstance(
            dailyBudget: String,
            suggestedDailyBudget: String,
            productIds: List<String>,
            bids: MutableList<Float?>,
            isBidAutomatic: Boolean,
        ): EditAdGroupDailyBudgetBottomSheet =
            EditAdGroupDailyBudgetBottomSheet().apply {
                this.dailyBudget = dailyBudget
                this.suggestedDailyBudget = suggestedDailyBudget
                this.productIds = productIds
                this.bids = bids
                this.isBidAutomatic = isBidAutomatic
            }
    }
}
