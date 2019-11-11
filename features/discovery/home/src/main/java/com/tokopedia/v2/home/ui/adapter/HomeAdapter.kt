package com.tokopedia.v2.home.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.v2.home.base.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.ui.adapter.delegate.staticwidgets.*
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
class HomeAdapter : BaseAdapterDelegate() {

//    override var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateManager.addType(BannerDelegateAdapter())
        delegateManager.addType(TickerDelegateAdapter())
        delegateManager.addType(DynamicIconDelegateAdapter())
        delegateManager.addType(WalletDelegateAdapter())
        delegateManager.addType(WalletNonLoginDelegateAdapter())
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