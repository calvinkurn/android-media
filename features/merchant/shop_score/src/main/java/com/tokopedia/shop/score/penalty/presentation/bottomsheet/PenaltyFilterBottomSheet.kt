package com.tokopedia.shop.score.penalty.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.presentation.bottomsheet.BaseBottomSheetShopScore
import com.tokopedia.shop.score.databinding.BottomsheetFilterPenaltyBinding
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyDateListener
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyTypesBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.model.FilterTypePenaltyUiModelWrapper
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.PenaltyTypesUiModelWrapper
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PenaltyFilterBottomSheet : BaseBottomSheetShopScore<BottomsheetFilterPenaltyBinding>(),
    FilterPenaltyBottomSheetListener, FilterPenaltyTypesBottomSheetListener, FilterPenaltyDateListener, PenaltyDateFilterBottomSheet.CalenderListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelShopPenalty by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPenaltyViewModel::class.java)
    }

    private var isApplyFilter = false
    private var canShowApplyFilterButton = false

    private val filterPenaltyAdapterTypeFactory by lazy { FilterPenaltyAdapterFactory(this, this) }
    private val filterPenaltyAdapter by lazy { FilterPenaltyAdapter(filterPenaltyAdapterTypeFactory) }

    private var penaltyFilterFinishListener: PenaltyFilterFinishListener? = null

    init {
        showCloseIcon = true
        clearContentPadding = true
        isFullpage = true
    }

    override fun bind(view: View) = BottomsheetFilterPenaltyBinding.bind(view)

    override fun getLayoutResId(): Int = R.layout.bottomsheet_filter_penalty

    override fun getTitleBottomSheet(): String =
        getString(R.string.title_penalty_filter_penalty_bottom_sheet)

    override fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, PENALTY_FILTER_BOTTOM_SHEET_TAG)
            }
        }
    }

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataCacheFromManager()
        setupRecyclerView()
        observePenaltyFilter()
        observeUpdateSortSelectedPeriod()
        observeUpdateFilterSelected()
        observeResetFilter()
        observeDateFilter()
        clickBtnApplied()
    }

    override fun onDestroy() {
        removeObservers(viewModelShopPenalty.penaltyPageData)
        removeObservers(viewModelShopPenalty.filterPenaltyData)
        removeObservers(viewModelShopPenalty.updateSortSelectedPeriod)
        removeObservers(viewModelShopPenalty.resetFilterResult)
        removeObservers(viewModelShopPenalty.updateFilterSelected)
        removeObservers(viewModelShopPenalty.dateFilterResult)
        super.onDestroy()
    }

    override fun onChipsFilterItemClick(
        nameFilter: String,
        chipType: String,
        chipTitle: String,
        position: Int
    ) {
        when (nameFilter) {
            ShopScoreConstant.TITLE_SORT -> {
                viewModelShopPenalty.updateFilterSelected(nameFilter, chipType, position, false)
            }
            ShopScoreConstant.TITLE_TYPE_PENALTY -> {
                viewModelShopPenalty.updateFilterSelected(nameFilter, chipType, position, true)
            }
        }
    }

    override fun onSeeAllButtonClicked(uiModel: PenaltyFilterUiModel) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(
            PenaltyFilterTypesBottomSheet.KEY_FILTER_PENALTY_TYPES,
            PenaltyTypesUiModelWrapper(
                uiModel.chipsFilterList
            )
        )

        val bottomSheetFilterPenalty =
            PenaltyFilterTypesBottomSheet.createInstance(cacheManager?.id.orEmpty())
        bottomSheetFilterPenalty.setListener(this)
        bottomSheetFilterPenalty.show(childFragmentManager)
    }

    override fun onFilterSaved(filterList: List<Int>) {
        viewModelShopPenalty.updateSortFilterSelected(filterList)
    }

    override fun onDatePicked(
        startDate: String,
        defaultStartDate: String,
        endDate: String,
        defaultEndDate: String
    ) {
        viewModelShopPenalty.setMaxDateFilterData(defaultStartDate to defaultEndDate)
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance(
            startDate,
            endDate,
            defaultStartDate,
            defaultEndDate
        )
        bottomSheetDateFilter.setCalendarListener(this)
        bottomSheetDateFilter.show(childFragmentManager)
    }

    override fun onSaveCalendarClicked(
        startDate: Pair<String, String>,
        endDate: Pair<String, String>
    ) {
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        viewModelShopPenalty.setDateFilterData(startDate.first, endDate.first, date)

        val maxDate =
            viewModelShopPenalty.getMaxStartDate()?.let { maxStartDate ->
                viewModelShopPenalty.getMaxEndDate()?.let { maxEndDate ->
                    Pair(maxStartDate, maxEndDate)
                }
            }

        maxDate?.let {
            filterPenaltyAdapter.updateDateSelected(
                Pair(startDate.first, endDate.first),
                it,
                date
            )
        }
    }

    private fun getDataCacheFromManager() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(KEY_CACHE_MANAGER_ID_PENALTY_FILTER)
            )
        }
        val filterTypePenalty =
            cacheManager?.get(KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapper::class.java)
                ?: FilterTypePenaltyUiModelWrapper()
        viewModelShopPenalty.getFilterPenalty(
            getOrderedUiModels(filterTypePenalty.penaltyFilterList + filterTypePenalty.penaltyDateFilterList)
        )
    }

    /**
     * Return ordered filter ui models so the date filter is positioned on the middle index of the list
     */
    private fun getOrderedUiModels(filterList: List<BaseFilterPenaltyPage>): List<BaseFilterPenaltyPage> {
        val dateFilterIndex = filterList.indexOfFirst { it is PenaltyFilterDateUiModel }
        val dateFilter = filterList.find { it is PenaltyFilterDateUiModel }
        val orderedList: MutableList<BaseFilterPenaltyPage> = filterList.toMutableList().apply {
            if (dateFilterIndex > RecyclerView.NO_POSITION && dateFilter != null) {
                removeAt(dateFilterIndex)
                val middlePosition = size / 2
                add(middlePosition, dateFilter)
            }
        }
        return orderedList.toList()
    }

    private fun clickBtnApplied() {
        binding?.btnShowPenalty?.setOnClickListener {
            isApplyFilter = true
            penaltyFilterFinishListener?.onClickFilterApplied(
                viewModelShopPenalty.getPenaltyFilterUiModelList()
            )
            dismiss()
        }
    }

    private fun observePenaltyFilter() {
        observe(viewModelShopPenalty.filterPenaltyData) {
            when (it) {
                is Success -> {
                    binding?.rvPenaltyFilterBottomSheet?.post {
                        filterPenaltyAdapter.updateData(it.data)
                        showHideBottomSheetReset()
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun observeUpdateFilterSelected() = observe(viewModelShopPenalty.updateFilterSelected) {
        when (it) {
            is Success -> {
                binding?.rvPenaltyFilterBottomSheet?.post {
                    filterPenaltyAdapter.updateFilterSelected(it.data.first, it.data.second)
                    showHideBottomSheetReset()
                }
            }
            is Fail -> {
            }
        }
    }

    private fun observeUpdateSortSelectedPeriod() = observe(viewModelShopPenalty.updateSortSelectedPeriod) {
        if (it is Success) {
            binding?.rvPenaltyFilterBottomSheet?.post {
                filterPenaltyAdapter.updateFilterSelected(it.data)
                showHideBottomSheetReset()
            }
        }
    }

    private fun observeResetFilter() = observe(viewModelShopPenalty.resetFilterResult) {
        when (it) {
            is Success -> {
                binding?.rvPenaltyFilterBottomSheet?.post {
                    filterPenaltyAdapter.resetFilterSelected(it.data)
                    showHideBottomSheetReset()
                }
            }
            else -> {
            }
        }
    }

    private fun observeDateFilter() = observe(viewModelShopPenalty.dateFilterResult) {
        showHideBottomSheetReset()
    }

    private fun setClickBtnReset() {
        setAction(getString(R.string.reset_filter_penalty)) {
            viewModelShopPenalty.resetFilterSelected()
        }
    }

    private fun showHideBottomSheetReset() {
        if (checkIsSelected() || checkIsDateFilterApplied()) {
            setClickBtnReset()
        } else {
            clearAction()
        }

        if (canShowApplyFilterButton) {
            showApplyFilterButton()
        } else {
            canShowApplyFilterButton = true
        }
    }

    private fun showApplyFilterButton() {
        binding?.btnShowPenalty?.show()
    }

    private fun checkIsSelected(): Boolean {
        viewModelShopPenalty.getPenaltyFilterUiModelList().filterIsInstance<PenaltyFilterUiModel>()
            .forEach {
                when(it.title) {
                    ShopScoreConstant.TITLE_TYPE_PENALTY -> {
                        it.chipsFilterList.forEach { chips ->
                            if (chips.isSelected) {
                                return true
                            }
                        }
                    }
                    ShopScoreConstant.TITLE_SORT -> {
                        it.chipsFilterList.forEach { chip ->
                            if (chip.isSelected && chip.title == ShopScoreConstant.SORT_OLDEST) {
                                return true
                            }
                        }
                    }
                }
            }
        return false
    }

    private fun checkIsDateFilterApplied(): Boolean {
        viewModelShopPenalty.getPenaltyFilterUiModelList()
            .filterIsInstance<PenaltyFilterDateUiModel>()
            .firstOrNull()?.let {
                return it.startDate != viewModelShopPenalty.getInitialStartDate() || it.endDate != viewModelShopPenalty.getInitialEndDate()
            }
        return false
    }

    private fun setupRecyclerView() {
        binding?.rvPenaltyFilterBottomSheet?.run {
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = filterPenaltyAdapter
        }
    }

    fun setPenaltyFilterFinishListener(penaltyFilterFinishListener: PenaltyFilterFinishListener) {
        this.penaltyFilterFinishListener = penaltyFilterFinishListener
    }

    interface PenaltyFilterFinishListener {
        fun onClickFilterApplied(penaltyFilterUiModelList: List<BaseFilterPenaltyPage>)
    }

    companion object {
        const val PENALTY_FILTER_BOTTOM_SHEET_TAG = "PenaltyFilterBottomSheetTag"
        const val KEY_FILTER_TYPE_PENALTY = "key_filter_type_penalty"
        const val KEY_CACHE_MANAGER_ID_PENALTY_FILTER = "key_cache_manager_id_penalty_filter"

        fun newInstance(cacheManagerId: String): PenaltyFilterBottomSheet {
            val penaltyFilterBottomSheet = PenaltyFilterBottomSheet()
            val args = Bundle()
            args.putString(KEY_CACHE_MANAGER_ID_PENALTY_FILTER, cacheManagerId)
            penaltyFilterBottomSheet.arguments = args
            return penaltyFilterBottomSheet
        }
    }
}

