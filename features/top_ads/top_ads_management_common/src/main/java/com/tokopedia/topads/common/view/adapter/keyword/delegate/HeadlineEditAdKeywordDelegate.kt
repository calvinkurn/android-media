package com.tokopedia.topads.common.view.adapter.keyword.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.KeywordUiModel

class HeadlineEditAdKeywordDelegate(var onItemClick: HeadlineEditAdKeywordViewHolder.OnHeadlineAdEditItemClick? = null)
    : TypedAdapterDelegate<HeadlineEditAdKeywordModel, KeywordUiModel, HeadlineEditAdKeywordViewHolder>(R.layout.item_topads_common_edit_keyword_layout) {
    override fun onBindViewHolder(item: HeadlineEditAdKeywordModel, holder: HeadlineEditAdKeywordViewHolder) {
        holder.bndHeadlineEditAdKeywordModel(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): HeadlineEditAdKeywordViewHolder {
        return HeadlineEditAdKeywordViewHolder(basicView, onItemClick)
    }

}