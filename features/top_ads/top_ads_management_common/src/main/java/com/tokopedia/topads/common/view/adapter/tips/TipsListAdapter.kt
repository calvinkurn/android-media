package com.tokopedia.topads.common.view.adapter.tips

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.topads.common.view.adapter.tips.delegate.TipsHeaderAdapterDelegate
import com.tokopedia.topads.common.view.adapter.tips.delegate.TipsRowAdapterDelegate
import com.tokopedia.topads.common.view.adapter.tips.delegate.TipsSortAdapterDelegate
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiSortViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel

class TipsListAdapter : BaseAdapter<TipsUiModel>() {
    private val tipsSortAdapterDelegate: TipsSortAdapterDelegate = TipsSortAdapterDelegate()

    init {
        delegatesManager
                .addDelegate(TipsHeaderAdapterDelegate())
                .addDelegate(TipsRowAdapterDelegate())
                .addDelegate(tipsSortAdapterDelegate)
    }

    fun setTipsItems(uiModels: List<TipsUiModel>) {
        setItems(uiModels)
        notifyDataSetChanged()
    }

    fun setOnUiSortItemClickListener(sortItemClick: TipsUiSortViewHolder.OnUiSortItemClick){
        this.tipsSortAdapterDelegate.onItemClick = sortItemClick
    }

}