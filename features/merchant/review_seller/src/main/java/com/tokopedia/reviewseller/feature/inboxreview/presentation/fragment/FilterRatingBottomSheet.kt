package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.RatingListAdapter
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.RatingListListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class FilterRatingBottomSheet(private val listener: (List<ListItemRatingWrapper>) -> Unit) : BottomSheetUnify(), RatingListListener {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter Rating"
        const val ACTION_TITLE = "Reset"
    }

    private var rvRatingFilter: RecyclerView? = null
    private var btnFilter: UnifyButton? = null

    private var ratingListFilter: MutableList<ListItemRatingWrapper>? = null

    private var ratingFilterAdapter: RatingListAdapter? = null

    private var filterRatingBottomSheetListener: FilterRatingBottomSheetListener? = null

    init {
        val view = View.inflate(context, R.layout.bottom_sheet_filter_rating_inbox_review, null)
        rvRatingFilter = view.findViewById(R.id.rvRatingInboxReview)
        btnFilter = view.findViewById(R.id.btnFilter)
        isHideable = true
        setTitle(BOTTOM_SHEET_TITLE)
        setAction(ACTION_TITLE) {
            resetRatingFilter()
        }
        setCloseClickListener {
            filterRatingBottomSheetListener?.onBottomSheetDismiss()
            dismissAllowingStateLoss()
        }
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if(newState == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheet.requestLayout()
                        bottomSheet.invalidate()
                        ratingListFilter?.let { ratingFilterAdapter?.setRatingFilter(it) }
                    }
                }
            })
        }
        setOnDismissListener {
            filterRatingBottomSheetListener?.onBottomSheetDismiss()
        }
        setChild(view)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingFilterAdapter = RatingListAdapter(this)
        initAdapter()

        btnFilter?.setOnClickListener {
            listener.invoke(ratingFilterAdapter?.ratingFilterList?.toList() ?: listOf())
        }
    }

    override fun onItemRatingClicked(selected: Boolean, adapterPosition: Int) {
        ratingFilterAdapter?.updateRatingFilter(selected, adapterPosition)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvRatingFilter?.layoutManager = layoutManager
        rvRatingFilter?.adapter = ratingFilterAdapter
    }


    private fun resetRatingFilter() {
        ratingFilterAdapter?.resetRatingFilter()
    }

    fun setRatingFilterList(filterRatingWrapper: List<ListItemRatingWrapper>) {
        ratingListFilter = filterRatingWrapper.toMutableList()
    }

    fun setFilterRatingBottomSheetListener(listener: FilterRatingBottomSheetListener) {
        this.filterRatingBottomSheetListener = listener
    }

    interface FilterRatingBottomSheetListener {
        fun onBottomSheetDismiss()
    }

}