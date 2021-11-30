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

    fun bindData(type: Int) = with(view) {
        checkBox.isChecked = item.isChecked

        txtTitle.text = item.keywordTag
        textGroupName.text = item.groupName

        txtSubTitle1Value.text = String.format(resources.getString(R.string.per_click_value), 0)
        updateSubTitle2Value(item.recommendedBid)

        edtBid.textFieldInput.setText(item.recommendedBid.toString())
        updateRecommendedBudgetText()

        txtFooter.attributedString(
            String.format(resources.getString(R.string.max_times_month), 500)
        )

        updateDataSpecific(type)
    }

    private fun updateDataSpecific(type: Int) = with(view) {
        when (type) {
            NEW_KEYWORD -> {
                txtNoOfSearches.attributedString(
                    String.format(resources.getString(R.string.no_of_searches), 10)
                )
            }
        }
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
        updateRecommendedBudgetText(text.toDouble() > item.recommendedBid)
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

    private fun updateRecommendedBudgetText(optimal: Boolean = false) {
        view.txtRecommendedBudget.text =
            if (optimal) view.resources.getString(R.string.biaya_optimal)
            else String.format(
                view.resources.getString(R.string.keyword_recommended_budget),
                item.recommendedBid
            )
    }

    private fun updateSubTitle2Value(value: Double) {
        view.txtSubTitle2Value.attributedString(
            String.format(
                view.resources.getString(R.string.per_click_bold_value),
                value
            )
        )
    }
}

fun Typography.attributedString(source: String) {
    text = Html.fromHtml(source)
}