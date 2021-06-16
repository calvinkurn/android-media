package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class ReadReviewFilterBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "ReadReviewFilterBottomSheet Tag"
        fun newInstance(title: String, filterList: ArrayList<ListItemUnify>, readReviewFilterBottomSheetListener: ReadReviewFilterBottomSheetListener, sortFilterType: SortFilterType): ReadReviewFilterBottomSheet {
            return ReadReviewFilterBottomSheet().apply {
                setTitle(title)
                this.filterData = filterList
                this.listener = readReviewFilterBottomSheetListener
                this.sortFilterType = sortFilterType
            }
        }
    }

    private var listUnify: ListUnify? = null
    private var submitButton: UnifyButton? = null

    private var filterData: ArrayList<ListItemUnify> = arrayListOf()
    private var listener: ReadReviewFilterBottomSheetListener? = null
    private var sortFilterType: SortFilterType? = null

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
    }

    private fun bindViews(view: View) {
        listUnify = view.findViewById(R.id.read_review_filter_list)
        submitButton = view.findViewById(R.id.read_review_submit_filter)
    }

    private fun setListUnifyData() {
        listUnify?.apply {
            setData(filterData)
            setOnItemClickListener { parent, view, position, id ->
                if (sortFilterType is SortFilterType.Sort) {
                    (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.listRightRadiobtn?.toggle()
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
            listener?.onFilterSubmitted(getSelectedFilters())
        }
    }

    private fun clearOtherItems(position: Int) {
        filterData.forEachIndexed { index, listItemUnify ->
            if (position != index) {
                (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked = false
            }
        }
    }

    private fun getSelectedFilters(): List<ListItemUnify> {
        val selectedFilters = mutableListOf<ListItemUnify>()
        if (sortFilterType is SortFilterType.Sort) {
            filterData.forEachIndexed { index, listItemUnify ->
                if ((listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightRadiobtn?.isChecked == true) {
                    selectedFilters.add(listItemUnify)
                }
            }
        } else {
            filterData.forEachIndexed { index, listItemUnify ->
                if ((listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked == true) {
                    selectedFilters.add(listItemUnify)
                }
            }
        }
        return selectedFilters
    }

    private fun setResetButton() {
        if (sortFilterType is SortFilterType.Sort) return
        setAction(getString(R.string.review_reading_reset_filter)) { resetFilters() }
    }

    private fun resetFilters() {
        filterData.forEachIndexed { index, listItemUnify ->
            (listUnify?.adapter?.getItem(index) as? ListItemUnify)?.listRightCheckbox?.isChecked = false
        }
    }
}