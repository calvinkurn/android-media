package com.tokopedia.v2.home.ui.adapter

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.v2.home.base.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.base.adapterdelegate.ViewTypeDelegateAdapter
import com.tokopedia.v2.home.ui.adapter.delegate.BannerDelegateAdapter
import com.tokopedia.v2.home.ui.adapter.delegate.LoadingDelegateAdapter

class HomeAdapter : BaseAdapterDelegate() {

    override var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.BANNER, BannerDelegateAdapter())
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