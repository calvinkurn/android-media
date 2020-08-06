package com.tokopedia.review.feature.inbox.container.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.review.feature.inbox.history.presentation.fragment.ReviewHistoryFragment
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment
import com.tokopedia.review.feature.inboxreview.presentation.fragment.InboxReviewFragment
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFragment

class ReviewInboxContainerAdapter(private val tabs: List<ReviewInboxTabs>,
                                  fragment: Fragment,
                                  private val bundle: Bundle? = null) : FragmentStateAdapter(fragment) {

    companion object {
        const val TAB_BUYER_REVIEW = 3
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(tabs[position]) {
            is ReviewInboxTabs.ReviewInboxPending -> {
                ReviewPendingFragment.createNewInstance()
            }
            is ReviewInboxTabs.ReviewInboxHistory -> {
                ReviewHistoryFragment.createNewInstance()
            }
            is ReviewInboxTabs.ReviewInboxSeller -> {
                // Change this to seller fragment
                InboxReputationFragment.createInstance(TAB_BUYER_REVIEW)
            }
            is ReviewInboxTabs.ReviewRatingProduct -> {
                val fragment = RatingProductFragment.createInstance()
                fragment.arguments = bundle
                fragment
            }
            is ReviewInboxTabs.ReviewBuyer -> {
                InboxReviewFragment.createInstance()
            }
            is ReviewInboxTabs.ReviewPenaltyAndReward -> {
                // Change this to penalty & reward fragment
                ReviewHistoryFragment.createNewInstance()
            }
        }
    }
}