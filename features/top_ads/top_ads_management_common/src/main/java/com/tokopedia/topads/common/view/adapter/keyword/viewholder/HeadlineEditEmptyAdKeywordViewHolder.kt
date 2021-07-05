package com.tokopedia.topads.common.view.adapter.keyword.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditEmptyAdKeywordModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class HeadlineEditEmptyAdKeywordViewHolder(itemView: View, var onItemClick: OnHeadlineEmptyKeywordButtonClick? = null) : BaseViewHolder(itemView) {

    private val title: Typography = itemView.findViewById(R.id.title)
    private val subTitle: Typography = itemView.findViewById(R.id.subTitle)
    private val ctaBtn: UnifyButton = itemView.findViewById(R.id.ctaBtn)

    fun bindEmptyKeyWord(emptyAdKeywordModel: HeadlineEditEmptyAdKeywordModel) {
        title.text = getString(emptyAdKeywordModel.titleText)
        subTitle.text = getString(emptyAdKeywordModel.subTitleText)
        ctaBtn.text = getString(emptyAdKeywordModel.ctaBtnText)
        ctaBtn.setOnClickListener {
            onItemClick?.onCtaBtnClick()
        }
    }

    interface OnHeadlineEmptyKeywordButtonClick {
        fun onCtaBtnClick()
    }
}