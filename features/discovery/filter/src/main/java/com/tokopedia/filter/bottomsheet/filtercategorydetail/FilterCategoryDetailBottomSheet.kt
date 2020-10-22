package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.helper.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.filter_category_detail_bottom_sheet.view.*

internal class FilterCategoryDetailBottomSheet :
        BottomSheetUnify(),
        FilterCategoryLevelOneScrollViewAdapter.Callback,
        FilterCategoryDetailCallback {

    companion object {
        private const val FILTER_CATEGORY_DETAIL_BOTTOM_SHEET_TAG = "FILTER_CATEGORY_DETAIL_BOTTOM_SHEET_TAG"
    }

    private var filter: Filter? = null
    private var selectedCategoryFilterValue = ""
    private var callback: Callback? = null

    private var filterCategoryDetailBottomSheetView: View? = null
    private var filterCategoryLevelOneScrollViewAdapter: FilterCategoryLevelOneScrollViewAdapter? = null
    private val filterCategoryLevelTwoAdapter = FilterCategoryLevelTwoAdapter(this)
    private var filterCategoryDetailViewModel: FilterCategoryDetailViewModel? = null

    fun show(fragmentManager: FragmentManager, filter: Filter, selectedCategoryFilterValue: String, callback: Callback) {
        this.filter = filter.copyParcelable()
        this.selectedCategoryFilterValue = selectedCategoryFilterValue
        this.callback = callback

        show(fragmentManager, FILTER_CATEGORY_DETAIL_BOTTOM_SHEET_TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initView()
    }

    private fun initViewModel() {
        filterCategoryDetailViewModel = FilterCategoryDetailViewModel()
        filterCategoryDetailViewModel?.init(filter, selectedCategoryFilterValue)
    }

    private fun initView() {
        initViewSettings()

        setTitle(filter?.title ?: "")

        filterCategoryDetailBottomSheetView = View.inflate(requireContext(), R.layout.filter_category_detail_bottom_sheet, null)
        setChild(filterCategoryDetailBottomSheetView)

        initHeaderView()
        initContentRecyclerView()
        initButtonApplyCategoryFilter()
    }

    private fun initViewSettings() {
        isDragable = false
        isHideable = false
        showKnob = false
        showCloseIcon = true
    }

    private fun initHeaderView() {
        filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderItems?.let {
            filterCategoryLevelOneScrollViewAdapter = FilterCategoryLevelOneScrollViewAdapter(it, this)
        }
    }

    private fun initContentRecyclerView() {
        filterCategoryDetailBottomSheetView?.let {
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val itemDecoration = createFilterDividerItemDecoration(it.context, layoutManager.orientation, 0)

            it.filterCategoryDetailContentRecyclerView?.adapter = filterCategoryLevelTwoAdapter
            it.filterCategoryDetailContentRecyclerView?.layoutManager = layoutManager
            it.filterCategoryDetailContentRecyclerView?.addItemDecorationIfNotExists(itemDecoration)
        }
    }

    private fun initButtonApplyCategoryFilter() {
        filterCategoryDetailBottomSheetView?.buttonApplyFilterCategoryDetail?.setOnClickListener {
            callback?.onApplyButtonClicked(filterCategoryDetailViewModel?.selectedCategoryFilterValue ?: "")
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetWrapper.setPadding(0, bottomSheetWrapper.paddingTop, 0, bottomSheetWrapper.paddingBottom)

        bottomSheetAction.setMargin(marginRight = 16.toPx())

        bottomSheetClose.setMargin(marginLeft = 16.toPx(), marginTop = 4.toPx(), marginRight = 12.toPx())

        configureBottomSheetHeight()

        observeViewModel()

        filterCategoryDetailViewModel?.onViewCreated()
    }

    private fun observeViewModel() {
        filterCategoryDetailViewModel?.headerViewModelListLiveData?.observe(viewLifecycleOwner, Observer {
            processHeaderViewModelList(it)
        })

        filterCategoryDetailViewModel?.updateHeaderInPositionEventLiveData?.observe(viewLifecycleOwner, EventObserver {
            updateHeaderViewInPosition(it)
        })

        filterCategoryDetailViewModel?.contentViewModelListLiveData?.observe(viewLifecycleOwner, Observer {
            processContentViewModelList(it)
        })

        filterCategoryDetailViewModel?.updateContentInPositionEventLiveData?.observe(viewLifecycleOwner, EventObserver {
            filterCategoryLevelTwoAdapter.notifyItemChanged(it)
        })

        filterCategoryDetailViewModel?.isButtonSaveVisibleLiveData?.observe(viewLifecycleOwner, Observer {
            filterCategoryDetailBottomSheetView?.buttonApplyFilterCategoryDetailContainer?.visibility = View.VISIBLE
        })
    }

    private fun processHeaderViewModelList(filterCategoryLevelOneViewModelList: List<FilterCategoryLevelOneViewModel>) {
        filterCategoryLevelOneScrollViewAdapter?.setList(filterCategoryLevelOneViewModelList)

        filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderScrollView?.post {
            filterCategoryLevelOneScrollViewAdapter?.scrollToSelected(filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderScrollView)
        }
    }

    private fun updateHeaderViewInPosition(position: Int) {
        filterCategoryLevelOneScrollViewAdapter?.notifyItemChanged(position)
    }

    private fun processContentViewModelList(filterCategoryLevelTwoViewModelList: List<FilterCategoryLevelTwoViewModel>) {
        filterCategoryLevelTwoAdapter.setList(filterCategoryLevelTwoViewModelList)
    }

    override fun onHeaderItemClick(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
        filterCategoryDetailViewModel?.onHeaderItemClick(filterCategoryLevelOneViewModel)
    }

    override fun onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel, isChecked: Boolean) {
        filterCategoryDetailViewModel?.onFilterCategoryClicked(filterCategoryLevelTwoViewModel, isChecked)
    }

    override fun onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel, isChecked: Boolean) {
        filterCategoryDetailViewModel?.onFilterCategoryClicked(filterCategoryLevelThreeViewModel, isChecked)
    }

    interface Callback {
        fun onApplyButtonClicked(selectedFilterValue: String)
    }
}