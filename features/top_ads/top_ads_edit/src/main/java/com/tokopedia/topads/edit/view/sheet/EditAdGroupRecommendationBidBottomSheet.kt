package com.tokopedia.topads.edit.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class EditAdGroupRecommendationBidBottomSheet: BottomSheetUnify()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_edit_sheet_edit_ad_group_recommendation_bid, null)
        isHideable = true
        showCloseIcon = true
        setChild(contentView)
        setTitle(getString(com.tokopedia.topads.edit.R.string.top_ads_edit_ad_group_recommendation_bid_bottom_sheet_title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        context?.let {

        }
    }

    fun show(
        fragmentManager: FragmentManager,
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "EDIT_AD_GROUP_RECOMMENDATION_BID_BOTTOM_SHEET_TAG"
        fun newInstance(): EditAdGroupRecommendationBidBottomSheet = EditAdGroupRecommendationBidBottomSheet()
    }
}