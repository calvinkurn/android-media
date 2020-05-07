package com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.reviewseller.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicAdapter = TopicListAdapter(this)
        sortAdapter = SortListAdapter(this)
        setAdapter()
        showDialog()
    }

    override fun setAdapter() {
        sortAdapter?.setSortFilter(sortTopicData)
        rvSortFilter?.adapter = sortAdapter

        topicAdapter?.setTopicFilter(filterTopicData)
        rvTopicFilter?.adapter = topicAdapter
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