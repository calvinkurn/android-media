package com.tokopedia.filter.bottomsheet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.filter.R
import com.tokopedia.filter.bottomsheet.filter.FilterViewListener
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.bottomsheet.filtercategorydetail.FilterCategoryDetailBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterListener
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewListener
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxListener
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.GeneralFilterSortOptions
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewListener
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.configureBottomSheetHeight
import com.tokopedia.filter.common.helper.setBottomSheetActionBold
import com.tokopedia.filter.databinding.SortFilterBottomSheetBinding
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class SortFilterBottomSheet: BottomSheetUnify() {

    companion object {
        private const val SORT_FILTER_BOTTOM_SHEET_TAG = "SORT_FILTER_BOTTOM_SHEET_TAG"

        private const val SORT_FILTER_TITLE_KEY = "sort_filter_title_key"

        @JvmStatic
        fun createInstance(title: String): SortFilterBottomSheet {
            return SortFilterBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(SORT_FILTER_TITLE_KEY, title)
                }
            }
        }
    }

    private var mapParameter: Map<String, String> = mapOf()
    private var dynamicFilterModel: DynamicFilterModel? = null
    private var sortFilterCallback: Callback? = null
    private var isReimagine: Boolean = false

    private var sortFilterBottomSheetViewModel: SortFilterBottomSheetViewModel? = null
    private var sortFilterBottomSheetView: View? = null
    private var binding: SortFilterBottomSheetBinding? = null
    private val sortViewListener = object: SortViewListener {
        override fun onSortItemClick(sortItemViewModel: SortItemViewModel) {
            sortFilterBottomSheetViewModel?.onSortItemClick(sortItemViewModel)
        }
    }
    private val filterViewListener = object: FilterViewListener {
        override fun onOptionClick(
            filterViewModel: FilterViewModel,
            optionViewModel: OptionViewModel,
        ) {
            sortFilterBottomSheetViewModel?.onOptionClick(filterViewModel, optionViewModel)
        }

        override fun onSeeAllOptionClick(filterViewModel: FilterViewModel) {
            if (filterViewModel.filter.isCategoryFilter)
                onSeeAllFilterCategoryClick(filterViewModel)
            else
                onSeeAllFilterGeneralClick(filterViewModel)
        }
    }

    private fun onSeeAllFilterCategoryClick(filterViewModel: FilterViewModel) {
        val filterDetailCallback = object: FilterCategoryDetailBottomSheet.Callback {
            override fun onApplyButtonClicked(selectedFilterValue: String) {
                sortFilterBottomSheetViewModel?.onApplyCategoryFilterFromDetailPage(
                    filterViewModel,
                    selectedFilterValue,
                )
            }
        }

        val selectedCategoryFilterValue =
            sortFilterBottomSheetViewModel?.getSelectedCategoryFilterValue() ?: ""

        FilterCategoryDetailBottomSheet().show(
            parentFragmentManager,
            filterViewModel.filter,
            selectedCategoryFilterValue,
            filterDetailCallback,
        )
    }

    private fun onSeeAllFilterGeneralClick(filterViewModel: FilterViewModel) {
        val filterDetailCallback = object: FilterGeneralDetailBottomSheet.Callback {
            override fun onApplyButtonClicked(optionList: List<GeneralFilterSortOptions>?) {
                sortFilterBottomSheetViewModel?.onApplyFilterFromDetailPage(
                    filterViewModel,
                    optionList?.filterIsInstance<Option>(),
                )
            }
        }

        FilterGeneralDetailBottomSheet().show(
            fragmentManager = parentFragmentManager,
            filter = filterViewModel.filter,
            callback = filterDetailCallback,
        )
    }

    private val priceFilterListener = object: PriceFilterViewListener {
        override fun onMinPriceEditedFromTextInput(
            priceFilterViewModel: PriceFilterViewModel,
            minValue: Int,
        ) {
            sortFilterBottomSheetViewModel?.onMinPriceFilterEdited(priceFilterViewModel, minValue)
        }

        override fun onMaxPriceEditedFromTextInput(
            priceFilterViewModel: PriceFilterViewModel,
            maxValue: Int,
        ) {
            sortFilterBottomSheetViewModel?.onMaxPriceFilterEdited(priceFilterViewModel, maxValue)
        }

        override fun onPriceRangeClicked(
            priceFilterViewModel: PriceFilterViewModel,
            priceRangeOption: PriceOptionViewModel,
        ) {
            sortFilterBottomSheetViewModel?.onPriceRangeOptionClick(
                priceFilterViewModel,
                priceRangeOption
            )
        }

        override fun onPriceTextOutOfFocus() {
            hideKeyboard()
            sortFilterBottomSheetViewModel?.onPriceTextOutOfFocus()
        }
    }

    private val keywordFilterListener = object: KeywordFilterListener {
        override fun scrollToPosition(position: Int) {
            val layoutManager = binding?.recyclerViewSortFilterBottomSheet?.layoutManager

            if (layoutManager is LinearLayoutManager)
                layoutManager.scrollToPositionWithOffset(position, 0)
        }

        override fun onChangeKeywordFilter(keywordFilterDataView: KeywordFilterDataView) {
            sortFilterBottomSheetViewModel?.onChangeKeywordFilter(keywordFilterDataView)
        }
    }

    private val priceRangeFilterCheckboxListener = object : PriceRangeFilterCheckboxListener {

        override fun onPriceRangeFilterCheckboxItemClicked(
            priceRangeFilterCheckboxDataView: PriceRangeFilterCheckboxDataView,
            optionViewModel: OptionViewModel
        ) {
            sortFilterBottomSheetViewModel?.onOptionClick(
                priceRangeFilterCheckboxDataView,
                optionViewModel,
            )
        }
    }

    private val sortFilterBottomSheetAdapter by lazyThreadSafetyNone {
        SortFilterBottomSheetAdapter(
            SortFilterBottomSheetTypeFactoryImpl(
                sortViewListener,
                filterViewListener,
                priceFilterListener,
                keywordFilterListener,
                priceRangeFilterCheckboxListener,
            )
        )
    }

    fun show(
        fragmentManager: FragmentManager,
        mapParameter: Map<String, String>?,
        dynamicFilterModel: DynamicFilterModel?,
        callback: Callback,
        isReimagine: Boolean = false,
    ) {
        if (mapParameter == null) return

        this.mapParameter = mapParameter
        this.dynamicFilterModel = dynamicFilterModel
        this.sortFilterCallback = callback
        this.isReimagine = isReimagine

        show(fragmentManager, SORT_FILTER_BOTTOM_SHEET_TAG)
    }

    fun setResultCountText(buttonApplySortFilterText: String) {
        binding?.buttonApplySortFilter?.let {
            it.isLoading = buttonApplySortFilterText.isEmpty()
            it.text = buttonApplySortFilterText
        }
    }

    fun setDynamicFilterModel(dynamicFilterModel: DynamicFilterModel) {
        sortFilterBottomSheetViewModel?.lateInitDynamicFilterModel(dynamicFilterModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        sortFilterBottomSheetViewModel = SortFilterBottomSheetViewModel()
        sortFilterBottomSheetViewModel?.init(mapParameter, dynamicFilterModel, isReimagine)
    }

    private fun initView() {
        initBottomSheetSettings()

        val bottomSheetTitle =
            arguments?.getString(SORT_FILTER_TITLE_KEY)?.takeIf { it.isNotBlank() }
                ?: getString(R.string.discovery_filter)

        setTitle(bottomSheetTitle)

        initBottomSheetAction()

        val view = View.inflate(requireContext(), R.layout.sort_filter_bottom_sheet, null)
        binding = SortFilterBottomSheetBinding.bind(view)
        sortFilterBottomSheetView = binding?.root
        setChild(sortFilterBottomSheetView)

        initRecyclerView()
        initButtonApplySortFilter()
    }

    private fun initBottomSheetSettings() {
        showKnob = sortFilterBottomSheetViewModel?.showKnob ?: false
        showCloseIcon = !showKnob
        isDragable = showKnob
        isHideable = showKnob
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun initBottomSheetAction() {
        setAction(getString(R.string.filter_button_reset_text)) {
            sortFilterBottomSheetViewModel?.resetSortAndFilter()
        }
    }

    private fun initRecyclerView() {
        binding?.recyclerViewSortFilterBottomSheet?.let {
            it.adapter = sortFilterBottomSheetAdapter
            it.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
            it.addOnScrollListener(createRecyclerViewOnScrollListener())
        }
    }

    private fun createRecyclerViewOnScrollListener() = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        activity?.let { activity ->
            KeyboardHandler.DropKeyboard(activity, sortFilterBottomSheetView)
        }
    }

    private fun initButtonApplySortFilter() {
        binding?.buttonApplySortFilter?.setOnClickListener(this::onButtonApplySortFilterClicked)
    }

    private fun onButtonApplySortFilterClicked(view: View) {
        sortFilterBottomSheetViewModel?.applySortFilter()

        callbackApplySortFilter()

        dismiss()
    }

    private fun callbackApplySortFilter() {
        val mapParameter = sortFilterBottomSheetViewModel?.mapParameter ?: mapOf()
        val selectedFilterMap = sortFilterBottomSheetViewModel?.getSelectedFilterMap() ?: mapOf()
        val selectedSortMap = sortFilterBottomSheetViewModel?.getSelectedSortMap() ?: mapOf()
        val sortAutoFilterMap = sortFilterBottomSheetViewModel?.getSortAutoFilterMap() ?: mapOf()
        val selectedSortName = sortFilterBottomSheetViewModel?.selectedSortName ?: ""
        val applySortFilterModel = ApplySortFilterModel(
            mapParameter,
            selectedFilterMap,
            selectedSortMap,
            selectedSortName,
            sortAutoFilterMap,
        )

        sortFilterCallback?.onApplySortFilter(applySortFilterModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (showKnob) configureBottomSheetHeight()
        setBottomSheetActionBold()
        observeViewModel()

        sortFilterBottomSheetViewModel?.onViewCreated()
    }

    private fun observeViewModel() {
        sortFilterBottomSheetViewModel?.run {
            sortFilterListLiveData.observe(viewLifecycleOwner, ::processSortFilterList)

            updateViewInPositionEventLiveData.observe(
                viewLifecycleOwner,
                EventObserver(::processUpdateViewInPosition),
            )

            isLoadingLiveData.observe(viewLifecycleOwner, ::processLoading)

            isButtonResetVisibleLiveData.observe(viewLifecycleOwner, ::setActionResetVisibility)

            isViewExpandedLiveData.observe(viewLifecycleOwner, ::expandBottomSheetView)

            trackPriceRangeClickEventLiveData.observe(
                viewLifecycleOwner,
                EventObserver(FilterTracking::sendGeneralEvent)
            )

            isLoadingForDynamicFilterLiveData.observe(
                viewLifecycleOwner,
                ::processLoadingForDynamicFilter,
            )
        }
    }

    private fun processSortFilterList(
        sortFilterList: List<Visitable<SortFilterBottomSheetTypeFactory>>,
    ) {
        sortFilterBottomSheetAdapter.setSortFilterList(sortFilterList)
        binding?.recyclerViewSortFilterBottomSheet?.scrollToPosition(0)
    }

    private fun processUpdateViewInPosition(position: Int) {
        sortFilterBottomSheetAdapter.notifyItemChanged(position)
    }

    private fun processLoading(isLoading: Boolean) {
        if (isLoading)
            showButtonApplyFilter()
        else
            hideButtonApplyFilter()
    }

    private fun showButtonApplyFilter() {
        binding?.buttonApplyContainer?.let {
            it.background = getButtonApplyContainerBackground()
            it.visibility = View.VISIBLE
        }

        setButtonApplyFilterText()
    }

    private fun setButtonApplyFilterText() {
        val mapParameter = sortFilterBottomSheetViewModel?.mapParameter ?: mapOf()

        setResultCountText("")
        sortFilterCallback?.getResultCount(mapParameter)
    }

    private fun getButtonApplyContainerBackground() =
        if (context.isDarkMode())
            context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        else
            context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_NN0)

    private fun hideButtonApplyFilter() {
        binding?.buttonApplyContainer?.visibility = View.GONE
    }

    private fun setActionResetVisibility(isVisible: Boolean) {
        bottomSheetAction.post {
            bottomSheetAction.shouldShowWithAction(isVisible, ::configureButtonReset)
        }
    }

    private fun configureButtonReset() {
        try {
            bottomSheetAction.text = getString(R.string.filter_button_reset_text)
            bottomSheetAction.setOnClickListener {
                sortFilterBottomSheetViewModel?.resetSortAndFilter()
            }
        } catch (throwable: Throwable) {

        }
    }

    private fun expandBottomSheetView(ignored: Boolean) {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun processLoadingForDynamicFilter(isLoadingForDynamicFilter: Boolean) {
        if (isLoadingForDynamicFilter) showLoadingForDynamicFilter()
        else finishLoadingForDynamicFilter()
    }

    private fun showLoadingForDynamicFilter() {
        val binding = binding ?: return
        binding.progressBarSortFilterBottomSheet.show()
        binding.recyclerViewSortFilterBottomSheet.hide()
        binding.buttonApplyContainer.hide()
    }

    private fun finishLoadingForDynamicFilter() {
        val binding = binding ?: return
        binding.progressBarSortFilterBottomSheet.hide()
        binding.recyclerViewSortFilterBottomSheet.show()
    }

    override fun onDestroyView() {
        view?.let {
            if (it is ViewGroup) {
                it.removeAllViews()
            }
        }
        binding = null

        super.onDestroyView()
    }

    data class ApplySortFilterModel(
        val mapParameter: Map<String, String>,
        val selectedFilterMapParameter: Map<String, String>,
        val selectedSortMapParameter: Map<String, String>,
        val selectedSortName: String,
        val sortAutoFilterMapParameter: Map<String, String>,
    )

    interface Callback {
        fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel)

        fun getResultCount(mapParameter: Map<String, String>)
    }
}
