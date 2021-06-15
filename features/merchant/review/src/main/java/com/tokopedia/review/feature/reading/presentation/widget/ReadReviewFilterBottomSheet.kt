package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class ReadReviewFilterBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG = "ReadReviewFilterBottomSheet Tag"
        fun newInstance(title: String, filterList: ArrayList<ListItemUnify>, readReviewFilterBottomSheetListener: ReadReviewFilterBottomSheetListener): ReadReviewFilterBottomSheet {
            return ReadReviewFilterBottomSheet().apply {
                setTitle(title)
                this.filterData = filterList
                this.listener = readReviewFilterBottomSheetListener
            }
        }
    }

    private var listUnify: ListUnify? = null
    private var submitButton: UnifyButton? = null

    private var filterData: ArrayList<ListItemUnify> = arrayListOf()
    private var listener: ReadReviewFilterBottomSheetListener? = null

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
    }

    private fun bindViews(view: View) {
        listUnify = view.findViewById(R.id.read_review_filter_list)
        submitButton = view.findViewById(R.id.read_review_submit_filter)
    }

    private fun setListUnifyData() {
        listUnify?.apply {
            setData(filterData)
            setOnItemClickListener { parent, view, position, id ->
                (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.listRightCheckbox?.toggle()
                (listUnify?.adapter?.getItem(position) as? ListItemUnify)?.listRightRadiobtn?.toggle()
            }
        }
    }

    private fun setSubmitButton() {
        submitButton?.setOnClickListener {
            listener?.onFilterSubmitted(getSelectedFilters())
        }
    }

    private fun getSelectedFilters(): List<ListItemUnify> {
        return listOf()
    }
}