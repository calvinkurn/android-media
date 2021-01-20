package com.tokopedia.topads.common.view.adapter.keyword

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.topads.common.view.adapter.keyword.delegate.HeadlineEditAdKeywordDelegate
import com.tokopedia.topads.common.view.adapter.keyword.delegate.HeadlineEditEmptyAdKeywordDelegate
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditEmptyAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.KeywordUiModel

class KeywordListAdapter(headlineAdEditItemClick: HeadlineEditAdKeywordViewHolder.OnHeadlineAdEditItemClick? = null,
                         headlineAdEmptyKeywordButtonClick: HeadlineEditEmptyAdKeywordViewHolder.OnHeadlineEmptyKeywordButtonClick? = null)
    : BaseAdapter<KeywordUiModel>() {

    init {
        delegatesManager
                .addDelegate(HeadlineEditAdKeywordDelegate(headlineAdEditItemClick))
                .addDelegate(HeadlineEditEmptyAdKeywordDelegate(headlineAdEmptyKeywordButtonClick))
    }

    fun setKeywordItems(uiModels: List<KeywordUiModel>) {
        setItems(uiModels)
        notifyDataSetChanged()
    }
}