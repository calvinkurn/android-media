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
        fun newInstance(title: String, filterList: ArrayList<ListItemUnify>, readReviewFilterBottomSheetListener: ReadReviewFilterBottomSheetListener, sortFilterBottomSheetType: SortFilterBottomSheetType, selectedFilter: Set<String> = setOf(), selectedSort: String = "", index: Int): ReadReviewFilterBottomSheet {
            return ReadReviewFilterBottomSheet().apply {
                setTitle(title)
                this.filterData = filterList
                this.listener = readReviewFilterBottomSheetListener
                this.sortFilterBottomSheetType = sortFilterBottomSheetType
                this.previouslySelectedFilter = selectedFilter
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
    private var previouslySelectedFilter: Set<String> = setOf()
    private var previouslySelectedSortOption: String = ""
    private var index: Int = 0
    private var latestSelectedSortOption: ListItemUnify = ListItemUnify()
    private var selectedFilters: Set<ListItemUnify> = setOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_read_review_filter, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setListUnifyData()
        if (isSortMode()) {
            setSortItemListener()
            setSubmitSortButton()
            listUnify?.onLoadFinish {
                configSortData()
            }
        } else {
            setFilterItemListener()
            setSubmitFilterButton()
            setResetButton()
            listUnify?.onLoadFinish {
                configFilterData()
            }
        }
    }

    private fun bindViews(view: View) {
        listUnify = view.findViewById(R.id.read_review_filter_list)
        submitButton = view.findViewById(R.id.read_review_submit_filter)
    }

    private fun setListUnifyData() {
        listUnify?.setData(filterData)
    }

    private fun setSortItemListener() {
        listUnify?.setOnItemClickListener { _, _, position, _ ->
            val selectedSortOption = (listUnify?.adapter?.getItem(position) as? ListItemUnify)
            selectedSortOption?.listRightRadiobtn?.toggle()
        }
    }

    private fun setFilterItemListener() {
        listUnify?.setOnItemClickListener { _, _, position, _ ->
            (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.let {
                it.listRightCheckbox?.let { checkbox ->
                    checkbox.toggle()
                }
            }
        }
    }

    private fun configSortData() {
        filterData.forEachIndexed { index, listItemUnify ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.setOnCheckedChangeListener { _, isChecked ->
                onSortCheckChange(isChecked, listItemUnify, index)
            }
            if (listItemUnify.listTitleText == previouslySelectedSortOption && previouslySelectedSortOption.isNotBlank()) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked = true
            }
        }
    }

    private fun configFilterData() {
        filterData.forEachIndexed { index, listItemUnify ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.setOnCheckedChangeListener { _, isChecked ->
                onFilterCheckChange(isChecked, listItemUnify)
            }
            if (previouslySelectedFilter.contains(listItemUnify.listTitleText) && previouslySelectedFilter.isNotEmpty()) {
                selectedFilters = selectedFilters.plus(listItemUnify)
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = true
            }
        }
    }

    private fun setSubmitSortButton() {
        submitButton?.setOnClickListener {
            dismiss()
            listener?.onSortSubmitted(latestSelectedSortOption)
        }
    }

    private fun setSubmitFilterButton() {
        submitButton?.setOnClickListener {
            dismiss()
            listener?.onFilterSubmitted(selectedFilters, sortFilterBottomSheetType
                    ?: SortFilterBottomSheetType.RatingFilterBottomSheet, index)
        }
    }

    private fun clearOtherItems(position: Int) {
        filterData.forEachIndexed { index, _ ->
            if (position != index) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked = false
            }
        }
    }

    private fun setResetButton() {
        setAction(getString(R.string.review_reading_reset_filter)) { resetFilters() }
    }

    private fun resetFilters() {
        filterData.forEachIndexed { index, _ ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = false
        }
        clearAllFilters()
    }

    private fun clearAllFilters() {
        selectedFilters = setOf()
    }

    private fun isSortMode(): Boolean {
        return sortFilterBottomSheetType is SortFilterBottomSheetType.SortBottomSheet
    }

    private fun onFilterCheckChange(isChecked: Boolean, itemUnify: ListItemUnify) {
        selectedFilters = if (isChecked) {
            selectedFilters.plus(itemUnify)
        } else {
            selectedFilters.minus(itemUnify)
        }
    }

    private fun onSortCheckChange(isChecked: Boolean, selectedSortOption: ListItemUnify, position: Int) {
        if (isChecked) {
            latestSelectedSortOption = selectedSortOption
            clearOtherItems(position)
        }
    }
}