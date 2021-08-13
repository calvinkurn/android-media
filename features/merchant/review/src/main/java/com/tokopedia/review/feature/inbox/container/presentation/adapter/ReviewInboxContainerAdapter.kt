package com.tokopedia.review.feature.inbox.container.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.review.feature.inbox.container.presentation.listener.ReviewInboxListener
import com.tokopedia.review.feature.inbox.history.presentation.fragment.ReviewHistoryFragment
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment

class ReviewInboxContainerAdapter(private val tabs: MutableList<ReviewInboxTabs>,
                                  fragment: Fragment,
                                  private val reviewInboxListener: ReviewInboxListener,
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
                val fragment = ReviewPendingFragment.createNewInstance(reviewInboxListener)
                fragment.arguments = bundle
                fragment
            }
            is ReviewInboxTabs.ReviewInboxHistory -> {
                ReviewHistoryFragment.createNewInstance()
            }
            is ReviewInboxTabs.ReviewInboxSeller -> {
                InboxReputationFragment.createInstance(TAB_BUYER_REVIEW)
            }
        }
    }
}