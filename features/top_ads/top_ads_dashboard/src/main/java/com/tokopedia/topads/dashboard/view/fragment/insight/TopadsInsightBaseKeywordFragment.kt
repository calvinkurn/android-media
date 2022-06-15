package com.tokopedia.topads.dashboard.view.fragment.insight

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsKeywordInsightsActivity
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsKeywordInsightAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Pika on 13/7/20.
 */

class TopadsInsightBaseKeywordFragment : BaseDaggerFragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var searchBar: SearchBarUnify? = null
    private var keywordList: RecyclerView? = null
    private var txtSearch: Typography? = null

    private lateinit var adapter: TopAdsKeywordInsightAdapter
    private var listOfKeys: MutableList<String> = mutableListOf()
    private lateinit var keyData: HashMap<String, KeywordInsightDataMain>


    companion object {
        fun createInstance(): TopadsInsightBaseKeywordFragment {
            return TopadsInsightBaseKeywordFragment()
        }
    }

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    override fun getScreenName(): String {
        return TopadsInsightBaseKeywordFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopAdsKeywordInsightAdapter(::onItemClicked)
    }

    private fun onItemClicked(pos: Int) {
        val intent = Intent(context, TopAdsKeywordInsightsActivity::class.java)
        intent.putExtra(KEY_INSIGHT, listOfKeys[pos])
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.topads_dash_insight_keword_layout, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        searchBar = view.findViewById(R.id.searchBar)
        keywordList = view.findViewById(R.id.keyword_list)
        txtSearch = view.findViewById(R.id.txtSearch)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        keywordList?.adapter = adapter
        keywordList?.layoutManager = LinearLayoutManager(context)
        searchBar?.let { Utils.setSearchListener(it, context, view, ::searchAction) }
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        swipeRefreshLayout?.isRefreshing = false
        val data: HashMap<String, KeywordInsightDataMain> = response.data
        keyData = data
        listOfKeys.clear()
        data.forEach {
            adapter.items.add(it.value)
            listOfKeys.add(it.key)
        }
        if (data.isEmpty()) {
            setEmpty()
        }
        (parentFragment as TopAdsRecommendationFragment).setCount(data.size, 2)
        adapter.notifyDataSetChanged()
    }

    private fun setEmpty() {
        view?.findViewById<ImageUnify>(R.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        view?.findViewById<ConstraintLayout>(R.id.emptyViewKeyword)?.visibility = View.VISIBLE
        keywordList?.visibility = View.GONE
        searchBar?.visibility = View.GONE
    }

    private fun loadData() {
        swipeRefreshLayout?.isEnabled = true
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)
    }

    private fun searchAction() {
        val list: MutableList<KeywordInsightDataMain> = mutableListOf()
        listOfKeys.clear()
        val search = searchBar?.searchBarTextField?.text.toString()
        keyData.forEach {
            if (it.value.name.contains(search)) {
                list.add(it.value)
                listOfKeys.add(it.key)
            }
        }
        (parentFragment as TopAdsRecommendationFragment).setCount(list.size, 2)
        if (list.isEmpty()) {
            keywordList?.visibility = View.GONE
            txtSearch?.visibility = View.VISIBLE
            txtSearch?.text =
                String.format(resources.getString(R.string.topads_insight_search_text), search)
        } else {
            keywordList?.visibility = View.VISIBLE
            txtSearch?.visibility = View.GONE
        }
        adapter.items.clear()
        adapter.items.addAll(list)
        adapter.notifyDataSetChanged()
    }
}