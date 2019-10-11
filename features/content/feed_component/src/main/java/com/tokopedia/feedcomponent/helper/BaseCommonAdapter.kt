package com.tokopedia.feedcomponent.helper

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.feedcomponent.helper.common.*

/**
 * Created by jegul on 2019-10-02.
 */
open class BaseCommonAdapter : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
                .addDelegate(LoadingMoreAdapterDelegate())
                .addDelegate(EmptyAdapterDelegate())
                .addDelegate(EmptyResultAdapterDelegate())
                .addDelegate(ErrorNetworkAdapterDelegate())
                .addDelegate(LoadingAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    @JvmOverloads
    fun showLoading(isSmooth: Boolean = false) {
        addItem(itemList.size, LoadingMoreModel())
        if (isSmooth) notifyItemInserted(itemList.size) else notifyDataSetChanged()
    }

    @JvmOverloads
    fun hideLoading(isSmooth: Boolean = false) {
        if (itemList.isNotEmpty() && itemList[itemList.lastIndex] is LoadingMoreModel) {
            removeItemAt(itemList.lastIndex)
            if (isSmooth) notifyItemRemoved(itemList.size) else notifyDataSetChanged()
        }
    }
}