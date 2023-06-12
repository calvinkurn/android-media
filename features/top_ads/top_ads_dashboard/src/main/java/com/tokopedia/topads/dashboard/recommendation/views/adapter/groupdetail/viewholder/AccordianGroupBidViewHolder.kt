package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel

class AccordianGroupBidViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianGroupBidUiModel>(itemView) {

    private var hasErrors : Boolean = false
    private val searchCheckBox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.searchCheckbox)
    private val recommendationCheckBox: com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify = itemView.findViewById(R.id.recommendation_checkbox)
    private val searchCurrentCost : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.search_current_cost_value)
    private val recommendationCurrentCost : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommendation_current_cost_value)
    private val searchPotential : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.search_potential_value)
    private val recommendationPotential : com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.recommendation_potential_value)
    private val searchCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.search_cost)
    private val recommendationCost : com.tokopedia.unifycomponents.TextFieldUnify2 = itemView.findViewById(R.id.recommendation_cost)
    private val searchGroup: Group = itemView.findViewById(R.id.searchGroup)
    private val recommendationGroup: Group = itemView.findViewById(R.id.recommendationGroup)

    override fun bind(element: AccordianGroupBidUiModel?) {
        createDefaultInputMddel(element)
        searchGroup.visibility =
            if ((element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.firstOrNull()?.priceBid
                    ?: 0) < (element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.firstOrNull()?.priceBid
                    ?: 0)
            ) View.VISIBLE else View.GONE

        recommendationGroup.visibility =
            if ((element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.getOrNull(1)?.priceBid
                    ?: 0) < (element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.getOrNull(1)?.priceBid
                    ?: 0)
            ) View.VISIBLE else View.GONE

        searchCurrentCost.text = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.firstOrNull()?.priceBid.toString()
        searchPotential.text = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.firstOrNull()?.priceBid.toString()
        searchCost.editText.setText(element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.firstOrNull()?.priceBid.toString())

        recommendationCurrentCost.text = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.getOrNull(1)?.priceBid.toString()
        recommendationPotential.text = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.getOrNull(1)?.priceBid.toString()
        recommendationCost.editText.setText(element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.getOrNull(1)?.priceBid.toString())

        searchCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked){
                updateSearchInput(element, searchCost.editText.text.toString().toIntOrZero())
            } else {
                updateSearchInput(element, element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.firstOrNull()?.priceBid.toString().toIntOrZero())
            }
            onInsightAction.invoke(hasErrors)
        }

        recommendationCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            if(isChecked){
                updateRecommendationInput(element, recommendationCost.editText.text.toString().toIntOrZero())
            } else {
                updateRecommendationInput(element, element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.getOrNull(1)?.priceBid.toString().toIntOrZero())
            }
            onInsightAction.invoke(hasErrors)
        }

        searchCost.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val bid = text.toString().toIntOrZero()
                if(bid < 350){
                    searchCost.isInputError = true
                    searchCost.editText.error = "Min. biaya Rp350."
                } else if(bid > 5000){
                    searchCost.isInputError = true
                    searchCost.editText.error = "Maks. biaya Rp5.000."
                } else if(bid % 50 != 0){
                    searchCost.isInputError = true
                    searchCost.editText.error = "Harus kelipatan Rp50."
                }
            }
        })

        recommendationCost.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val bid = text.toString().toIntOrZero()
                if(bid < 350){
                    recommendationCost.isInputError = true
                    recommendationCost.editText.error = "Min. biaya Rp350."
                } else if(bid > 5000){
                    recommendationCost.isInputError = true
                    recommendationCost.editText.error = "Maks. biaya Rp5.000."
                } else if(bid % 50 != 0){
                    recommendationCost.isInputError = true
                    recommendationCost.editText.error = "Harus kelipatan Rp50."
                }
            }
        })
    }

    private fun createDefaultInputMddel(element: AccordianGroupBidUiModel?) {
        element?.input?.groupInput = GroupEditInput(
            action = ACTION_EDIT_PARAM,
            group = GroupEditInput.Group(
                name = null,
                type = null,
                adOperations = null,
                dailyBudget = null,
                status = null,
                strategies = null,
                bidSettings = arrayListOf(
                    GroupEditInput.Group.TopadsGroupBidSetting(
                        bidType = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.firstOrNull()?.bidType,
                        priceBid = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.firstOrNull()?.priceBid?.toFloat(),
                    ),
                    GroupEditInput.Group.TopadsGroupBidSetting(
                        bidType = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.getOrNull(1)?.bidType,
                        priceBid = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.currentBidSettings?.getOrNull(1)?.priceBid?.toFloat(),
                    )
                ),
                suggestionBidSettings = arrayListOf(
                    GroupEditInput.Group.TopadsSuggestionBidSetting(
                        bidType = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.firstOrNull()?.bidType,
                        suggestionPriceBid = (element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.firstOrNull()?.priceBid ?: 0 ).toFloat(),
                    ),
                    GroupEditInput.Group.TopadsSuggestionBidSetting(
                        bidType = element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.getOrNull(1)?.bidType,
                        suggestionPriceBid = (element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData?.suggestionBidSettings?.getOrNull(1)?.priceBid ?: 0).toFloat(),
                    )
                )
            )
        )
    }

    private fun updateSearchInput(element: AccordianGroupBidUiModel?, bid: Int){
        element?.input?.groupInput?.group?.bidSettings?.firstOrNull()?.priceBid = bid.toFloat()
    }


    private fun updateRecommendationInput(element: AccordianGroupBidUiModel?, bid: Int){
        element?.input?.groupInput?.group?.bidSettings?.getOrNull(1)?.priceBid = bid.toFloat()
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_group_bid_layout
    }
}
