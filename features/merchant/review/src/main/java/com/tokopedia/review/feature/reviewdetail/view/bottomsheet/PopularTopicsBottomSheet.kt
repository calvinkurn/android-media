package com.tokopedia.review.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.review.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.review.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Yehezkiel on 27/04/20
 */
class PopularTopicsBottomSheet(mActivity: FragmentActivity?,
                               private val tracking: ProductReviewDetailTracking,
                               private val userSession: UserSessionInterface,
                               private val productID: Int,
                               listenerTopics: (List<SortFilterItemWrapper>, List<SortItemUiModel>) -> Unit) :
        BaseTopicsBottomSheet(mActivity, listenerTopics), TopicSortFilterListener.Topic, TopicSortFilterListener.Sort {

    init {
        setAction(ACTION_TITLE) {
            resetFilterClicked()
        }
        setCloseClickListener {
            tracking.eventClickCloseBottomSheetSortFilter(userSession.shopId.orEmpty(), productID.toString())
            dismiss()
        }

        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(view: View, state: Int) {
                    if (state == BottomSheetBehavior.STATE_COLLAPSED || state == BottomSheetBehavior.STATE_HIDDEN) {
                        //down
                        if (state == BottomSheetBehavior.STATE_HIDDEN) {
                            tracking.eventSwipeBottomSheetSortFilterTopics(
                                    userSession.shopId.orEmpty(),
                                    productID.toString(), "down")
                            dismiss()
                        }
                    } else if (state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_DRAGGING) {
                        //up
                        if(state == BottomSheetBehavior.STATE_EXPANDED) {
                            tracking.eventSwipeBottomSheetSortFilterTopics(
                                    userSession.shopId.orEmpty(),
                                    productID.toString(), "up")
                        }
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicAdapter = TopicListAdapter(this)
        sortAdapter = SortListAdapter(this)
        setAdapter()
        showDialog()
    }

    override fun setAdapter() {
        rvSortFilter?.adapter = sortAdapter
        sortAdapter?.setSortFilter(sortTopicData)

        if (filterTopicData.isEmpty()) {
            tvTopicTitle?.hide()
        }
        rvTopicFilter?.adapter = topicAdapter
        topicAdapter?.setTopicFilter(filterTopicData)
    }

    private fun resetFilterClicked() {
        tracking.eventClickResetOnBottomSheet(userSession.shopId.orEmpty(), productID.toString())
        topicAdapter?.resetSortFilter()
        sortAdapter?.resetSortFilter()
    }

    override fun onTopicClicked(chipType: String, adapterPosition: Int) {
        val isSelected = chipType == ChipsUnify.TYPE_SELECTED
        tracking.eventClickFilterOnBottomSheet(
                userSession.shopId.orEmpty(),
                productID.toString(),
                topicAdapter?.sortFilterList?.getOrNull(adapterPosition)?.titleUnformated.orEmpty(),
                isSelected.toString())
        topicAdapter?.updateTopicFilter(isSelected, adapterPosition)
    }

    override fun onSortClicked(chipType: String, adapterPosition: Int) {
        tracking.eventClickSortOnBottomSheet(
                userSession.shopId.orEmpty(),
                productID.toString(),
                sortAdapter?.sortFilterListUiModel?.getOrNull(adapterPosition)?.title.orEmpty())
        sortAdapter?.updatedSortFilter(adapterPosition)
    }
}