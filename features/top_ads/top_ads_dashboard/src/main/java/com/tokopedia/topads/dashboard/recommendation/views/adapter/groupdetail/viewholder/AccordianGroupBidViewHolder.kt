package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.ACTION_EDIT_PARAM
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_GROUP_BID_MAX_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_MULTIPLIER
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse.TopAdsBatchGetAdGroupBidInsightByGroupID.Group.AdGroupBidInsightData

class AccordianGroupBidViewHolder(
    private val itemView: View,
    private val onInsightAction: (hasErrors: Boolean) -> Unit
) :
    AbstractViewHolder<AccordianGroupBidUiModel>(itemView) {

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
        createDefaultInputModel(element)
        addTextChangeListener(element)
        setViews(element)
        bindValues(element)
        setCheckedChangeListener(element)
    }

    private fun createDefaultInputModel(element: AccordianGroupBidUiModel?) {
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
                        bidType = getAdGroupBidInsightData(element)?.currentBidSettings?.firstOrNull()?.bidType,
                        priceBid = getSearchTypeCurrentBid(element).toFloat(),
                    ),
                    GroupEditInput.Group.TopadsGroupBidSetting(
                        bidType = getAdGroupBidInsightData(element)?.currentBidSettings?.getOrNull(1)?.bidType,
                        priceBid = getBrowseTypeCurrentBid(element).toFloat(),
                    )
                ),
                suggestionBidSettings = arrayListOf(
                    GroupEditInput.Group.TopadsSuggestionBidSetting(
                        bidType = getAdGroupBidInsightData(element)?.suggestionBidSettings?.firstOrNull()?.bidType,
                        suggestionPriceBid = getSearchTypeSuggestionBid(element).toFloat(),
                    ),
                    GroupEditInput.Group.TopadsSuggestionBidSetting(
                        bidType = getAdGroupBidInsightData(element)?.suggestionBidSettings?.getOrNull(1)?.bidType,
                        suggestionPriceBid = getBrowseTypeSuggestionBid(element).toFloat(),
                    )
                )
            )
        )
        element?.input?.keywordOperation = null
    }

    private fun setViews(element: AccordianGroupBidUiModel?) {
        searchGroup.showWithCondition(getSearchTypeCurrentBid(element) < getSearchTypeSuggestionBid(element))
        recommendationGroup.showWithCondition(getBrowseTypeCurrentBid(element) < getBrowseTypeSuggestionBid(element))
    }

    private fun bindValues(element: AccordianGroupBidUiModel?) {
        searchCurrentCost.text = getSearchTypeCurrentBid(element).toString()
        searchPotential.text = getSearchTypeSuggestionBid(element).toString()
        searchCost.editText.setText(getSearchTypeSuggestionBid(element).toString())

        recommendationCurrentCost.text = getBrowseTypeCurrentBid(element).toString()
        recommendationPotential.text = getBrowseTypeSuggestionBid(element).toString()
        recommendationCost.editText.setText(getBrowseTypeSuggestionBid(element).toString())
    }

    private fun setCheckedChangeListener(element: AccordianGroupBidUiModel?) {
        searchCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            if (isChecked)
                updateSearchTypeBid(element, searchCost.editText.text.toString().toIntOrZero())
            else
                updateSearchTypeBid(element, getSearchTypeCurrentBid(element))
            onInsightAction.invoke(checkForErrors(element))
        }

        recommendationCheckBox.setOnCheckedChangeListener { btn, isChecked ->
            if (isChecked)
                updateBrowseTypeBid(element, recommendationCost.editText.text.toString().toIntOrZero())
            else
                updateBrowseTypeBid(element, getBrowseTypeCurrentBid(element))
            onInsightAction.invoke(checkForErrors(element))
        }
    }

    private fun addTextChangeListener(element: AccordianGroupBidUiModel?) {
        searchCost.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val bid = text.toString().toIntOrZero()
                updateSearchTypeBid(element, bid)
                val errorMsg = validateInput(bid, getSearchTypeCurrentBid(element))
                if (errorMsg.isEmpty()) {
                    searchCost.isInputError = false
                    if (text.toString().toIntOrZero() == getSearchTypeSuggestionBid(element)) {
                        searchCost.setMessage(getString(R.string.biaya_optimal))
                    } else {
                        attachClickableSpan(searchCost, getSearchTypeSuggestionBid(element))
                    }
                } else {
                    searchCost.isInputError = true
                    searchCost.setMessage(errorMsg)
                }
                onInsightAction.invoke(checkForErrors(element))
            }
        })

        recommendationCost.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val bid = text.toString().toIntOrZero()
                updateBrowseTypeBid(element, bid)
                val errorMsg = validateInput(bid, getBrowseTypeCurrentBid(element))
                if(errorMsg.isEmpty()){
                    recommendationCost.isInputError = false
                    if (text.toString().toIntOrZero() == getBrowseTypeSuggestionBid(element)) {
                        recommendationCost.setMessage(getString(R.string.biaya_optimal))
                    }
                    else {
                        attachClickableSpan(recommendationCost, getBrowseTypeSuggestionBid(element))
                    }
                } else {
                    recommendationCost.isInputError = true
                    recommendationCost.setMessage(errorMsg)
                }
                onInsightAction.invoke(checkForErrors(element))
            }
        })
    }

    private fun updateSearchTypeBid(element: AccordianGroupBidUiModel?, bid: Int) {
        element?.input?.groupInput?.group?.bidSettings?.firstOrNull()?.priceBid = bid.toFloat()
    }

    private fun updateBrowseTypeBid(element: AccordianGroupBidUiModel?, bid: Int) {
        element?.input?.groupInput?.group?.bidSettings?.getOrNull(1)?.priceBid = bid.toFloat()
    }

    private fun getSearchTypeCurrentBid(element: AccordianGroupBidUiModel?): Int {
        return getAdGroupBidInsightData(element)?.currentBidSettings?.firstOrNull()?.priceBid ?: 0
    }

    private fun getSearchTypeSuggestionBid(element: AccordianGroupBidUiModel?): Int {
        return getAdGroupBidInsightData(element)?.suggestionBidSettings?.firstOrNull()?.priceBid ?: 0
    }

    private fun getBrowseTypeCurrentBid(element: AccordianGroupBidUiModel?): Int {
        return getAdGroupBidInsightData(element)?.currentBidSettings?.getOrNull(1)?.priceBid ?: 0
    }

    private fun getBrowseTypeSuggestionBid(element: AccordianGroupBidUiModel?): Int {
        return getAdGroupBidInsightData(element)?.suggestionBidSettings?.getOrNull(1)?.priceBid ?: 0
    }

    private fun getAdGroupBidInsightData(element: AccordianGroupBidUiModel?): AdGroupBidInsightData? {
        return element?.topAdsBatchGetAdGroupBidInsightByGroupID?.groups?.firstOrNull()?.adGroupBidInsightData
    }

    private fun validateInput(inputBid: Int, currentBid: Int): String {
        return if (inputBid < currentBid)
            String.format(getString(R.string.topads_insight_min_bid_error_msg_format), currentBid)
        else if (inputBid > INSIGHT_GROUP_BID_MAX_BID)
            String.format(
                getString(R.string.topads_insight_max_bid_error_msg_format),
                INSIGHT_GROUP_BID_MAX_BID
            )
        else if (inputBid % INSIGHT_MULTIPLIER != 0)
            getString(R.string.error_bid_not_multiple_50)
        else String.EMPTY
    }

    private fun checkForErrors(element: AccordianGroupBidUiModel?): Boolean {
        if (!searchCheckBox.isChecked && !recommendationCheckBox.isChecked) {
            return true
        }
        if (searchCheckBox.isChecked) {
            if (validateInput(
                    searchCost.editText.text.toString().toIntOrZero(),
                    getSearchTypeCurrentBid(element)
                )
                    .isNotEmpty()
            )
                return true
        }
        if (recommendationCheckBox.isChecked) {
            if (validateInput(
                    recommendationCost.editText.text.toString().toIntOrZero(),
                    getBrowseTypeCurrentBid(element)
                )
                    .isNotEmpty()
            )
                return true
        }

        return false
    }

    private fun attachClickableSpan(
        view: com.tokopedia.unifycomponents.TextFieldUnify2,
        suggestionBid: Int
    ) {
        val msg = String.format(
            getString(R.string.topads_insight_recommended_bid_apply),
            suggestionBid
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                view.editText.setText(suggestionBid.toString())
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    view.context,
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
        view.setMessage(ss)
    }

    companion object {
        val LAYOUT = R.layout.top_ads_accordian_group_bid_layout
    }
}
