package com.tokopedia.topads.dashboard.view

import android.content.Context
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsInsightShopKeywordRecommAdapter
import kotlinx.android.synthetic.main.topads_insight_keywords_layout.view.*

class TopAdsInsightShopKeywordRecommView(
    context: Context,
    private val type: Int,
    private val recommendedKeywordData: RecommendedKeywordData,
    private val lstnr: (Int, Int) -> Unit
) : ConstraintLayout(context) {

    private val mAdapter by lazy {
        TopAdsInsightShopKeywordRecommAdapter.createInstance(
            recommendedKeywordData.recommendedKeywordDetails!!, type, ::onKeywordSelected
        )
    }

    init {
        inflate(context, layout, this)
        initView()
        initRecyclerView()
        initListeners()
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
        //todo checkbox dash icon
        checkBox.isChecked = true
    }

    private fun initListeners() {
        checkBox?.setOnClickListener {
            onCheckBoxSelected(checkBox.isChecked)
        }
    }

    private fun onCheckBoxSelected(checked: Boolean) {
        if (checked) {
            mAdapter.checkAllItems()
        } else {
            mAdapter.unCheckAllItems()
        }
    }

    private fun onKeywordSelected(selectedItems: Int) {
        checkBox.isChecked = selectedItems == mAdapter.itemCount
        lstnr.invoke(type, selectedItems)
    }

    companion object {
        private val layout = R.layout.topads_insight_keywords_layout
        fun createInstance(
            context: Context, type: Int,
            recommendedKeywordData: RecommendedKeywordData,
            lstnr: (Int, Int) -> Unit
        ) = TopAdsInsightShopKeywordRecommView(context, type, recommendedKeywordData, lstnr)
    }

}