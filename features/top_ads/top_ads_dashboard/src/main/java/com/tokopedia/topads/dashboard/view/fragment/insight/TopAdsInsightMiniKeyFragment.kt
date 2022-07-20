package com.tokopedia.topads.dashboard.view.fragment.insight

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsKeywordInsightsActivity
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsMiniKeywordInsightAdapter

/**
 * Created by Pika on 20/7/20.
 */

private const val INITIAL_SIZE_LIMIT = 3
class TopAdsInsightMiniKeyFragment : BaseDaggerFragment() {

    private lateinit var adapter: TopAdsMiniKeywordInsightAdapter
    private var listOfKeys: MutableList<String> = mutableListOf()


    override fun getScreenName(): String {
        return TopAdsInsightMiniKeyFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TopAdsMiniKeywordInsightAdapter(this::onItemClicked)
    }

    companion object {
        fun createInstance(data: Bundle): TopAdsInsightMiniKeyFragment {
            val fragment = TopAdsInsightMiniKeyFragment()
            fragment.arguments = data
            return fragment
        }
    }

    private fun onItemClicked(pos: Int) {
        val intent = Intent(context, TopAdsKeywordInsightsActivity::class.java)
        intent.putExtra(KEY_INSIGHT, listOfKeys[pos])
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.topads_dash_insight_mini_keword_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        view.findViewById<RecyclerView>(R.id.keyword_list)?.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setView() {
        val data: HashMap<String, KeywordInsightDataMain> =
            arguments?.getSerializable(DATA_INSIGHT) as HashMap<String, KeywordInsightDataMain>
        listOfKeys.clear()
        var sizeLimit = INITIAL_SIZE_LIMIT
        data.forEach {
            if (sizeLimit == 0)
                return@forEach
            sizeLimit -= 1
            adapter.items.add(it.value)
            listOfKeys.add(it.key)

        }
        adapter.notifyDataSetChanged()
    }
}