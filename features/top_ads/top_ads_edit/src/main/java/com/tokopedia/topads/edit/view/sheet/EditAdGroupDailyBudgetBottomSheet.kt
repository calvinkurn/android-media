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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupDailyBudgetBinding
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.edit.R as topadseditR

class EditAdGroupDailyBudgetBottomSheet : BottomSheetUnify() {

    private var bids: MutableList<Float?> = mutableListOf()
    private var productIds: List<String> = mutableListOf()
    private var suggestedDailyBudget: String = "0"
    private var dailyBudget: String = "0"
    private var clickListener: ((dailyBudget: String, isToggleOn: Boolean) -> Unit)? = null
    private var binding: TopadsEditSheetEditAdGroupDailyBudgetBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditAdGroupViewModel::class.java]
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
        viewModel.getPerformanceData(productIds, bids, dailyBudget.toFloatOrZero())
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.performanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val data = it.data.getOrNull(2)
                    data?.let { performanceData ->
                        binding?.amount?.text = String.format("%sx", performanceData.retention)
                        if (performanceData.percentage.toIntOrZero() != 0) {
                            binding?.percentage?.text = String.format("%s %% meningkat", performanceData.percentage)
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
        setListeners()
        if (dailyBudget == "0") {
            binding?.toggle?.isChecked = false
            binding?.textField?.isEnabled = false
            binding?.editAdGroupNamCta?.isEnabled = false
        } else {
            binding?.toggle?.isChecked = true
            binding?.textField?.isEnabled = true
            binding?.editAdGroupNamCta?.isEnabled = true
            binding?.textField?.editText?.setText(dailyBudget)
        }

    }

    private fun setListeners() {
        binding?.textField?.editText?.addTextChangedListener(object : NumberTextWatcher(binding?.textField?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)

                if (dailyBudget == "0" && number >= suggestedDailyBudget.removeCommaRawString().toDoubleOrZero()) {
                    binding?.editAdGroupNamCta?.isEnabled = true
                    viewModel.getPerformanceData(productIds, bids, number.toFloat())
                } else if (dailyBudget.toFloatOrZero() >= 0f && number < suggestedDailyBudget.removeCommaRawString().toDoubleOrZero()) {
                    binding?.editAdGroupNamCta?.isEnabled = false
                } else {
                    binding?.editAdGroupNamCta?.isEnabled = true
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
                binding?.textField?.isEnabled = true
                if (dailyBudget.removeCommaRawString().toFloatOrZero() < suggestedDailyBudget.removeCommaRawString().toFloatOrZero()) {
                    binding?.editAdGroupNamCta?.isEnabled = true
                }
                binding?.textField?.editText?.setText(suggestedDailyBudget)
            } else {
                binding?.textField?.isEnabled = false
                binding?.editAdGroupNamCta?.isEnabled = false
                binding?.editAdGroupNamCta?.isEnabled = true
            }
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
        ): EditAdGroupDailyBudgetBottomSheet =
            EditAdGroupDailyBudgetBottomSheet().apply {
                this.dailyBudget = dailyBudget
                this.suggestedDailyBudget = suggestedDailyBudget
                this.productIds = productIds
                this.bids = bids
            }
    }
}
