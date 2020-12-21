package com.tokopedia.topads.common.view.adapter.tips

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.topads.common.view.adapter.tips.delegate.TipsHeaderAdapterDelegate
import com.tokopedia.topads.common.view.adapter.tips.delegate.TipsRowAdapterDelegate
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel

class TipsListAdapter : BaseAdapter<TipsUiModel>() {

    init {
        delegatesManager
                .addDelegate(TipsHeaderAdapterDelegate())
                .addDelegate(TipsRowAdapterDelegate())
    }

    fun setTipsItems(uiModels: List<TipsUiModel>) {
        setItems(uiModels)
        notifyDataSetChanged()
    }
}