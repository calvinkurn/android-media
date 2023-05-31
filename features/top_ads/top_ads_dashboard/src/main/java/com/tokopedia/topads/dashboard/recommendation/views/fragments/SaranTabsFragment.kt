package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
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

class SaranTabsFragment(private val tabType: Int) : BaseDaggerFragment() {

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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mapper: InsightDataMapper

    private val viewModel: TopAdsListAllInsightViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[TopAdsListAllInsightViewModel::class.java]
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
        setPageState(tabType)
        getFirstPageData()
        observeViewModel()

        chipsAdapter = ChipsAdapter(onClick())

        chipsRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chipsRecyclerView?.adapter = chipsAdapter

        chipsAdapter?.submitList(viewModel.getChipsData())
        val itemDecoration =
            RecommendationInsightItemDecoration(
                groupCardRecyclerView?.context,
                layoutManager.orientation
            )
        groupCardRecyclerView?.layoutManager = layoutManager
        groupCardRecyclerView?.addItemDecoration(itemDecoration)
        groupCardRecyclerView?.adapter = insightListAdapter
        groupCardRecyclerView?.addOnScrollListener(recyclerviewScrollListener)
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

        if (mapper.insightDataMap[position]?.adGroups?.isEmpty() == true) {
            viewModel.getFirstPageData(
                adGroupType = RecommendationConstants.PRODUCT_KEY,
                insightType = position,
                mapper = mapper
            )
        }

        insightListAdapter.submitList(mapper.insightDataMap[position]?.adGroups?.toMutableList())
        recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun observeViewModel() {
        viewModel.productInsights.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessProductInsight(it)
                }
                is Fail -> {
                }
                is Loading -> {
                    if (mapper.insightDataMap[it.type]?.nextCursor?.isNotEmpty() == true) {
                        mapper.insightDataMap[it.type]?.adGroups?.add(loadingModel)
                        insightListAdapter.submitList(mapper.insightDataMap[it.type]?.adGroups?.toMutableList())
                    } else {
                        mapper.insightDataMap[it.type]?.adGroups?.add(loadingModel)
                        insightListAdapter.submitList(mapper.insightDataMap[it.type]?.adGroups?.toMutableList())
                    }
                }
            }
        }

        viewModel.headlineInsights.observe(viewLifecycleOwner) {
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
        mapper.insightDataMap[type]?.adGroups?.remove(loadingModel)
        mapper.insightDataMap[type]?.adGroups?.addAll(it.data.adGroups)
        mapper.insightDataMap[type]?.nextCursor = it.data.nextCursor
        if (mapper.insightDataMap[type]?.adGroups?.size == 0) {
            mapper.insightDataMap[type]?.adGroups?.clear()
            mapper.insightDataMap[type]?.adGroups?.add(viewModel.getEmptyStateData(type))
        }
        insightListAdapter.submitList(mapper.insightDataMap[type]?.adGroups?.toMutableList())
    }

    private fun onRecyclerViewListener(layoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Toast.makeText(context, "scdjn", Toast.LENGTH_LONG).show()
                val selected = chipsAdapter?.getSelectedChips()
                val nextCursor = mapper.insightDataMap[selected]?.nextCursor
                if (nextCursor?.isNotEmpty() == true) {
                    viewModel.getNextPageData(
                        adGroupType = RecommendationConstants.PRODUCT_KEY,
                        insightType = selected ?: 0,
                        startCursor = nextCursor,
                        mapper = mapper
                    )
                }
            }
        }
    }

    private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit =
        { adGroupList, item ->
            val bundle = Bundle()
            bundle.putString("adType", item.adGroupType)
            bundle.putString("adGroupName", item.adGroupName)
            bundle.putString("groupId", item.adGroupID)
            bundle.putInt("count", item.count)
            bundle.putInt("insightType", item.insightType)
            bundle.putParcelableArrayList("insightTypeList", adGroupList)
            Intent(context, GroupDetailActivity::class.java).apply {
                this.putExtra("groupDetailBundle", bundle)
                startActivity(this)
            }
        }

    private fun getFirstPageData() {
        viewModel.getFirstPageData(
            adGroupType = if (tabType == TYPE_PRODUCT_VALUE) RecommendationConstants.PRODUCT_KEY else RecommendationConstants.HEADLINE_KEY,
            insightType = if (tabType == TYPE_PRODUCT_VALUE) 0 else 5,
            mapper = mapper
        )
    }
}
