package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.RatingListAdapter
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.RatingListListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class FilterRatingBottomSheet(private val mActivity: FragmentActivity?,
                              private val listener: (List<ListItemRatingWrapper>) -> Unit): BottomSheetUnify(), RatingListListener {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter Rating"
        const val ACTION_TITLE = "Reset"
    }

    private var rvRatingFilter: RecyclerView? = null
    private var btnFilter: UnifyButton? = null
    private var ratingFilterList: List<ListItemRatingWrapper> = mutableListOf()

    private val ratingFilterAdapter by lazy { RatingListAdapter(this) }

    init {
        val view = View.inflate(mActivity, R.layout.bottom_sheet_filter_rating_inbox_review, null)
        rvRatingFilter = view.findViewById(R.id.rvRatingInboxReview)
        btnFilter = view.findViewById(R.id.btnFilter)
        isDragable = true
        isFullpage = true
        setTitle(BOTTOM_SHEET_TITLE)
        setAction(ACTION_TITLE) {
            resetRatingFilter()
        }
        setCloseClickListener {
            dismiss()
        }
        setChild(view)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        btnFilter?.setOnClickListener {
            listener.invoke(ratingFilterAdapter.ratingFilterList?.toList() ?: listOf())
        }
    }

    override fun onItemRatingClicked(selected: Boolean, adapterPosition: Int) {
        ratingFilterAdapter.updateRatingFilter(selected, adapterPosition)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context)
        rvRatingFilter?.layoutManager = layoutManager
        ratingFilterAdapter.setRatingFilter(ratingFilterList)
        rvRatingFilter?.adapter = ratingFilterAdapter
    }

    private fun resetRatingFilter() {
        ratingFilterAdapter.resetRatingFilter()
    }

    fun setRatingFilterList(filterRatingWrapper: List<ListItemRatingWrapper>) {
        this.ratingFilterList = filterRatingWrapper
    }

    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, BOTTOM_SHEET_TITLE)
        }
    }

}