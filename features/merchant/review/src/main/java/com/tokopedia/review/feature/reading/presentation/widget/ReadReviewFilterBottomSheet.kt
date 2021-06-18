package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class ReadReviewFilterBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "ReadReviewFilterBottomSheet Tag"
        fun newInstance(title: String, filterList: ArrayList<ListItemUnify>, readReviewFilterBottomSheetListener: ReadReviewFilterBottomSheetListener, sortFilterBottomSheetType: SortFilterBottomSheetType, selectedFilter: List<String> = listOf(), selectedSort: String = "", index: Int): ReadReviewFilterBottomSheet {
            return ReadReviewFilterBottomSheet().apply {
                setTitle(title)
                this.filterData = filterList
                this.listener = readReviewFilterBottomSheetListener
                this.sortFilterBottomSheetType = sortFilterBottomSheetType
                this.selectedFilter = selectedFilter
                this.previouslySelectedSortOption = selectedSort
                this.index = index
            }
        }
    }

    private var listUnify: ListUnify? = null
    private var submitButton: UnifyButton? = null

    private var filterData: ArrayList<ListItemUnify> = arrayListOf()
    private var listener: ReadReviewFilterBottomSheetListener? = null
    private var sortFilterBottomSheetType: SortFilterBottomSheetType? = null
    private var selectedFilter: List<String> = listOf()
    private var previouslySelectedSortOption: String = ""
    private var index: Int = 0
    private var latestSelectedSortOption: ListItemUnify = ListItemUnify()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_read_review_filter, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setListUnifyData()
        setSubmitButton()
        setResetButton()
        listUnify?.onLoadFinish {
            if (isSortMode()) {
                setSelectedSort()
            } else {
                setSelectedFilter()
            }
        }
    }

    private fun bindViews(view: View) {
        listUnify = view.findViewById(R.id.read_review_filter_list)
        submitButton = view.findViewById(R.id.read_review_submit_filter)
    }

    private fun setListUnifyData() {
        listUnify?.apply {
            setData(filterData)
            setOnItemClickListener { _, _, position, _ ->
                if (isSortMode()) {
                    val selectedSortOption = (listUnify?.adapter?.getItem(position) as? ListItemUnify)
                    selectedSortOption?.listRightRadiobtn?.toggle()
                    if (selectedSortOption?.listRightRadiobtn?.isChecked == true) {
                        latestSelectedSortOption = selectedSortOption ?: ListItemUnify()
                    }
                    clearOtherItems(position)
                } else {
                    (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.listRightCheckbox?.toggle()
                }
            }
        }
    }

    private fun setSubmitButton() {
        submitButton?.setOnClickListener {
            dismiss()
            if (isSortMode()) {
                listener?.onSortSubmitted(latestSelectedSortOption)
            } else {
                listener?.onFilterSubmitted(getSelectedFilters(), sortFilterBottomSheetType
                        ?: SortFilterBottomSheetType.RatingFilterBottomSheet, index)
            }
        }
    }

    private fun setSelectedSort() {
        if (previouslySelectedSortOption.isBlank()) return
        filterData.forEachIndexed { index, listItemUnify ->
            if (listItemUnify.listTitleText == previouslySelectedSortOption) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked = true
            }
        }
    }

    private fun setSelectedFilter() {
        filterData.forEachIndexed { index, listItemUnify ->
            selectedFilter.forEach {
                if (listItemUnify.listTitleText == it) {
                    (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = true
                }
            }
        }
    }

    private fun clearOtherItems(position: Int) {
        filterData.forEachIndexed { index, _ ->
            if (position != index) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked = false
            }
        }
    }

    private fun getSelectedFilters(): List<ListItemUnify> {
        val selectedFilters = mutableListOf<ListItemUnify>()
        filterData.forEachIndexed { index, listItemUnify ->
            if ((listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked == true) {
                selectedFilters.add(listItemUnify)
            }
        }
        return selectedFilters
    }

    private fun setResetButton() {
        if (isSortMode()) return
        setAction(getString(R.string.review_reading_reset_filter)) { resetFilters() }
    }

    private fun resetFilters() {
        filterData.forEachIndexed { index, _ ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = false
        }
    }

    private fun isSortMode(): Boolean {
        return sortFilterBottomSheetType is SortFilterBottomSheetType.SortBottomSheet
    }
}