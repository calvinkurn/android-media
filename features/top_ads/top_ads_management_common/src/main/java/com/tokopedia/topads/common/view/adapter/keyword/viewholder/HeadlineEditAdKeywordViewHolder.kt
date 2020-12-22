package com.tokopedia.topads.common.view.adapter.keyword.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditAdKeywordModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.Typography

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
            advertisingCost.hide()
            advertisingCostText.hide()
            keywordName.text = keywordModel.keywordName
            searchType.text = keywordModel.searchType
            advertisingCost.textFieldInput.setText(keywordModel.advertisingCost)
            searchTypeSelection.setOnClickListener {
                onItemClick?.onSearchTypeClick(keywordModel)
            }
            deleteKeyword.setOnClickListener {
                onItemClick?.onDeleteItemClick(keywordModel)
            }
        }
    }

    interface OnHeadlineAdEditItemClick {
        fun onDeleteItemClick(keywordModel: HeadlineEditAdKeywordModel)
        fun onSearchTypeClick(keywordModel: HeadlineEditAdKeywordModel)
    }
}