package com.tokopedia.topads.dashboard.view.fragment.insight

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*
import kotlinx.android.synthetic.main.topads_dash_insight_keword_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 13/7/20.
 */

class TopadsInsightBaseKeywordFragment : BaseDaggerFragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_dash_insight_keword_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(context)
        Utils.setSearchListener(searchBar, context, view, ::searchAction)
    }

    private fun onSuccessGetInsightData(response: InsightKeyData) {
        swipe_refresh_layout.isRefreshing = false
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
        emptyViewKeyword?.image_empty?.setImageDrawable(context?.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_success))
        emptyViewKeyword?.visibility = View.VISIBLE
        keyword_list?.visibility = View.GONE
        searchBar?.visibility = View.GONE
    }

    private fun loadData() {
        swipe_refresh_layout?.isEnabled = true
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        topAdsDashboardPresenter.getInsight(resources, ::onSuccessGetInsightData)
    }

    private fun searchAction() {
        val list: MutableList<KeywordInsightDataMain> = mutableListOf()
        listOfKeys.clear()
        val search = searchBar.searchBarTextField.text.toString()
        keyData.forEach {
            if (it.value.name.contains(search)) {
                list.add(it.value)
                listOfKeys.add(it.key)
            }
        }
        (parentFragment as TopAdsRecommendationFragment).setCount(list.size, 2)
        if (list.isEmpty()) {
            keyword_list.visibility = View.GONE
            txtSearch.visibility = View.VISIBLE
            txtSearch.text = String.format(resources.getString(R.string.topads_insight_search_text), search)
        } else {
            keyword_list.visibility = View.VISIBLE
            txtSearch.visibility = View.GONE
        }
        adapter.items.clear()
        adapter.items.addAll(list)
        adapter.notifyDataSetChanged()
    }
}