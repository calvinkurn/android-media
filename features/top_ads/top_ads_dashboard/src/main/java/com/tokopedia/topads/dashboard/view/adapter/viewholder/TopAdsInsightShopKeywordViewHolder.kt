package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.text.Html
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightShopKeywordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var item: RecommendedKeywordDetail

    fun bindData(type: Int) = with(view) {
        checkBox.isChecked = item.isChecked

        txtTitle.text = item.keywordTag
        textGroupName.text = item.groupName

        txtNoOfSearches.attributedString(
            String.format(resources.getString(R.string.no_of_searches), item.totalHits)
        )

        updateSubTitle2Value(item.priceBid)
        edtBid.textFieldInput.setText("${item.priceBid}")
        updateRecommBudget(item.priceBid)

        txtFooter.attributedString(
            String.format(resources.getString(R.string.max_times_month), item.impressionCount)
        )
    }

    fun updateRecommBudget(inputBudget: Int) = with(view.txtRecommendedBudget) {
        var isError = false
        text = if (inputBudget % 50 != 0) {
            isError = true
            resources.getString(R.string.error_bid_not_multiple_50)
        } else if (inputBudget <= item.recommendedBid && inputBudget >= item.minBid) {
            String.format(
                view.resources.getString(R.string.keyword_recommended_budget),
                item.recommendedBid.toInt()
            )
        } else if (inputBudget > item.recommendedBid && inputBudget <= item.maxBid) {
            view.resources.getString(R.string.biaya_optimal)
        } else if (inputBudget < item.minBid) {
            isError = true
            String.format(view.resources.getString(R.string.min_bid_error_new), item.minBid.toInt())
        } else if (inputBudget > item.maxBid) {
            isError = true
            String.format(view.resources.getString(R.string.max_bid_error_new), item.maxBid.toInt())
        } else {
            ""
        }
        item.isError = isError
        if (isError)
            setTextColor(resources.getColor(R.color.Unify_R600, null))
        else
            setTextColor(resources.getColor(R.color.Unify_N700_68, null))
    }

    fun closeEditTextFee() = with(view) {
        if (edtBid.visibility == View.VISIBLE) {
            txtSubTitle2Value.show()
            btnEditFee.show()
            edtBid.hide()
            if (!item.isError)
                txtRecommendedBudget.hide()
            updateSubTitle2Value(item.priceBid)
        }
    }

    fun openEditTextFee() = with(view){
        txtRecommendedBudget.show()
        txtSubTitle2Value.invisible()
        btnEditFee.invisible()
        edtBid.show()
    }

    private fun updateSubTitle2Value(value: Int) {
        view.txtSubTitle2Value.attributedString(
            String.format(
                view.resources.getString(R.string.per_click_bold_value), value
            )
        )
    }

    fun initView(type: Int, it: RecommendedKeywordDetail) = with(view) {
        item = it
        when (type) {
            BID_KEYWORD -> {
                searchGroup.hide()
            }
            NEW_KEYWORD -> {
                (txtSubTitle2.layoutParams as ConstraintLayout.LayoutParams)
                    .startToEnd = R.id.guideline
                (edtBid.layoutParams as ConstraintLayout.LayoutParams)
                    .endToEnd = R.id.centerVerticalGuideline

                txtSubTitle2.text = resources.getString(R.string.new_keyword_subtitle2)

                newKeywordGroup.hide()
                searchGroup.show()
            }
            NEGATIVE_KEYWORD -> {
                searchGroup.hide()
                btnEditFee.hide()
            }
        }
        updateRecommBudget(item.priceBid)
        if(item.isError) txtRecommendedBudget.show() else txtRecommendedBudget.hide()
    }
}

fun Typography.attributedString(source: String) {
    text = Html.fromHtml(source)
}