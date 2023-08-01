package com.tokopedia.topads.dashboard.recommendation.views.fragments

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
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_COUNT_PLACE_HOLDER
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_PRODUCT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_SHOP
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightCountUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.RecommendationViewModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.EmptyStatePagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.setCounter
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
    private var recommendationGlobalError: GlobalError? = null

    private var emptyStateRecyclerView: RecyclerView? = null
    private var pageControlEmptyState: PageControl? = null
    private var potentialProductCard: View? = null

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: TopAdsHeadlineBaseFragment.AppBarActionHeadline? = null

    private var detailPagerAdapter: TopAdsDashboardBasePagerAdapter? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: RecommendationViewModel? by lazy {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            )[RecommendationViewModel::class.java]
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopAdsHeadlineBaseFragment.AppBarActionHeadline) {
            collapseStateCallBack = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_fragment_saran_top_ads_layout,
            container,
            false
        )
        initializeViews(view)
        return view
    }

    private fun initializeViews(view: View) {
        layoutAppBar = view.findViewById(R.id.appBarLayout)
        saranAdsTypeTab = view.findViewById(R.id.saranAdsTypeTab)
        saranTopAdsViewPager = view.findViewById(R.id.saranTopAdsViewPager)
        insightWidgetTitle = view.findViewById(R.id.insightWidgetTitle)
        insightWidgetIcon = view.findViewById(R.id.insightWidgetIcon)
        topLevelWidgetShimmer = view.findViewById(R.id.topLevelWidgetShimmer)
        bottomShimmerRecommendation = view.findViewById(R.id.bottomShimmerRecommendation)
        recommendationMainContainer = view.findViewById(R.id.recommendationMainContainer)
        recommendationGlobalError = view.findViewById(R.id.recommendationGlobalError)
        emptyStateRecyclerView = view.findViewById(R.id.emptyStateView)
        pageControlEmptyState = view.findViewById(R.id.pageControlEmptyState)
        potentialProductCard = view.findViewById(R.id.topads_insight_center_product_widget_potential)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarListener()
        viewModel?.loadRecommendationPage()
        setUpObserver()
        settingClickListener()
    }

    private fun setAppBarListener() {
        layoutAppBar?.addOnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == Int.ZERO -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE
                }
            }
        }
    }

    private fun settingClickListener() {
        recommendationGlobalError?.setActionClickListener{
            viewModel?.loadRecommendationPage()
            recommendationGlobalError?.hide()
            recommendationMainContainer?.show()
        }

        potentialProductCard?.setOnClickListener {
            RouteManager.route(
                activity,
                "tokopedia://webview?url=https://ta.tokopedia.com/v2/manage/recommendation/eligible-product"
            )
        }
    }

    override fun getScreenName(): String? = null

    private fun setUpObserver() {
        viewModel?.shopInfo?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    hideShimmerBottom()
                    renderTabAndViewPager(it.data)
                    renderTopLevelWidgetForNoTopAds(it.data.isHeadline && it.data.isProduct)
                }
                is Fail -> {
                    showGlobalError()
                    recommendationMainContainer?.hide()
                }
            }
        }

        viewModel?.adGroupWithInsight?.observe(viewLifecycleOwner) {
            when (it) {
                is TopAdsListAllInsightState.Success -> {
                    topLevelWidgetShimmer?.hide()
                    renderTopLevelWidget(it.data)
                    setTabsCount(it.data)
                }
                is TopAdsListAllInsightState.Loading -> {
                    topLevelWidgetShimmer?.show()
                }
                is TopAdsListAllInsightState.Fail -> {
                }
            }
        }
    }

    private fun showGlobalError() {
        recommendationGlobalError?.show()
        recommendationGlobalError?.errorTitle?.text = context?.getString(R.string.topads_insight_global_error_title)
        recommendationGlobalError?.errorDescription?.text = context?.getString(R.string.topads_insight_global_error_description)
    }

    private fun setTabsCount(totalAdGroupsWithInsight: GroupInsightCountUiModel) {
        saranAdsTypeTab?.getUnifyTabLayout()?.getTabAt(Int.ZERO)?.setCounter(totalAdGroupsWithInsight.productInsightCount)
        saranAdsTypeTab?.getUnifyTabLayout()?.getTabAt(Int.ONE)?.setCounter(totalAdGroupsWithInsight.headlineInsightCount)
    }

    private fun hideShimmerBottom() {
        bottomShimmerRecommendation?.hide()
    }

    private fun renderTopLevelWidgetForNoTopAds(isTopAds: Boolean) {
        if (!isTopAds && context != null) {
            insightWidgetTitle?.text =
                context?.getString(R.string.topads_insight_no_active_ads_title)
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
            hideTabAndShowEmptyState()
            renderEmptyStates()
            return
        }
    }

    private fun hideTabAndShowEmptyState(isHideTab: Boolean = true) {
        if (isHideTab) {
            saranAdsTypeTab?.hide()
            saranTopAdsViewPager?.hide()
            emptyStateRecyclerView?.show()
            pageControlEmptyState?.show()
        } else {
            saranAdsTypeTab?.show()
            saranTopAdsViewPager?.show()
            emptyStateRecyclerView?.hide()
            pageControlEmptyState?.hide()
        }
    }

    private fun renderTopLevelWidget(
        data: GroupInsightCountUiModel
    ) {
        if (context == null) return
        val count = data.productInsightCount + data.headlineInsightCount
        if (count == Int.ZERO) {
            insightWidgetTitle?.text = context?.getString(R.string.topads_insight_max_out_title)
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.perfomace_widget_optimized_icon
                )
            )
            hideTabAndShowEmptyState()
            renderEmptyStates()
        } else if (count <= INSIGHT_COUNT_PLACE_HOLDER) {
            insightWidgetTitle?.text = String.format(
                context?.getString(R.string.topads_insight_title_improve_ads_performance) ?: "",
                "$count"
            )
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
        } else {
            insightWidgetTitle?.text = String.format(
                context?.getString(R.string.topads_insight_title_improve_ads_performance) ?: "",
                "$INSIGHT_COUNT_PLACE_HOLDER+"
            )
            insightWidgetIcon?.loadImage(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.performance_widget_default_icon
                )
            )
        }
    }

    private fun renderEmptyStates() {
        val indicatorCount = viewModel?.emptyStateData?.size ?: Int.ZERO
        pagerAdapter.emptyStatePages = viewModel?.emptyStateData ?: listOf()
        emptyStateRecyclerView?.layoutManager = layoutManager
        emptyStateRecyclerView?.adapter = pagerAdapter
        pageControlEmptyState?.setIndicator(indicatorCount)
        emptyStateRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (currentPosition != RecyclerView.NO_POSITION && indicatorCount > Int.ONE) {
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
        saranTopAdsViewPager?.offscreenPageLimit = Int.ONE
        saranTopAdsViewPager?.currentItem = Int.ZERO
        saranTopAdsViewPager?.let { saranAdsTypeTab?.setupWithViewPager(it) }
    }

    private fun getViewPagerAdapter(data: TopAdsGetShopInfoUiModel): PagerAdapter? {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        saranAdsTypeTab?.getUnifyTabLayout()?.removeAllTabs()
        saranAdsTypeTab?.customTabMode = TabLayout.MODE_FIXED
        setTabs(list, data)
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        detailPagerAdapter?.setList(list)
        return detailPagerAdapter
    }

    private fun setTabs(list: MutableList<FragmentTabItem>, data: TopAdsGetShopInfoUiModel) {
        if (data.isProduct && data.isHeadline) {
            saranAdsTypeTab?.addNewTab(TAB_NAME_PRODUCT)
            saranAdsTypeTab?.addNewTab(TAB_NAME_SHOP)
            hideTabAndShowEmptyState(false)
        } else {
            hideTabAndShowEmptyState()
        }
        if (data.isProduct) {
            list.add(FragmentTabItem(TAB_NAME_PRODUCT, SaranTabsFragment.createInstance(TYPE_PRODUCT_VALUE)))
        }
        if (data.isHeadline) {
            list.add(FragmentTabItem(TAB_NAME_SHOP, SaranTabsFragment.createInstance(TYPE_SHOP_VALUE)))
        }
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
    }

    companion object {
        fun createInstance(): RecommendationFragment {
            return RecommendationFragment()
        }
    }
}
