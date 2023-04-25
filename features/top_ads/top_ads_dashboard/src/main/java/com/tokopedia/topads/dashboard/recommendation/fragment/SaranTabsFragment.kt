package com.tokopedia.topads.dashboard.recommendation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.adapter.ChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.adapter.InsightListAdapter
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.LoadingUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState.*
import com.tokopedia.topads.dashboard.recommendation.fragment.RecommendationFragment.Companion.TYPE_PRODUCT
import com.tokopedia.topads.dashboard.recommendation.viewmodel.TopAdsListAllInsightViewModel
import javax.inject.Inject

class SaranTabsFragment(private val tabType: Int) : BaseDaggerFragment() {

    private var chipsRecyclerView: RecyclerView? = null
    private var groupCardRecyclerView: RecyclerView? = null
    private val insightListAdapter by lazy { InsightListAdapter() }
    private var chipsAdapter: ChipsAdapter? = null
    private val loadingModel = LoadingUiModel()

    val productInsightData: MutableMap<Int, InsightUiModel> = mutableMapOf(
        0 to InsightUiModel(),
        1 to InsightUiModel(),
        2 to InsightUiModel(),
        3 to InsightUiModel(),
        4 to InsightUiModel(),
        5 to InsightUiModel(),
    )


    private val layoutManager by lazy {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
    private val recyclerviewScrollListener by lazy { onRecyclerViewListener(layoutManager) }


    val list = mutableListOf(
        SaranTopAdsChipsUiModel("Semua", true),
        SaranTopAdsChipsUiModel("Kata Kunci"),
        SaranTopAdsChipsUiModel("Biaya Kata Kunci"),
        SaranTopAdsChipsUiModel("Biaya Iklan"),
        SaranTopAdsChipsUiModel("Anggaran Harian"),
        SaranTopAdsChipsUiModel("Kata Kunci Negatif"),
    )

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: TopAdsListAllInsightViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[TopAdsListAllInsightViewModel::class.java]
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.topads_dash_saran_topads_tab_fragment_layout, container, false
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

        chipsAdapter?.submitList(list)
        groupCardRecyclerView?.layoutManager = layoutManager
        groupCardRecyclerView?.adapter = insightListAdapter
        groupCardRecyclerView?.addOnScrollListener(recyclerviewScrollListener)

    }

    private fun setPageState(tabType: Int) {
        chipsRecyclerView?.showWithCondition(tabType == TYPE_PRODUCT)
    }

    private fun onClick(): (List<SaranTopAdsChipsUiModel>, Int) -> Unit = { list, position ->
        val mList = mutableListOf<SaranTopAdsChipsUiModel>()
        list.forEachIndexed { index, saranTopAdsChipsModel ->
            mList.add(SaranTopAdsChipsUiModel(saranTopAdsChipsModel.name, position == index))

        }
        chipsAdapter?.submitList(mList)

        if (productInsightData[position]?.adGroups?.isEmpty() == true) {
            viewModel.getFirstPageData(
                adGroupType = "product",
                insightType = position,
            )
        }

        insightListAdapter.submitList(productInsightData[position]?.adGroups?.toMutableList())
        recyclerviewScrollListener.updateStateAfterGetData()
    }

    private fun observeViewModel() {


        viewModel.productInsights.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSucessProductInsight(it)
                }
                is Fail -> {

                }
                is Loading -> {
                    if (productInsightData[it.type]?.nextCursor?.isNotEmpty() == true) {
                        productInsightData[it.type]?.adGroups?.add(loadingModel)
                        insightListAdapter.submitList(productInsightData[it.type]?.adGroups?.toMutableList())
                    } else {
                        productInsightData[it.type]?.adGroups?.add(loadingModel)
                        insightListAdapter.submitList(productInsightData[it.type]?.adGroups?.toMutableList())
                    }
                }

            }
        }

        viewModel.headlineInsights.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.adGroups.forEach {
                    }
                }
            }
        }
    }

    private fun onSucessProductInsight(it: Success<InsightUiModel>) {
        recyclerviewScrollListener.updateStateAfterGetData()
        val type = it.data.insightType
        productInsightData[type]?.adGroups?.remove(loadingModel)
        productInsightData[type]?.adGroups?.addAll(it.data.adGroups)
        productInsightData[type]?.nextCursor = it.data.nextCursor
        val list = productInsightData[type]?.adGroups?.toMutableList()
        insightListAdapter.submitList(productInsightData[type]?.adGroups?.toMutableList())
    }

    private fun onRecyclerViewListener(layoutManager: LinearLayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Toast.makeText(context, "scdjn", Toast.LENGTH_LONG).show()
                val selected = chipsAdapter?.getSelectedChips()
                val nextCursor = productInsightData[selected]?.nextCursor
                if (nextCursor?.isNotEmpty() == true) {
                    viewModel.getNextPageData(
                        adGroupType = "product",
                        insightType = selected ?: 0,
                        startCursor = nextCursor
                    )
                }
            }
        }
    }

    private fun getFirstPageData() {
        viewModel.getFirstPageData(
            adGroupType = if (tabType == TYPE_PRODUCT) "product" else "headline",
            insightType = 0,
        )
    }

}
