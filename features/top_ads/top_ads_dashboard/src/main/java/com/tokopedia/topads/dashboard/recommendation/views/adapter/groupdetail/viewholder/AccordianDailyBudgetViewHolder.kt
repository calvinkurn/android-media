package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_DAILY_BUDGET_MAX_BID
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel

class AccordianDailyBudgetViewHolder(
    private val view: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianDailyBudgetUiModel>(view) {

    private val currentBudget : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.currentBudget)
    private val recommendedBudget : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommendedBudget)
    private val dailyBudget : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.dailyBudgetInput)
    private var topadsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput? = null
    private var hasError : Boolean = false

    override fun bind(element: AccordianDailyBudgetUiModel?) {
        setDefaultInputModel(element)
        setTextChangeListener(element)
        bindValue(element)
    }

    private fun setDefaultInputModel(element: AccordianDailyBudgetUiModel?) {
        topadsManagePromoGroupProductInput = element?.input
        setDailyBudgetIntoInputModel(element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily?.toDouble() ?: 0.0)
        topadsManagePromoGroupProductInput?.keywordOperation = null
    }

    private fun bindValue(element: AccordianDailyBudgetUiModel?) {
        currentBudget.text = String.format(getString(R.string.topads_ads_price_format_1), element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.priceDaily.toString())
        recommendedBudget.text = String.format(getString(R.string.topads_ads_price_format_1), element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toString())
        dailyBudget.editText.setText(element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toString())
    }

    private fun setTextChangeListener(element: AccordianDailyBudgetUiModel?) {
        dailyBudget.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val dailyBudgetBid = text.toString().toDoubleOrZero()
                val errorMsg = validateDailyBudgetBidInput(dailyBudgetBid, element)
                if(errorMsg.isEmpty()){
                    hasError = false
                    dailyBudget.isInputError = false
                    if(dailyBudgetBid == element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily?.toDouble()){
                        dailyBudget.setMessage(getString(R.string.biaya_optimal))
                    } else {
                        dailyBudget.setMessage(getClickableString(element))
                    }
                } else {
                    hasError = true
                    dailyBudget.isInputError = true
                    dailyBudget.setMessage(errorMsg)
                }
                setDailyBudgetIntoInputModel(dailyBudgetBid)
                onInsightAction.invoke(hasError)
            }
        })
    }

    private fun validateDailyBudgetBidInput(budget: Double, element: AccordianDailyBudgetUiModel?): String {
        return if (budget < element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.priceDaily.toZeroIfNull())
            String.format(
                getString(R.string.topads_insight_min_budget_rp),
                element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.priceDaily.toZeroIfNull()
            )
        else if (budget > INSIGHT_DAILY_BUDGET_MAX_BID)
            String.format(
                getString(R.string.topads_insight_max_budget_rp),
                INSIGHT_DAILY_BUDGET_MAX_BID
            )
        else String.EMPTY
    }

    private fun setDailyBudgetIntoInputModel(dailyBudgetBid: Double) {
        topadsManagePromoGroupProductInput?.groupInput = GroupEditInput(
            action = ACTION_EDIT_PARAM,
            group = GroupEditInput.Group(
                dailyBudget = dailyBudgetBid
            )
        )
    }

    private fun getClickableString(element: AccordianDailyBudgetUiModel?): SpannableString {
        val msg = String.format(
            getString(R.string.topads_insight_recommended_bid_apply),
            element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toZeroIfNull()
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                dailyBudget.editText.setText(element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toZeroIfNull().toString())
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    dailyBudget.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                ds.isFakeBoldText = true
            }
        }
        ss.setSpan(
            cs,
            msg.length - 8,
            msg.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ss
    }

    companion object {
        val LAYOUT = R.layout.topads_insights_accordian_anggaran_harian_item_layout
    }
}
