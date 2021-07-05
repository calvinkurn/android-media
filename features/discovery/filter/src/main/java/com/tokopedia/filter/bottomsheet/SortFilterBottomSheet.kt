package com.tokopedia.filter.bottomsheet

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
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
import com.tokopedia.filter.bottomsheet.filtercategorydetail.FilterCategoryDetailBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewListener
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewListener
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.StatusBarColorHelper
import com.tokopedia.filter.common.helper.configureBottomSheetHeight
import com.tokopedia.filter.common.helper.setBottomSheetActionBold
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.sort_filter_bottom_sheet.view.*

class SortFilterBottomSheet: BottomSheetUnify() {

    companion object {
        private const val SORT_FILTER_BOTTOM_SHEET_TAG = "SORT_FILTER_BOTTOM_SHEET_TAG"
    }

    private var mapParameter: Map<String, String> = mapOf()
    private var dynamicFilterModel: DynamicFilterModel? = null
    private var sortFilterCallback: Callback? = null
    private var statusBarColorHelper: StatusBarColorHelper? = null

    private var sortFilterBottomSheetViewModel: SortFilterBottomSheetViewModel? = null
    private var sortFilterBottomSheetView: View? = null
    private val sortViewListener = object: SortViewListener {
        override fun onSortItemClick(sortItemViewModel: SortItemViewModel) {
            sortFilterBottomSheetViewModel?.onSortItemClick(sortItemViewModel)
        }
    }
    private val filterViewListener = object: FilterViewListener {
        override fun onOptionClick(filterViewModel: FilterViewModel, optionViewModel: OptionViewModel) {
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
                sortFilterBottomSheetViewModel?.onApplyCategoryFilterFromDetailPage(filterViewModel, selectedFilterValue)
            }
        }

        val selectedCategoryFilterValue = sortFilterBottomSheetViewModel?.getSelectedCategoryFilterValue() ?: ""
        FilterCategoryDetailBottomSheet().show(requireFragmentManager(), filterViewModel.filter, selectedCategoryFilterValue, filterDetailCallback)
    }

    private fun onSeeAllFilterGeneralClick(filterViewModel: FilterViewModel) {
        val filterDetailCallback = object: FilterGeneralDetailBottomSheet.Callback {
            override fun onApplyButtonClicked(optionList: List<Option>?) {
                sortFilterBottomSheetViewModel?.onApplyFilterFromDetailPage(filterViewModel, optionList)
            }
        }

        FilterGeneralDetailBottomSheet().show(requireFragmentManager(), filterViewModel.filter, filterDetailCallback)
    }

    private val priceFilterListener = object: PriceFilterViewListener {
        override fun onMinPriceEditedFromTextInput(priceFilterViewModel: PriceFilterViewModel, minValue: Int) {
            hideKeyboard()
            sortFilterBottomSheetViewModel?.onMinPriceFilterEdited(priceFilterViewModel, minValue)
        }

        override fun onMaxPriceEditedFromTextInput(priceFilterViewModel: PriceFilterViewModel, maxValue: Int) {
            hideKeyboard()
            sortFilterBottomSheetViewModel?.onMaxPriceFilterEdited(priceFilterViewModel, maxValue)
        }

        override fun onPriceRangeClicked(priceFilterViewModel: PriceFilterViewModel, priceRangeOption: PriceOptionViewModel) {
            sortFilterBottomSheetViewModel?.onPriceRangeOptionClick(priceFilterViewModel, priceRangeOption)
        }
    }
    private val sortFilterBottomSheetAdapter = SortFilterBottomSheetAdapter(
            SortFilterBottomSheetTypeFactoryImpl(
                    sortViewListener, filterViewListener, priceFilterListener
            )
    )

    fun show(
            fragmentManager: FragmentManager,
            mapParameter: Map<String, String>?,
            dynamicFilterModel: DynamicFilterModel?,
            callback: Callback
    ) {
        if (mapParameter == null) return

        this.mapParameter = mapParameter
        this.dynamicFilterModel = dynamicFilterModel
        this.sortFilterCallback = callback

        show(fragmentManager, SORT_FILTER_BOTTOM_SHEET_TAG)
    }

    fun setResultCountText(buttonApplySortFilterText: String) {
        sortFilterBottomSheetView?.let {
            it.buttonApplySortFilter.isLoading = false
            it.buttonApplySortFilter.text = buttonApplySortFilterText
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
        sortFilterBottomSheetViewModel?.init(mapParameter, dynamicFilterModel)
    }

    private fun initView() {
        initBottomSheetSettings()

        setTitle(getString(R.string.discovery_filter))

        initBottomSheetAction()

        sortFilterBottomSheetView = View.inflate(requireContext(), R.layout.sort_filter_bottom_sheet, null)
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
        sortFilterBottomSheetView?.let {
            it.recyclerViewSortFilterBottomSheet?.adapter = sortFilterBottomSheetAdapter
            it.recyclerViewSortFilterBottomSheet?.layoutManager = LinearLayoutManager(activity, VERTICAL, false)
            it.recyclerViewSortFilterBottomSheet?.addOnScrollListener(createRecyclerViewOnScrollListener())
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
        sortFilterBottomSheetView?.buttonApplySortFilter?.setOnClickListener(this::onButtonApplySortFilterClicked)
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
        val selectedSortName = sortFilterBottomSheetViewModel?.selectedSortName ?: ""
        val applySortFilterModel = ApplySortFilterModel(mapParameter, selectedFilterMap, selectedSortMap, selectedSortName)

        sortFilterCallback?.onApplySortFilter(applySortFilterModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (showKnob) configureBottomSheetHeight()
        setBottomSheetActionBold()
        setStatusBarOverlayColor()
        observeViewModel()

        sortFilterBottomSheetViewModel?.onViewCreated()
    }

    private fun setStatusBarOverlayColor() {
        statusBarColorHelper = StatusBarColorHelper(requireActivity())
        statusBarColorHelper?.setStatusBarColor()
    }

    private fun observeViewModel() {
        sortFilterBottomSheetViewModel?.sortFilterListLiveData?.observe(viewLifecycleOwner, Observer {
            processSortFilterList(it)
        })

        sortFilterBottomSheetViewModel?.updateViewInPositionEventLiveData?.observe(viewLifecycleOwner, EventObserver {
            processUpdateViewInPosition(it)
        })

        sortFilterBottomSheetViewModel?.isLoadingLiveData?.observe(viewLifecycleOwner, Observer {
            processLoading(it)
        })

        sortFilterBottomSheetViewModel?.isButtonResetVisibleLiveData?.observe(viewLifecycleOwner, Observer {
            setActionResetVisibility(it)
        })

        sortFilterBottomSheetViewModel?.isViewExpandedLiveData?.observe(viewLifecycleOwner, Observer {
            expandBottomSheetView()
        })

        sortFilterBottomSheetViewModel?.trackPriceRangeClickEventLiveData?.observe(viewLifecycleOwner, EventObserver {
            FilterTracking.sendGeneralEvent(it)
        })

        sortFilterBottomSheetViewModel?.isLoadingForDynamicFilterLiveData?.observe(viewLifecycleOwner, Observer {
            processLoadingForDynamicFilter(it)
        })
    }

    private fun processSortFilterList(sortFilterList: List<Visitable<SortFilterBottomSheetTypeFactory>>) {
        sortFilterBottomSheetAdapter.setSortFilterList(sortFilterList)
        sortFilterBottomSheetView?.recyclerViewSortFilterBottomSheet?.scrollToPosition(0)
    }

    private fun processUpdateViewInPosition(position: Int) {
        sortFilterBottomSheetAdapter.notifyItemChanged(position)
    }

    private fun processLoading(isLoading: Boolean) {
        if (isLoading) {
            sortFilterBottomSheetView?.let {
                if (context.isDarkMode()) {
                    it.buttonApplyContainer?.background = context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_N50)
                } else {
                    it.buttonApplyContainer?.background = context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_N0)
                }
                it.buttonApplyContainer?.visibility = View.VISIBLE
                it.buttonApplySortFilter?.isLoading = true
                it.buttonApplySortFilter?.text = ""
            }

            sortFilterCallback?.getResultCount(sortFilterBottomSheetViewModel?.mapParameter ?: mapOf())
        }
        else {
            sortFilterBottomSheetView?.buttonApplyContainer?.visibility = View.GONE
        }
    }

    private fun setActionResetVisibility(isVisible: Boolean) {
        bottomSheetAction.post {
            bottomSheetAction.shouldShowWithAction(isVisible) {
                bottomSheetAction.text = getString(R.string.filter_button_reset_text)
                bottomSheetAction.setOnClickListener {
                    sortFilterBottomSheetViewModel?.resetSortAndFilter()
                }
            }
        }
    }

    private fun expandBottomSheetView() {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun processLoadingForDynamicFilter(isLoadingForDynamicFilter: Boolean) {
        if (isLoadingForDynamicFilter) showLoadingForDynamicFilter()
        else finishLoadingForDynamicFilter()
    }

    private fun showLoadingForDynamicFilter() {
        sortFilterBottomSheetView?.let {
            it.progressBarSortFilterBottomSheet?.show()
            it.recyclerViewSortFilterBottomSheet?.hide()
            it.buttonApplyContainer?.hide()
        }
    }

    private fun finishLoadingForDynamicFilter() {
        sortFilterBottomSheetView?.let {
            it.progressBarSortFilterBottomSheet?.hide()
            it.recyclerViewSortFilterBottomSheet?.show()
        }
    }

    override fun onDestroyView() {
        view?.let {
            if (it is ViewGroup) {
                it.removeAllViews()
            }
        }

        super.onDestroyView()
    }

    override fun onDestroy() {
        undoStatusBarOverlayColor()
        super.onDestroy()
    }

    private fun undoStatusBarOverlayColor() {
        statusBarColorHelper?.undoSetStatusBarColor()
        statusBarColorHelper = null
    }

    data class ApplySortFilterModel(
            val mapParameter: Map<String, String>,
            val selectedFilterMapParameter: Map<String, String>,
            val selectedSortMapParameter: Map<String, String>,
            val selectedSortName: String
    )

    interface Callback {
        fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel)

        fun getResultCount(mapParameter: Map<String, String>)
    }
}