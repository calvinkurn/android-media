package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.text.Html
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_insight_keyword_recomm_item.view.*

class TopAdsInsightShopKeywordViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var item: RecommendedKeywordDetail

    fun bindData(type: Int) = with(view) {
        checkBox.isChecked = item.isChecked

        txtTitle.text = item.keywordTag
        textGroupName.text = item.groupName

        txtNoOfSearches.attributedString(
            String.format(resources.getString(R.string.no_of_searches), item.totalHits)
        )

        updateSubTitle2Value(item.recommendedBid)
        edtBid.textFieldInput.setText("${item.recommendedBid}")
        txtRecommendedBudget.text = String.format(
            view.resources.getString(R.string.keyword_recommended_budget),
            item.recommendedBid
        )

        txtFooter.attributedString(
            String.format(resources.getString(R.string.max_times_month), item.impressionCount)
        )
    }

    fun addListeners(lstr: (CheckboxUnify) -> Unit) = with(view) {
        edtBid.textFieldInput.afterTextChanged(::editTextValueChanged)
        btnEditFee.setOnClickListener {
            openEditTextFee()
        }
        setOnClickListener {
            closeEditTextFee()
        }
        lstr.invoke(checkBox)
    }

    private fun editTextValueChanged(text: String) {
        val inputBudget = text.toDouble()
        view.txtRecommendedBudget.text =
            if (inputBudget <= item.recommendedBid && inputBudget > item.minBid) {
                String.format(view.resources.getString(R.string.keyword_recommended_budget), item.recommendedBid)
            } else if(inputBudget > item.recommendedBid && inputBudget < item.maxBid) {
                view.resources.getString(R.string.biaya_optimal)
            } else if(inputBudget < item.minBid) {
                String.format(view.resources.getString(R.string.min_bid_error_new), item.minBid)
            } else if(inputBudget > item.maxBid) {
                String.format(view.resources.getString(R.string.max_bid_error_new), item.maxBid)
            } else ""
        item.priceBid = inputBudget
    }

    private fun View.closeEditTextFee() {
        if (edtBid.visibility == View.VISIBLE) {
            txtSubTitle2Value.show()
            btnEditFee.show()
            edtBid.hide()
            val edtBidText = edtBid.textFieldInput.text.toString()
            if (edtBidText.toDouble() > item.recommendedBid)
                txtRecommendedBudget.hide()
            updateSubTitle2Value(edtBidText.toDouble())
        }
    }

    private fun View.openEditTextFee() {
        txtRecommendedBudget.show()
        txtSubTitle2Value.invisible()
        btnEditFee.invisible()
        edtBid.show()
    }

    private fun updateSubTitle2Value(value: Double) {
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
    }
}

fun Typography.attributedString(source: String) {
    text = Html.fromHtml(source)
}