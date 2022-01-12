package com.tokopedia.topads.dashboard.view

import android.content.Context
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordData
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.topads.dashboard.view.adapter.insight.TopAdsShopKeywordRecommendationAdapter
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_insight_keywords_layout.view.*

class TopAdsInsightShopKeywordRecommendationView(
    context: Context,
    val type: Int,
    private val recommendedKeywordData: RecommendedKeywordData,
    private val lstnr: (Int, Int) -> Unit
) : ConstraintLayout(context) {

    private var recyclerViewKeyword: RecyclerView? = null
    private val mAdapter by lazy {
        TopAdsShopKeywordRecommendationAdapter.createInstance(
            context,
            recommendedKeywordData.recommendedKeywordDetails!!,
            type, ::onKeywordSelected, ::keywordError
        )
    }

    init {
        inflate(context, layout, this)
        recyclerViewKeyword = findViewById(R.id.recyclerViewKeyword)
        initView()
        initRecyclerView()
        initListeners()
    }

    private fun initRecyclerView() {
        recyclerViewKeyword?.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun keywordError(item: RecommendedKeywordDetail) {
        if (item.isError && item.errorMessage != null) {
            Toaster.toasterCustomBottomHeight =
                resources.getDimensionPixelSize(com.tokopedia.topads.dashboard.R.dimen.dp_90)
            Toaster.build(
                rootView,
                resources.getString(R.string.topads_insight_keyword_error),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun onCheckBoxSelected(checked: Boolean) {
        if (checked) {
            checkBox.setIndeterminate(false)
            mAdapter.selectedItemsCount = recommendedKeywordData.recommendedKeywordCount
            mAdapter.checkAllItems()
        } else {
            mAdapter.selectedItemsCount = 0
            mAdapter.unCheckAllItems()
        }
        lstnr.invoke(type, mAdapter.selectedItemsCount)
    }

    private fun onKeywordSelected(selectedItems: Int) {
        when (selectedItems) {
            0 -> checkBox.isChecked = false
            mAdapter.itemCount -> {
                checkBox.isChecked = true
                checkBox.setIndeterminate(false)
            }
            else -> {
                checkBox.isChecked = true
                checkBox.setIndeterminate(true)
            }
        }
        lstnr.invoke(type, selectedItems)
    }

    fun selectedItemsCount() = mAdapter.selectedItemsCount
    fun getKeywords() = mAdapter.getSelectedKeywords()

    private fun initView() {
        when (type) {
            TopAdsInsightConstants.BID_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    recommendedKeywordData.recommendedKeywordCount
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    recommendedKeywordData.totalImpressionCount
                )
            }
            TopAdsInsightConstants.NEW_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    recommendedKeywordData.recommendedKeywordCount
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    recommendedKeywordData.totalImpressionCount
                )
            }
            TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                txtTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_title),
                    recommendedKeywordData.recommendedKeywordCount
                )
                txtSubTitle.text = String.format(
                    resources.getString(R.string.topads_insight_recomm_keyword_subtitle),
                    recommendedKeywordData.totalImpressionCount
                )
            }
        }
        checkBox.isChecked = true
    }

    private fun initListeners() {
        checkBox?.setOnClickListener {
            onCheckBoxSelected(checkBox.isChecked)
        }
    }

    companion object {
        private val layout = R.layout.topads_insight_keywords_layout
        fun createInstance(
            context: Context, type: Int,
            recommendedKeywordData: RecommendedKeywordData,
            lstnr: (Int, Int) -> Unit
        ) = TopAdsInsightShopKeywordRecommendationView(context, type, recommendedKeywordData, lstnr)
    }

}