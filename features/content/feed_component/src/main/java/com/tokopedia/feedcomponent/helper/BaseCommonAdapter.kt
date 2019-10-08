package com.tokopedia.feedcomponent.helper

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.feedcomponent.helper.common.LoadingMoreAdapterDelegate

/**
 * Created by jegul on 2019-10-02.
 */
open class BaseCommonAdapter : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
                .addDelegate(LoadingMoreAdapterDelegate)
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    fun showLoading() {
        itemList.add(itemList.size, LoadingMoreModel())
        notifyDataSetChanged()
    }

    fun hideLoading() {
        if (itemList.isNotEmpty() && itemList[itemList.lastIndex] is LoadingMoreModel) itemList.removeAt(itemList.lastIndex)
        notifyDataSetChanged()
    }
}