package com.tokopedia.tokofood.feature.search.searchresult.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokofood.databinding.BottomsheetTokofoodSearchQuickPriceRangeBinding
import com.tokopedia.tokofood.feature.search.di.component.DaggerTokoFoodSearchComponent
import com.tokopedia.tokofood.feature.search.di.component.TokoFoodSearchComponent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox.QuickPriceRangeFilterListener
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.pricerangecheckbox.QuickPriceRangeItemAdapter
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeChipUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.PriceRangeFilterCheckboxItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodQuickPriceRangeViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import javax.inject.Inject

class TokofoodQuickPriceRangeBottomsheet : BottomSheetUnify(), QuickPriceRangeFilterListener,
    HasComponent<TokoFoodSearchComponent> {

    @Inject
    lateinit var viewModel: TokofoodQuickPriceRangeViewModel

    private val priceRangeItemUiModels by lazy {
        arguments?.getParcelableArrayList<PriceRangeFilterCheckboxItemUiModel>(KEY_PRICE_RANGE_ITEMS)
            .orEmpty()
    }

    private val title by lazy {
        arguments?.getString(KEY_TITLE).orEmpty()
    }

    private var listener: Listener? = null

    private var binding by autoClearedNullable<BottomsheetTokofoodSearchQuickPriceRangeBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            BottomsheetTokofoodSearchQuickPriceRangeBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        collectValues()
        viewModel.setPriceRangeUiModels(priceRangeItemUiModels, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
    }

    override fun getComponent(): TokoFoodSearchComponent {
        return DaggerTokoFoodSearchComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onPriceRangeFilterCheckboxItemClicked(
        uiModel: PriceRangeFilterCheckboxItemUiModel,
        isChecked: Boolean
    ) {
        viewModel.setPriceRangeUiModel(uiModel, isChecked)
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setupBottomSheet() {
        setupBottomSheetTitle()
        clearContentPadding = true
    }

    private fun setupBottomSheetTitle() {
        setTitle(title)
    }

    private fun setupView() {
        setupRecyclerView()
        setupApplyButton()
    }

    private fun collectValues() {
        collectApplyButtonClicked()
        collectShouldShowApplyButton()
        collectShouldShowResetButton()
        collectUiModels()
    }

    private fun collectApplyButtonClicked() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.applyButtonClicked.collect { options ->
                listener?.onApplyPriceRange(options)
            }
        }
    }

    private fun collectShouldShowApplyButton() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shouldShowApplyButton.collect { shouldShow ->
                toggleButtonLayoutVisibility(shouldShow)
            }
        }
    }

    private fun collectShouldShowResetButton() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shouldShowResetButton.collect { shouldShow ->
                toggleResetButton(shouldShow)
            }
        }
    }

    private fun collectUiModels() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentUiModelsFlow.collect { uiModels ->
                setupAdapter(uiModels)
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.rvTokofoodSearchQuickPriceRange?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupApplyButton() {
        binding?.btnTokofoodSearchQuickPriceRangeApply?.setOnClickListener {
            dismiss()
            viewModel.clickApplyButton()
        }
    }

    private fun setupAdapter(uiModels: List<PriceRangeFilterCheckboxItemUiModel>) {
        val optionViewModelList = uiModels.map {
            PriceRangeFilterCheckboxItemUiModel(option = it.option).apply {
                isSelected = it.isSelected
            }
        }
        val quickPriceRangeAdapter = QuickPriceRangeItemAdapter(optionViewModelList, this)
        binding?.rvTokofoodSearchQuickPriceRange?.swapAdapter(quickPriceRangeAdapter, false)
    }

    private fun toggleButtonLayoutVisibility(shouldShow: Boolean) {
        binding?.layoutTokofoodSearchQuickPriceRangeButton?.showWithCondition(shouldShow)
        binding?.btnTokofoodSearchQuickPriceRangeApply?.showWithCondition(shouldShow)
    }

    private fun toggleResetButton(shouldShow: Boolean) {
        if (shouldShow) {
            setAction(getResetMessage()) {
                viewModel.resetUiModels()
            }
        } else {
            setAction(String.EMPTY) {}
        }
    }

    private fun getResetMessage(): String {
        return try {
            context?.getString(com.tokopedia.tokofood.R.string.search_srp_quick_price_reset).orEmpty()
        } catch (ex: Exception) {
            RESET_MESSAGE
        }
    }

    interface Listener {
        fun onApplyPriceRange(checkedOptions: List<Option>)
    }

    companion object {

        private const val TAG = "TokofoodQuickPriceRangeBottomsheet"
        private const val RESET_MESSAGE = "Reset"

        private const val KEY_PRICE_RANGE_ITEMS = "key_price_range_items"
        private const val KEY_TITLE = "key_title"

        @JvmStatic
        fun createInstance(
            item: PriceRangeChipUiModel,
            listener: Listener
        ): TokofoodQuickPriceRangeBottomsheet {
            return TokofoodQuickPriceRangeBottomsheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_PRICE_RANGE_ITEMS, ArrayList(item.uiModels))
                    putString(KEY_TITLE, item.subtitle)
                }
                this.listener = listener
            }
        }
    }

}
