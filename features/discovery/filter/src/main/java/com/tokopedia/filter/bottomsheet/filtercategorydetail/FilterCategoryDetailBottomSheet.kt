package com.tokopedia.filter.bottomsheet.filtercategorydetail

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.updateScrollingChild
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.helper.addItemDecorationIfNotExists
import com.tokopedia.filter.common.helper.configureBottomSheetHeight
import com.tokopedia.filter.common.helper.copyParcelable
import com.tokopedia.filter.common.helper.createFilterDividerItemDecoration
import com.tokopedia.filter.common.helper.setBottomSheetActionBold
import com.tokopedia.filter.common.helper.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.filter_category_detail_bottom_sheet.view.*

internal class FilterCategoryDetailBottomSheet :
        BottomSheetUnify(),
        FilterCategoryLevelOneAdapter.Callback,
        FilterCategoryDetailCallback {

    companion object {
        private const val FILTER_CATEGORY_DETAIL_BOTTOM_SHEET_TAG = "FILTER_CATEGORY_DETAIL_BOTTOM_SHEET_TAG"
    }

    private var filter: Filter? = null
    private var selectedCategoryFilterValue = ""
    private var callback: Callback? = null

    private var filterCategoryDetailBottomSheetView: View? = null
    private var filterCategoryLevelOneAdapter: FilterCategoryLevelOneAdapter? = null
    private val filterCategoryLevelTwoAdapter = FilterCategoryLevelTwoAdapter(this)
    private var filterCategoryDetailViewModel: FilterCategoryDetailViewModel? = null

    private val itemTouchListener: RecyclerView.OnItemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            bottomSheet.updateScrollingChild(rv)
            return false
        }
    }

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
        filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderRecyclerView?.let {
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val itemDecoration = createFilterDividerItemDecoration(it.context, layoutManager.orientation, 0)

            filterCategoryLevelOneAdapter = FilterCategoryLevelOneAdapter(this)
            it.adapter = filterCategoryLevelOneAdapter
            it.layoutManager = layoutManager
            it.addItemDecorationIfNotExists(itemDecoration)
            it.addOnItemTouchListener(itemTouchListener)
        }
    }

    private fun initContentRecyclerView() {
        filterCategoryDetailBottomSheetView?.filterCategoryDetailContentRecyclerView?.let {
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            val itemDecoration = createFilterDividerItemDecoration(it.context, layoutManager.orientation, 0)

            it.adapter = filterCategoryLevelTwoAdapter
            it.layoutManager = layoutManager
            it.addItemDecorationIfNotExists(itemDecoration)
            it.addOnItemTouchListener(itemTouchListener)
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

        initButtonReset()

        bottomSheetClose.setMargin(marginLeft = 16.toPx(), marginTop = 4.toPx(), marginRight = 12.toPx())

        configureBottomSheetHeight()

        observeViewModel()

        filterCategoryDetailViewModel?.onViewCreated()
    }

    private fun initButtonReset() {
        bottomSheetAction.setMargin(marginRight = 16.toPx())
        setAction(getString(R.string.filter_button_reset_text)) { filterCategoryDetailViewModel?.onResetButtonClicked() }
        setBottomSheetActionBold()
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

        filterCategoryDetailViewModel?.isButtonResetVisibleLiveData?.observe(viewLifecycleOwner, Observer {
            setActionResetVisibility(it)
        })

        filterCategoryDetailViewModel?.isButtonSaveVisibleLiveData?.observe(viewLifecycleOwner, Observer {
            filterCategoryDetailBottomSheetView?.buttonApplyFilterCategoryDetailContainer?.showWithCondition(it)
        })
    }

    private fun processHeaderViewModelList(filterCategoryLevelOneViewModelList: List<FilterCategoryLevelOneViewModel>) {
        filterCategoryLevelOneAdapter?.setList(filterCategoryLevelOneViewModelList)
        filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderRecyclerView?.post {
            filterCategoryLevelOneAdapter?.scrollToSelected(filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderRecyclerView)
        }
    }

    private fun updateHeaderViewInPosition(position: Int) {
        val adapter = filterCategoryLevelOneAdapter ?: return

        adapter.notifyItemChanged(position)
        adapter.scrollToSelectedIfNotFullyVisible(
                filterCategoryDetailBottomSheetView?.filterCategoryDetailHeaderRecyclerView,
                position
        )
    }

    private fun setActionResetVisibility(isVisible: Boolean) {
        bottomSheetAction.post {
            bottomSheetAction.showWithCondition(isVisible)
        }
    }

    private fun processContentViewModelList(filterCategoryLevelTwoViewModelList: List<FilterCategoryLevelTwoViewModel>) {
        filterCategoryLevelTwoAdapter.setList(filterCategoryLevelTwoViewModelList)
    }

    override fun onHeaderItemClick(filterCategoryLevelOneViewModel: FilterCategoryLevelOneViewModel) {
        filterCategoryDetailViewModel?.onHeaderItemClick(filterCategoryLevelOneViewModel)
    }

    override fun onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        filterCategoryDetailViewModel?.onFilterCategoryClicked(filterCategoryLevelTwoViewModel)
    }

    override fun onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        filterCategoryDetailViewModel?.onFilterCategoryClicked(filterCategoryLevelThreeViewModel)
    }

    interface Callback {
        fun onApplyButtonClicked(selectedFilterValue: String)
    }
}