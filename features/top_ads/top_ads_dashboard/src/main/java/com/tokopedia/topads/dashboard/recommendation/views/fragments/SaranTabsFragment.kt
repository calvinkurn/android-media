package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_COUNT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_ID_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_NAME_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.AD_GROUP_TYPE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.GROUP_DETAIL_BUNDLE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_TYPE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_TYPE_LIST_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_ALL
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.common.decoration.ChipsInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.common.decoration.RecommendationInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.LoadingUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState.*
import com.tokopedia.topads.dashboard.recommendation.viewmodel.TopAdsListAllInsightViewModel
import com.tokopedia.topads.dashboard.recommendation.views.activities.GroupDetailActivity
import com.tokopedia.topads.dashboard.recommendation.views.adapter.ChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.InsightListAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import javax.inject.Inject

class SaranTabsFragment : BaseDaggerFragment() {

    private var chipsRecyclerView: RecyclerView? = null
    private var groupCardRecyclerView: RecyclerView? = null
    private val insightListAdapter by lazy { InsightListAdapter(onInsightItemClick) }
    private var chipsAdapter: ChipsAdapter? = null
    private val loadingModel = LoadingUiModel()

    private val layoutManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
    private val recyclerviewScrollListener by lazy { onRecyclerViewListener(layoutManager) }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    @JvmField
    @Inject
    var mapper: InsightDataMapper? = null

    @JvmField
    @Inject
    var utils: Utils? = null

    private val viewModel: TopAdsListAllInsightViewModel? by lazy {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            )[TopAdsListAllInsightViewModel::class.java]
        }
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_saran_topads_tab_fragment_layout,
            container,
            false
        )

        chipsRecyclerView = view.findViewById(R.id.chipsRecyclerView)
        groupCardRecyclerView = view.findViewById(R.id.groupCardRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPageState(getTabType())
        getFirstPageData()
        observeViewModel()
        setChipsView()
        setGroupsView()
    }

    private fun setGroupsView() {
        val itemDecoration = RecommendationInsightItemDecoration(
            groupCardRecyclerView?.context,
            layoutManager.orientation
        )
        groupCardRecyclerView?.layoutManager = layoutManager
        groupCardRecyclerView?.addItemDecoration(itemDecoration)
        groupCardRecyclerView?.adapter = insightListAdapter
        groupCardRecyclerView?.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun setChipsView() {
        chipsAdapter = ChipsAdapter(onClick())
        chipsRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chipsRecyclerView?.addItemDecoration(ChipsInsightItemDecoration())
        chipsRecyclerView?.adapter = chipsAdapter
        viewModel?.let { chipsAdapter?.submitList(it.getChipsData()) }
    }

    private fun getTabType(): Int {
        return this.arguments?.getInt(AD_GROUP_TYPE_KEY) ?: TYPE_PRODUCT_VALUE
    }

    private fun setPageState(tabType: Int) {
        chipsRecyclerView?.showWithCondition(tabType == TYPE_PRODUCT_VALUE)
    }

    private fun onClick(): (List<SaranTopAdsChipsUiModel>, Int) -> Unit = { list, position ->
        chipsRecyclerView?.layoutManager?.startSmoothScroll(
            object : LinearSmoothScroller(context) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }.apply { targetPosition = position }
        )
        val mList = mutableListOf<SaranTopAdsChipsUiModel>()
        list.forEachIndexed { index, saranTopAdsChipsModel ->
            mList.add(SaranTopAdsChipsUiModel(saranTopAdsChipsModel.name, position == index))
        }
        chipsAdapter?.submitList(mList)

        if (mapper?.insightDataMap?.get(position)?.adGroups?.isEmpty() == true) {
            viewModel?.getFirstPageData(
                adGroupType = PRODUCT_KEY,
                insightType = position,
                mapper = mapper
            )
        }

        insightListAdapter.submitList(mapper?.insightDataMap?.get(position)?.adGroups?.toMutableList())
        recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun observeViewModel() {
        viewModel?.productInsights?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessProductInsight(it)
                }
                is Fail -> {
                }
                is Loading -> {
                    mapper?.insightDataMap?.get(it.type)?.adGroups?.add(loadingModel)
                    insightListAdapter.submitList(mapper?.insightDataMap?.get(it.type)?.adGroups?.toMutableList())
                }
            }
        }

        viewModel?.headlineInsights?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessProductInsight(it)
                }
                is Fail -> {
                }
                is Loading -> {
                }
            }
        }
    }

    private fun onSuccessProductInsight(it: Success<InsightUiModel>) {
        recyclerviewScrollListener.updateStateAfterGetData()
        val type = it.data.insightType
        val insightUiModel = mapper?.insightDataMap?.get(type)
        insightUiModel?.adGroups?.remove(loadingModel)
        insightUiModel?.adGroups?.addAll(it.data.adGroups)
        insightUiModel?.nextCursor = it.data.nextCursor
        if (insightUiModel?.adGroups?.size.isZero()) {
            insightUiModel?.adGroups?.clear()
            viewModel?.getEmptyStateData(type)?.let { it1 -> insightUiModel?.adGroups?.add(it1) }
        }
        insightListAdapter.submitList(insightUiModel?.adGroups?.toMutableList())
    }

    private fun onRecyclerViewListener(layoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val selected = chipsAdapter?.getSelectedChips()
                val nextCursor = mapper?.insightDataMap?.get(selected)?.nextCursor
                if (nextCursor?.isNotEmpty() == true) {
                    mapper?.let {
                        viewModel?.getNextPageData(
                            adGroupType = PRODUCT_KEY,
                            insightType = selected ?: Int.ZERO,
                            startCursor = nextCursor,
                            mapper = it
                        )
                    }
                }
            }
        }
    }

    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit =
        { adGroupList, item ->
            val bundle = Bundle()
            bundle.putString(AD_GROUP_TYPE_KEY, item.adGroupType)
            bundle.putString(AD_GROUP_NAME_KEY, item.adGroupName)
            bundle.putString(AD_GROUP_ID_KEY, item.adGroupID)
            bundle.putInt(AD_GROUP_COUNT_KEY, item.count)
            bundle.putInt(INSIGHT_TYPE_KEY, item.insightType)
            bundle.putParcelableArrayList(INSIGHT_TYPE_LIST_KEY, adGroupList)
            Intent(context, GroupDetailActivity::class.java).apply {
                this.putExtra(GROUP_DETAIL_BUNDLE_KEY, bundle)
                startActivity(this)
            }
        }

    private fun getFirstPageData() {
        val tabType = getTabType()
        viewModel?.getFirstPageData(
            adGroupType = utils?.convertAdTypeToString(tabType) ?: PRODUCT_KEY,
            insightType = INSIGHT_TYPE_ALL,
            mapper = mapper
        )
    }

    companion object {
        fun createInstance(adType: Int): SaranTabsFragment {
            val fragment = SaranTabsFragment()
            val bundle = Bundle()
            bundle.putInt(AD_GROUP_TYPE_KEY, adType)
            fragment.arguments = bundle
            return fragment
        }
    }
}
