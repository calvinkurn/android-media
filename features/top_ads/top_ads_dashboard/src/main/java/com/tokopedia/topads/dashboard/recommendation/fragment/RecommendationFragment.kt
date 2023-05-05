package com.tokopedia.topads.dashboard.recommendation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.adapter.EmptyStatePagerAdapter
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.RecommendationViewModel
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class RecommendationFragment : BaseDaggerFragment() {
    private var layoutAppBar: AppBarLayout? = null
    private var saranAdsTypeTab: TabsUnify? = null
    private var saranTopAdsViewPager: ViewPager? = null
    private var insightWidgetTitle: Typography? = null
    private var insightWidgetIcon: ImageUnify? = null
    private var topLevelWidgetShimmer: View? = null
    private var bottomShimmerRecommendation: View? = null
    private var recommendationMainContainer: CardUnify2? = null
    private var recommendationEmptyState: EmptyStateUnify? = null

    private var emptyStateRecyclerView: RecyclerView? = null
    private var pageControlEmptyState: PageControl? = null

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: TopAdsHeadlineBaseFragment.AppBarActionHeadline? = null

    private var isTopAds = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: RecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[RecommendationViewModel::class.java]
    }

    private val pagerAdapter by lazy {
        EmptyStatePagerAdapter()
    }

    private val layoutManager by lazy {
        return@lazy object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    private lateinit var detailPagerAdapter: TopAdsDashboardBasePagerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopAdsHeadlineBaseFragment.AppBarActionHeadline)
            collapseStateCallBack = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_fragment_saran_top_ads_layout, container, false
        )
        layoutAppBar = view.findViewById(R.id.appBarLayout)
        saranAdsTypeTab = view.findViewById(R.id.saranAdsTypeTab)
        saranTopAdsViewPager = view.findViewById(R.id.saranTopAdsViewPager)
        insightWidgetTitle = view.findViewById(R.id.insightWidgetTitle)
        insightWidgetIcon = view.findViewById(R.id.insightWidgetIcon)
        topLevelWidgetShimmer = view.findViewById(R.id.topLevelWidgetShimmer)
        bottomShimmerRecommendation = view.findViewById(R.id.bottomShimmerRecommendation)
        recommendationMainContainer = view.findViewById(R.id.recommendationMainContainer)
        recommendationEmptyState = view.findViewById(R.id.recommendationEmptyState)
        emptyStateRecyclerView = view.findViewById(R.id.emptyStateView)
        pageControlEmptyState = view.findViewById(R.id.pageControlEmptyState)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutAppBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED;
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED;
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE);
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE;
                }
            }
        })
        viewModel.loadRecommendationPage()
        setUpObserver()
        settingClickListener()
    }

    private fun settingClickListener() {
        recommendationEmptyState?.emptyStateCTAID?.setOnClickListener {
            viewModel.loadRecommendationPage()
            recommendationEmptyState?.hide()
            recommendationMainContainer?.show()
        }
    }

    override fun getScreenName(): String? = null

    private fun setUpObserver() {
        viewModel.shopInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hideShimmerBottom()
                    renderTabAndViewPager(it.data)
                    renderTopLevelWidgetForNoTopAds(it.data.isHeadline && it.data.isProduct)
//                    renderTopLevelWidgetForNoTopAds(false)

                }
                is Fail -> {
                    recommendationEmptyState?.show()
                    recommendationMainContainer?.hide()
                }
            }
        }

        viewModel.adGroupWithInsight.observe(viewLifecycleOwner) {
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    topLevelWidgetShimmer?.hide()
                    renderTopLevelWidget(it.data)
                }
                is TopAdsListAllInsightState.Loading -> {

                }
                is TopAdsListAllInsightState.Fail -> {

                }
            }
        }
    }

    private fun hideShimmerBottom() {
        bottomShimmerRecommendation?.hide()
        saranAdsTypeTab?.show()
        saranTopAdsViewPager?.show()
        emptyStateRecyclerView?.show()
        pageControlEmptyState?.show()
    }

    private fun renderTopLevelWidgetForNoTopAds(isTopAds: Boolean) {
        if (!isTopAds) {
            insightWidgetTitle?.text = "Kamu belum punya iklan yang lagi aktif, nih~"
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
            saranAdsTypeTab?.hide()
            saranTopAdsViewPager?.hide()
            renderEmptyStates()
            return
        }
    }

    private fun renderTopLevelWidget(
        data: TopAdsTotalAdGroupsWithInsightResponse,
    ) {
        if (context == null) return
        val count =
            data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
        if (count == 0) {
            insightWidgetTitle?.text = "Yay, semua iklanmu sudah maksimal!"
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.perfomace_widget_optimized_icon
                )
            )
            saranAdsTypeTab?.hide()
            saranTopAdsViewPager?.hide()
            renderEmptyStates()
        } else if (count < 10) {
            insightWidgetTitle?.text = "Tingkatkan performa 5 grup iklanmu, yuk!"
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.perfomace_widget_optimized_icon
                )
            )

        } else {
            insightWidgetTitle?.text = "Tingkatkan performa 10+ grup iklanmu, yuk!"
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
        }
    }

    private fun renderEmptyStates() {
        val indicatorCount = viewModel.emptyStateData.size
        pagerAdapter.emptyStatePages = viewModel.emptyStateData
        emptyStateRecyclerView?.layoutManager = layoutManager
        emptyStateRecyclerView?.adapter = pagerAdapter
        pageControlEmptyState?.setIndicator(indicatorCount)
        emptyStateRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (currentPosition != RecyclerView.NO_POSITION && indicatorCount > 1) {
                    pageControlEmptyState?.setCurrentIndicator(currentPosition)
                }
            }
        })
        try {
            PagerSnapHelper().attachToRecyclerView(emptyStateRecyclerView)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun renderTabAndViewPager(data: TopAdsGetShopInfoUiModel) {
        saranTopAdsViewPager?.adapter = getViewPagerAdapter(data)
        saranTopAdsViewPager?.offscreenPageLimit = 1
        saranTopAdsViewPager?.currentItem = 0
        saranTopAdsViewPager?.let { saranAdsTypeTab?.setupWithViewPager(it) }
    }

    private fun getViewPagerAdapter(data: TopAdsGetShopInfoUiModel): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        saranAdsTypeTab?.getUnifyTabLayout()?.removeAllTabs()
        saranAdsTypeTab?.customTabMode = TabLayout.MODE_FIXED
        setTabs(list, data)
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    private fun setTabs(list: MutableList<FragmentTabItem>, data: TopAdsGetShopInfoUiModel) {
        if (data.isProduct && data.isHeadline) {
            saranAdsTypeTab?.addNewTab("Iklan Produk")
            saranAdsTypeTab?.addNewTab("Iklan Toko")
            saranAdsTypeTab?.show()
        } else {
            saranAdsTypeTab?.hide()
        }
        if (data.isProduct) {
            list.add(FragmentTabItem("Iklan Produk", SaranTabsFragment(TYPE_PRODUCT)))
        }
        if (data.isHeadline) {
            list.add(FragmentTabItem("Iklan Toko", SaranTabsFragment(TYPE_SHOP)))
        }

    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
    }

    companion object {
        const val TYPE_PRODUCT = 0
        const val TYPE_SHOP = 1
    }
}
