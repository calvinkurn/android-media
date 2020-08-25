package com.tokopedia.review.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.review.R
import com.tokopedia.review.feature.inboxreview.analytics.InboxReviewTracking
import com.tokopedia.review.feature.inboxreview.presentation.adapter.RatingListAdapter
import com.tokopedia.review.feature.inboxreview.presentation.adapter.RatingListListener
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class FilterRatingBottomSheet(mActivity: FragmentActivity?,
                              val listener: (List<ListItemRatingWrapper>) -> Unit) : BottomSheetUnify(), RatingListListener {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter Rating"
        const val ACTION_TITLE = "Reset"
    }

    private var rvRatingFilter: RecyclerView? = null
    private var btnFilter: UnifyButton? = null

    private var ratingListFilter: MutableList<ListItemRatingWrapper>? = null

    private var ratingFilterAdapter: RatingListAdapter? = null
    private var shopId = ""

    init {
        val view = View.inflate(mActivity, R.layout.bottom_sheet_filter_rating_inbox_review, null)
        rvRatingFilter = view.findViewById(R.id.rvRatingInboxReview)
        btnFilter = view.findViewById(R.id.btnFilter)
        isHideable = true
        setTitle(BOTTOM_SHEET_TITLE)
        setAction(ACTION_TITLE) {
            resetRatingFilter()
        }
        setCloseClickListener {
            dismiss()
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
            InboxReviewTracking.eventClickOutsideRatingFilterBottomSheet(shopId)
        }
        setChild(view)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingFilterAdapter = RatingListAdapter(this)
        initAdapter()

        btnFilter?.setOnClickListener {
            val starSelected = ratingFilterAdapter?.ratingFilterList?.filter { it.isSelected }?.joinToString(separator = ",") { it.sortValue }.orEmpty()
            InboxReviewTracking.eventClickSelectedRatingFilterBottomSheet(starSelected, shopId)
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        ratingFilterAdapter?.ratingFilterList?.toList()?.let {
            listener.invoke(it)
        }
    }

    override fun onItemRatingClicked(starSelected: String, selected: Boolean, adapterPosition: Int) {
        InboxReviewTracking.eventClickItemRatingFilterBottomSheet(starSelected, selected.toString(), shopId)
        ratingFilterAdapter?.updateRatingFilter(selected, adapterPosition)
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvRatingFilter?.layoutManager = layoutManager
        rvRatingFilter?.adapter = ratingFilterAdapter
    }

    private fun resetRatingFilter() {
        val starSelected = ratingFilterAdapter?.ratingFilterList?.filter { it.isSelected }?.joinToString(separator = ",") { it.sortValue }.orEmpty()
        InboxReviewTracking.eventClickResetRatingFilterBottomSheet(starSelected, shopId)
        ratingFilterAdapter?.resetRatingFilter()
    }

    fun setRatingFilterList(filterRatingWrapper: List<ListItemRatingWrapper>) {
        ratingListFilter = filterRatingWrapper.toMutableList()
    }

    fun setShopId(shopId: String) {
        this.shopId = shopId
    }

}