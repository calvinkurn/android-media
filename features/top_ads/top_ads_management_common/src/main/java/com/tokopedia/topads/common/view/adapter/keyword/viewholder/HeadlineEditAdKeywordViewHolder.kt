package com.tokopedia.topads.common.view.adapter.keyword.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditAdKeywordModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.text.currency.NumberTextWatcher

class HeadlineEditAdKeywordViewHolder(itemView: View, var onItemClick: OnHeadlineAdEditItemClick? = null) : BaseViewHolder(itemView) {
    private val keywordName: Typography = itemView.findViewById(R.id.keywordName)
    private val searchType: Typography = itemView.findViewById(R.id.searchType)
    private val searchTypeSelection: ImageUnify = itemView.findViewById(R.id.searchTypeSelection)
    private val deleteKeyword: ImageUnify = itemView.findViewById(R.id.deleteKeyword)
    private val advertisingCost: TextFieldUnify = itemView.findViewById(R.id.advertisingCost)
    private val advertisingCostText: Typography = itemView.findViewById(R.id.klik)

    fun bndHeadlineEditAdKeywordModel(keywordModel: HeadlineEditAdKeywordModel) {
        if (keywordModel.isNegativeKeyword) {
            advertisingCost.hide()
            advertisingCostText.hide()
        } else {
            advertisingCost.show()
            advertisingCostText.show()
        }
        keywordName.text = keywordModel.keywordName
        searchType.text = keywordModel.searchType
        advertisingCost.textFieldInput.setText(keywordModel.advertisingCost)
        searchTypeSelection.setImageDrawable(itemView.context?.getResDrawable(R.drawable.ic_dropdown_arrow))
        searchType.setOnClickListener {
            onItemClick?.onSearchTypeClick(keywordModel)
        }
        searchTypeSelection.setOnClickListener {
            onItemClick?.onSearchTypeClick(keywordModel)
        }
        deleteKeyword.setOnClickListener {
            onItemClick?.onDeleteItemClick(keywordModel)
        }
        if (keywordModel.minimumBid != "0" && keywordModel.maximumBid != "0") {
            setBidInfo(keywordModel)
        }
    }

    private fun setBidInfo(keywordModel: HeadlineEditAdKeywordModel) {
        advertisingCost.textFieldInput.addTextChangedListener(object : NumberTextWatcher(advertisingCost.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                when {
                    result < keywordModel.minimumBid.toDouble() -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_common_min_bid), keywordModel.minimumBid))
                        onItemClick?.onEditPriceBid(false, keywordModel)
                    }
                    result > keywordModel.maximumBid.toDouble() -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_common_max_bid), keywordModel.maximumBid))
                        onItemClick?.onEditPriceBid(false, keywordModel)
                    }
                    else -> {
                        keywordModel.priceBid = result.toString()
                        advertisingCost.setError(false)
                        advertisingCost.setMessage("")
                        onItemClick?.onEditPriceBid(true, keywordModel)
                    }
                }
            }
        })
    }

    interface OnHeadlineAdEditItemClick {
        fun onDeleteItemClick(keywordModel: HeadlineEditAdKeywordModel)
        fun onSearchTypeClick(keywordModel: HeadlineEditAdKeywordModel)
        fun onEditPriceBid(isEnabled: Boolean, keywordModel: HeadlineEditAdKeywordModel)
    }
}