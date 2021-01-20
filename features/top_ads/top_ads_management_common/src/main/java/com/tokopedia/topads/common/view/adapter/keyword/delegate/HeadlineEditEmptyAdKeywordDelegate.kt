package com.tokopedia.topads.common.view.adapter.keyword.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditEmptyAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditEmptyAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.KeywordUiModel

class HeadlineEditEmptyAdKeywordDelegate(var onItemClick: HeadlineEditEmptyAdKeywordViewHolder.OnHeadlineEmptyKeywordButtonClick? = null)
    : TypedAdapterDelegate<HeadlineEditEmptyAdKeywordModel, KeywordUiModel, HeadlineEditEmptyAdKeywordViewHolder>(R.layout.topads_headline_keyword_empty_state) {
    override fun onBindViewHolder(item: HeadlineEditEmptyAdKeywordModel, holder: HeadlineEditEmptyAdKeywordViewHolder) {
        holder.bindEmptyKeyWord(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): HeadlineEditEmptyAdKeywordViewHolder {
        return HeadlineEditEmptyAdKeywordViewHolder(basicView, onItemClick)
    }
}