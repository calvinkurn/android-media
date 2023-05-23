package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.content.Context
import android.text.Html
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.BID_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsInsightConstants.NEW_KEYWORD
import com.tokopedia.topads.dashboard.data.model.insightkey.RecommendedKeywordDetail
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class TopAdsInsightShopKeywordViewHolder(private val context: Context, private val view: View) :
    RecyclerView.ViewHolder(view) {

    val edtBid: TextFieldUnify = view.findViewById(R.id.edtBid)
    val checkBox: CheckboxUnify = view.findViewById(R.id.checkBox)
    private val txtTitle: Typography = view.findViewById(R.id.txtTitle)
    private val textGroupName: Typography = view.findViewById(R.id.textGroupName)
    private val txtNoOfSearches: Typography = view.findViewById(R.id.txtNoOfSearches)
    private val txtSubTitle2: Typography = view.findViewById(R.id.txtSubTitle2)
    private val txtSubTitle2Value: Typography = view.findViewById(R.id.txtSubTitle2Value)
    private val txtRecommendedBudget: Typography = view.findViewById(R.id.txtRecommendedBudget)
    private val txtFooter: Typography = view.findViewById(R.id.txtFooter)
    private val btnEditFee: ImageUnify = view.findViewById(R.id.btnEditFee)
    private val searchGroup: Group = view.findViewById(R.id.searchGroup)
    private val newKeywordGroup: Group = view.findViewById(R.id.newKeywordGroup)


    fun bindData(item: RecommendedKeywordDetail) {

        checkBox.isChecked = item.isChecked

        txtTitle.text = item.keywordTag
        textGroupName.text = item.groupName

        txtNoOfSearches.attributedString(
            String.format(view.resources.getString(R.string.no_of_searches), item.totalHits)
        )

        updateSubTitle2Value(item.priceBid)

        edtBid.textFieldInput.setText("${item.priceBid}")
        updateRecommBudget(item)

        txtFooter.attributedString(
            String.format(view.resources.getString(R.string.max_times_month), item.impressionCount)
        )

        itemView.setOnClickListener {
            closeEditTextFee(item)
        }
        btnEditFee.setOnClickListener {
            openEditTextFee()
        }
    }

    fun updateRecommBudget(item: RecommendedKeywordDetail) = with(txtRecommendedBudget) {
        val inputBudget = item.priceBid
        var isError = false
        val message = if (inputBudget % BUDGET_MULTIPLE_FACTOR != 0) {
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
            String.format(view.resources.getString(R.string.min_bid_error_number), item.minBid.toInt())
        } else if (inputBudget > item.maxBid) {
            isError = true
            String.format(view.resources.getString(R.string.max_bid_error_number), item.maxBid.toInt())
        } else {
            ""
        }

        text = message
        item.isError = isError
        if (isError) {
            setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_RN500))
            item.errorMessage = message
        } else {
            setTextColor(ContextCompat.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            item.errorMessage = null
        }
    }

    private fun closeEditTextFee(item: RecommendedKeywordDetail) {
        if (edtBid.visibility == View.VISIBLE) {
            txtSubTitle2Value.show()
            btnEditFee.show()
            edtBid.hide()
            if (!item.isError)
                txtRecommendedBudget.hide()
            updateSubTitle2Value(item.priceBid)
        }
    }

    fun openEditTextFee() = with(view) {
        txtRecommendedBudget.show()
        txtSubTitle2Value.invisible()
        btnEditFee.invisible()
        edtBid.show()
    }

    private fun updateSubTitle2Value(value: Int) {
        txtSubTitle2Value.attributedString(
            String.format(
                view.resources.getString(R.string.per_click_bold_value), value
            )
        )
    }

    fun initView(type: Int, item: RecommendedKeywordDetail) = with(view) {
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
        updateRecommBudget(item)
        if (item.isError) txtRecommendedBudget.show() else txtRecommendedBudget.hide()
    }
}

fun Typography.attributedString(source: String) {
    text = Html.fromHtml(source)
}