package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.text.Html
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightShopKeywordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(item: RecommendedKeywordDetail) = with(view) {
        checkBox.isChecked = item.isChecked
        txtTitle.text = item.keywordTag
        textGroupName.text = item.groupName
        txtSubTitle1Value.text = String.format(resources.getString(R.string.per_click_value), 110)
        txtSubTitle2Value.attributedString(
            String.format(resources.getString(R.string.per_click_bold_value), 100)
        )
        txtFooter.attributedString(
            String.format(resources.getString(R.string.max_times_month), 500)
        )
    }

    fun initView(type: Int) = with(view) {
        when (type) {
            TopAdsInsightConstants.BID_KEYWORD -> {
                searchGroup.hide()
            }
            TopAdsInsightConstants.NEW_KEYWORD -> {
                (txtSubTitle2.layoutParams as ConstraintLayout.LayoutParams)
                    .startToEnd = R.id.guideline

                newKeywordGroup.hide()
                searchGroup.show()

                txtNoOfSearches.attributedString(
                    String.format(resources.getString(R.string.no_of_searches), 10)
                )
            }
            TopAdsInsightConstants.NEGATIVE_KEYWORD -> {
                searchGroup.hide()
                btnEditFee.hide()
            }
        }
    }
}

fun Typography.attributedString(source: String) {
    text = Html.fromHtml(source)
}