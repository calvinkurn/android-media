package com.tokopedia.topads.dashboard.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeyword
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightRecommKeywordsAdapter
import kotlinx.android.synthetic.main.topads_insight_keywords_layout.view.*

class TopAdsInsightRecommKeywordsView(
    context: Context,
    private val type: Int,
    private val recommendedKeyword: RecommendedKeyword,
    private val lstnr: (Int, Int) -> Unit
) : ConstraintLayout(context) {

    private var selectedItemCount = 0
    private val mAdapter by lazy {
        TopAdsInsightRecommKeywordsAdapter.createInstance(
            recommendedKeyword.recommendedKeywordDetails, type, ::onKeywordSelected
        )
    }

    init {
        inflate(context, layout, this)
        initView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(recyclerViewKeyword) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initView() {
        when (type) {
            TopAdsInsightConstants.BID_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    1
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    "1.000"
                )
            }
            TopAdsInsightConstants.NEW_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    1
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    "1.000"
                )
            }
            TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    1
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    "1.000"
                )
            }
        }
    }

    private fun onKeywordSelected(isChecked: Boolean) {
        if (isChecked) selectedItemCount++ else selectedItemCount--
        lstnr.invoke(type, selectedItemCount)
    }

    companion object {
        private val layout = R.layout.topads_insight_keywords_layout
        fun createInstance(
            context: Context, type: Int,
            recommendedKeyword: RecommendedKeyword,
            lstnr: (Int, Int) -> Unit
        ) = TopAdsInsightRecommKeywordsView(context, type, recommendedKeyword, lstnr)
    }

}