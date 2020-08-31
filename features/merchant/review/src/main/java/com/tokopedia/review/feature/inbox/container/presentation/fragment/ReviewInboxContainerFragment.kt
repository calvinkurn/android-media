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
import com.tokopedia.config.GlobalConfig
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
        private const val IS_DIRECTLY_GO_TO_RATING = "is_directly_go_to_rating"

        fun createNewInstance(goToReputationHistory: Boolean) : ReviewInboxContainerFragment{
            return ReviewInboxContainerFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_DIRECTLY_GO_TO_RATING, !goToReputationHistory)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewInboxContainerViewModel

    private var previouslySelectedTab = 0

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
        if (GlobalConfig.isSellerApp()) {
            val reviewSellerBundle = Bundle()
            reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, arguments?.getBoolean(IS_DIRECTLY_GO_TO_RATING) ?: true)
            setupSellerAdapter(reviewSellerBundle)
            setupViewPager(listOf(getString(R.string.title_review_rating_product), getString(R.string.title_review_inbox), getString(R.string.title_reputation_history)))
        } else {
            observeReviewTabs()
            viewModel.getTabCounter()
        }
        setupTabLayout()
    }

    override fun onDestroy() {
        removeObservers(viewModel.reviewTabs)
        super.onDestroy()
    }


    override fun onBackPressed() {
        ReviewInboxContainerTracking.eventOnClickBackButton(viewModel.getUserId())
    }

    private fun setupSellerAdapter(bundle: Bundle) {
        val tabs: List<ReviewInboxTabs> = listOf(ReviewInboxTabs.ReviewRatingProduct, ReviewInboxTabs.ReviewBuyer, ReviewInboxTabs.ReviewPenaltyAndReward)
        reviewInboxViewPager.adapter = ReviewInboxContainerAdapter(tabs, this, bundle)
    }

    private fun setupBuyerAdapter() {
        reviewInboxViewPager.adapter = viewModel.reviewTabs.value?.let { ReviewInboxContainerAdapter(it,this) }
    }

    private fun setupViewPager(tabTitles: List<String>) {
        tabTitles.forEach {
            reviewInboxTabs.addNewTab(it)
        }
        reviewInboxViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                reviewInboxTabs.getUnifyTabLayout().getTabAt(position)?.select()
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
                if(!GlobalConfig.isSellerApp()) {
                    when(tab.position) {
                        PENDING_TAB_INDEX -> {
                            when(previouslySelectedTab) {
                                PENDING_TAB_INDEX -> {
                                    ReviewInboxContainerTracking.eventOnClickReviewPendingTabFromReviewPendingTab(viewModel.getUserId())
                                }
                                HISTORY_TAB_INDEX -> {
                                    ReviewInboxContainerTracking.eventOnClickReviewPendingTabFromReviewHistoryTab(viewModel.getUserId())
                                }
                            }
                        }
                        HISTORY_TAB_INDEX -> {
                            ReviewInboxContainerTracking.eventOnClickReviewHistoryTabFromReviewPendingTab(viewModel.getUserId())
                        }
                        else -> {
                            ReviewInboxContainerTracking.eventOnClickReviewSellerTab(viewModel.getUserId())
                        }
                    }
                    previouslySelectedTab = tab.position
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
            setupBuyerAdapter()
            setupViewPager(tabTitles)
        })
    }

}