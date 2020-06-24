package com.tokopedia.review.feature.inbox.container.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.review.feature.inbox.history.presentation.fragment.ReviewHistoryFragment
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment

class ReviewInboxContainerAdapter(private val tabs: List<ReviewInboxTabs>,
                                  fragment: Fragment) : FragmentStateAdapter(fragment) {

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
                ReviewHistoryFragment.createNewInstance()
            }
        }
    }
}