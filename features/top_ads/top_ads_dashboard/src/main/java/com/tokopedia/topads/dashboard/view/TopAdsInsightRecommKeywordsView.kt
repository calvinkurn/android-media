package com.tokopedia.topads.dashboard.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightRecommKeywordsAdapter
import kotlinx.android.synthetic.main.topads_insight_keywords_layout.view.*

class TopAdsInsightRecommKeywordsView(
    context: Context,
    private val type: Int
) : ConstraintLayout(context) {

    private val list = mutableListOf(1,2,4)
    private val mAdapter by lazy { TopAdsInsightRecommKeywordsAdapter() }

    init {
        inflate(context, layout, this)
        initView()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        with(recyclerViewKeyword) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initView() {
        when(type) {
            TopAdsInsightConstants.RECOMM_KEYWORD -> {
                txtTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_title),list.size)
                txtSubTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_subtitle),"1.00")
            }
            TopAdsInsightConstants.NEW_KEYWORD -> {
                txtTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_title),list.size)
                txtSubTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_subtitle),"1.00")
            }
            TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                txtTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_title),list.size)
                txtSubTitle.text = String.format(resources.getString(R.string.topads_insight_recomm_keyword_subtitle),"1.00")
            }
        }
    }

    companion object {
        private val layout = R.layout.topads_insight_keywords_layout
        fun createInstance(context: Context, type: Int) =
            TopAdsInsightRecommKeywordsView(context, type)
    }

}