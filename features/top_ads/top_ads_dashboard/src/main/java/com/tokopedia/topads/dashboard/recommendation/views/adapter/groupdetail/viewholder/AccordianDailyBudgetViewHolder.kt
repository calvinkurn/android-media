package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
        topadsManagePromoGroupProductInput = element?.input
        currentBudget.text = String.format("Rp%s", element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.priceDaily.toString())
        recommendedBudget.text = String.format("Rp%s", element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toString())

        dailyBudget.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                checkForErrors(text.toString().toDoubleOrZero(), element)
                topadsManagePromoGroupProductInput?.groupInput = GroupEditInput(
                    action = ACTION_EDIT_PARAM,
                    group = GroupEditInput.Group(
                        dailyBudget = text.toString().toDoubleOrZero()
                    )
                )
                onInsightAction.invoke(hasError)
            }
        })

        dailyBudget.editText.setText(element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily.toString())
    }

    private fun checkForErrors(budget: Double, element: AccordianDailyBudgetUiModel?){
        if(budget < element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.priceDaily.toZeroIfNull()){
            hasError = true
            dailyBudget.isInputError = true
            dailyBudget.editText.error = "Min. anggaran Rp$element?.sellerInsightData?.dailyBudgetData?.firstOrNull()?.suggestedPriceDaily"
        } else if(budget > INSIGHT_DAILY_BUDGET_MAX_BID){
            dailyBudget.isInputError = true
            hasError = true
            dailyBudget.editText.error = "Maks. anggaran Rp$INSIGHT_DAILY_BUDGET_MAX_BID"
        } else {
            dailyBudget.isInputError = false
            dailyBudget.editText.error = ""
            hasError = false
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_insights_accordian_anggaran_harian_item_layout
    }
}
