package com.tokopedia.review.feature.inbox.container.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.ReviewInboxInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.OnBackPressedListener
import com.tokopedia.review.feature.inbox.container.analytics.ReviewInboxContainerTracking
import com.tokopedia.review.feature.inbox.container.data.ReviewInboxTabs
import com.tokopedia.review.feature.inbox.container.di.DaggerReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.di.ReviewInboxContainerComponent
import com.tokopedia.review.feature.inbox.container.presentation.adapter.ReviewInboxContainerAdapter
import com.tokopedia.review.feature.inbox.container.presentation.listener.ReviewInboxListener
import com.tokopedia.review.feature.inbox.container.presentation.viewmodel.ReviewInboxContainerViewModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.FragmentReviewInboxContainerBinding
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ReviewInboxContainerFragment : BaseDaggerFragment(), HasComponent<ReviewInboxContainerComponent>, OnBackPressedListener, ReviewPerformanceMonitoringContract, ReviewInboxListener {

    companion object {
        const val PENDING_TAB_INDEX = 0
        const val HISTORY_TAB_INDEX = 1
        const val SELLER_TAB_INDEX = 2
        const val HIDE_TAB_COUNTER = -1

        fun createNewInstance(tab: String, source: String) : ReviewInboxContainerFragment{
            return ReviewInboxContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewApplinkConst.PARAM_TAB, tab)
                    putString(ReviewInboxConstants.PARAM_SOURCE, source)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewInboxContainerViewModel

    private var previouslySelectedTab = 0
    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null
    private var tab = ""
    private var source = ""

    private var counter = 0

    private var binding by autoClearedNullable<FragmentReviewInboxContainerBinding>()

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromArguments()
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ReviewInboxContainerComponent? {
        return activity?.run {
            DaggerReviewInboxContainerComponent
                    .builder()
                    .reviewInboxComponent(ReviewInboxInstance.getComponent(application))
                    .build()
        }
    }

    override fun stopPreparePerfomancePageMonitoring() {
        reviewPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        // No Op
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewPerformanceMonitoringListener? {
        return if(context is ReviewPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReviewInboxContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_Background))
        initToolbar()
        observeReviewTabs()
        getCounterData()
        setupTabLayout()
    }

    override fun onDestroy() {
        removeObservers(viewModel.reviewTabs)
        super.onDestroy()
    }


    override fun onBackPressed() {
        if(::viewModel.isInitialized) {
            ReviewInboxContainerTracking.eventOnClickBackButton(viewModel.getUserId())
        }
    }

    override fun reloadCounter() {
        getCounterData()
    }

    private fun getCounterData() {
        viewModel.getTabCounter()
    }

    private fun setupBuyerAdapter() {
        binding?.reviewInboxViewPager?.adapter = viewModel.reviewTabs.value?.let { ReviewInboxContainerAdapter(it, this, this, getSourceBundle()) }
    }

    private fun setupViewPager(tabTitles: List<String>) {
        binding?.reviewInboxTabs?.tabLayout?.removeAllTabs()
        tabTitles.forEach {
            binding?.reviewInboxTabs?.addNewTab(it)
        }
        binding?.reviewInboxViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding?.reviewInboxTabs?.getUnifyTabLayout()?.getTabAt(position)?.select()
            }
        })
    }

    private fun selectTab() {
        when(tab) {
            ReviewApplinkConst.PENDING_TAB -> {
                binding?.reviewInboxViewPager?.currentItem = PENDING_TAB_INDEX
            }
            ReviewApplinkConst.HISTORY_TAB -> {
                binding?.reviewInboxViewPager?.currentItem = HISTORY_TAB_INDEX
            }
            ReviewApplinkConst.SELLER_TAB -> {
                binding?.reviewInboxViewPager?.currentItem = SELLER_TAB_INDEX
            }
            else ->  {
                binding?.reviewInboxViewPager?.currentItem = PENDING_TAB_INDEX
            }
        }
    }

    private fun setupTabLayout() {
        binding?.reviewInboxTabs?.apply {
            customTabGravity = TabLayout.GRAVITY_FILL
            customTabMode = TabLayout.MODE_SCROLLABLE
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {
                    // No Op
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    // No Op
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding?.reviewInboxViewPager?.setCurrentItem(tab.position, true)
                    if (!GlobalConfig.isSellerApp()) {
                        when (tab.position) {
                            PENDING_TAB_INDEX -> {
                                when (previouslySelectedTab) {
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
    }

    private fun observeReviewTabs() {
        viewModel.reviewTabs.observe(viewLifecycleOwner, { tabs ->
            updateInboxUnifiedBuyerView(getTabTitles(tabs))
            updateCounter(counter)
        })
    }

    private fun updateCounter(counter: Int) {
        binding?.reviewInboxTabs?.tabLayout?.getTabAt(PENDING_TAB_INDEX)?.setCounter(counter)
    }

    private fun getDataFromArguments() {
        tab = arguments?.getString(ReviewApplinkConst.PARAM_TAB, "") ?: ""
        source = arguments?.getString(ReviewInboxConstants.PARAM_SOURCE, ReviewInboxConstants.DEFAULT_SOURCE) ?: ReviewInboxConstants.DEFAULT_SOURCE
        if(source.isEmpty()) {
            source = ReviewInboxConstants.DEFAULT_SOURCE
        }
    }

    private fun getSourceBundle(): Bundle {
        return Bundle().apply { putString(ReviewInboxConstants.PARAM_SOURCE, source) }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(binding?.headerReviewInboxContainer)
                binding?.headerReviewInboxContainer?.apply {
                    title = getString(R.string.title_activity_reputation_review)
                    show()
                }
            }
        }
    }

    private fun updateInboxUnifiedBuyerView(tabTitles: List<String>) {
        setupBuyerAdapter()
        setupViewPager(tabTitles)
        selectTab()
    }

    private fun getTabTitles(tabs: MutableList<ReviewInboxTabs>): MutableList<String> {
        val tabTitles = mutableListOf<String>()
        tabs.forEach { tab ->
            when (tab) {
                is ReviewInboxTabs.ReviewInboxPending -> {
                    tabTitles.add(getString(R.string.review_pending_tab_title))
                    counter = (if (tab.counter.isNotBlank() && tab.counter.toIntOrZero() != 0) tab.counter.toIntOrZero() else HIDE_TAB_COUNTER)
                }
                is ReviewInboxTabs.ReviewInboxHistory -> {
                    tabTitles.add(getString(R.string.review_history_tab_title))
                }
                is ReviewInboxTabs.ReviewInboxSeller -> {
                    tabTitles.add(getString(R.string.review_seller_tab_title))
                }
            }
        }
        return tabTitles
    }
}
