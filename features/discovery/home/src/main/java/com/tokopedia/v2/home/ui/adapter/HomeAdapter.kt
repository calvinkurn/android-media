package com.tokopedia.v2.home.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.v2.home.base.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets.BannerDelegateAdapter
import com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets.DynamicIconDelegateAdapter
import com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets.TickerDelegateAdapter

class HomeAdapter : BaseAdapterDelegate() {

//    override var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateManager.addType(BannerDelegateAdapter())
        delegateManager.addType(TickerDelegateAdapter())
        delegateManager.addType(DynamicIconDelegateAdapter())
    }

    override fun diffUtil(oldList: List<ModelViewType>, newList: List<ModelViewType>): DiffUtil.Callback {
        return object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].getPrimaryKey() == newList[newItemPosition].getPrimaryKey()
            }

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].isContentsTheSame(newList[newItemPosition])
            }

        }
    }
}