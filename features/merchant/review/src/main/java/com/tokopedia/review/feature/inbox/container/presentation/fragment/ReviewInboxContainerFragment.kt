package com.tokopedia.review.feature.inbox.container.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.inbox.container.analytics.ReviewInboxContainerTracking
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.review.feature.inbox.container.di.DaggerReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.di.ReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.presentation.adapter.ReviewInboxContainerAdapter
import com.tokopedia.review.feature.inbox.container.presentation.viewmodel.ReviewInboxContainerViewModel
import kotlinx.android.synthetic.main.fragment_review_inbox_container.*
import javax.inject.Inject

class ReviewInboxContainerFragment : BaseDaggerFragment(), HasComponent<ReviewInboxContainerComponent>, OnBackPressedListener {

    companion object {
        const val PENDING_TAB_INDEX = 0
        const val HISTORY_TAB_INDEX = 1

        fun createNewInstance() : ReviewInboxContainerFragment{
            return ReviewInboxContainerFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewInboxContainerViewModel

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ReviewInboxContainerComponent? {
        return activity?.run {
            DaggerReviewInboxContainerComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_review_inbox_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeReviewTabs()
        setupTabLayout()
        viewModel.getTabCounter()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewTabs)
    }


    override fun onBackPressed() {
        ReviewInboxContainerTracking.eventOnClickBackButton(viewModel.userId)
    }

    private fun setupViewPager(tabTitles: List<String>) {
        reviewInboxViewPager.adapter = viewModel.reviewTabs.value?.let { ReviewInboxContainerAdapter(it,this) }
        tabTitles.forEach {
            reviewInboxTabs.addNewTab(it)
        }
        reviewInboxViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                reviewInboxTabs.getUnifyTabLayout().selectTab(reviewInboxTabs.getUnifyTabLayout().getTabAt(position))
            }
        })
    }

    private fun setupTabLayout() {
        reviewInboxTabs.customTabMode = TabLayout.MODE_SCROLLABLE
        reviewInboxTabs.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                // No Op
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // No Op
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                reviewInboxViewPager.setCurrentItem(tab.position, true)
                when(tab.position) {
                    PENDING_TAB_INDEX -> {
                        ReviewInboxContainerTracking.eventOnClickReviewPendingTab(viewModel.userId)
                    }
                    HISTORY_TAB_INDEX -> {
                        ReviewInboxContainerTracking.eventOnClickReviewHistoryTab(viewModel.userId)
                    }
                    else -> {
                        ReviewInboxContainerTracking.eventOnClickReviewSellerTab(viewModel.userId)
                    }
                }
            }
        })
    }

    private fun observeReviewTabs() {
        viewModel.reviewTabs.observe(viewLifecycleOwner, Observer { tabs ->
            val tabTitles = mutableListOf<String>()
            tabs.forEach { tab ->
                when(tab) {
                    is ReviewInboxTabs.ReviewInboxPending -> {
                        if(tab.counter.isNotBlank() && tab.counter.toIntOrZero() != 0) {
                            tabTitles.add(getString(R.string.review_pending_tab_title, tab.counter))
                        } else {
                            tabTitles.add(getString(R.string.review_pending_tab_title_no_count))
                        }
                    }
                    is ReviewInboxTabs.ReviewInboxHistory -> {
                        tabTitles.add(getString(R.string.review_history_tab_title))
                    }
                    is ReviewInboxTabs.ReviewInboxSeller -> {
                        tabTitles.add(getString(R.string.review_seller_tab_title))
                    }
                }
            }
            setupViewPager(tabTitles)
        })
    }

}