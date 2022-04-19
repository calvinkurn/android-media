package com.tokopedia.adapterdelegate

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.adapterdelegate.common.*
import kotlin.reflect.KClass

/**
 * Created by jegul on 2019-10-02.
 */
open class BaseCommonAdapter(isFlexibleType: Boolean = false) : BaseDiffUtilAdapter<Any>(isFlexibleType) {

    init {
        delegatesManager
                .addDelegate(LoadingMoreAdapterDelegate())
                .addDelegate(EmptyAdapterDelegate())
                .addDelegate(EmptyResultAdapterDelegate())
                .addDelegate(ErrorNetworkAdapterDelegate())
                .addDelegate(LoadingAdapterDelegate())
    }

    @Deprecated(
            message = "It is better to check for loading from view / viewModel",
            level = DeprecationLevel.WARNING
    )
    val isLoading: Boolean
        get() {
            return if (lastIndex > -1) {
                val lastItem = getItem(lastIndex)
                lastItem is LoadingModel || lastItem is LoadingMoreModel
            } else {
                false
            }
        }

    @Deprecated(
            message = "It,is better to check for loading from view / viewModel",
            level = DeprecationLevel.WARNING
    )
    val isShowLoadingMore: Boolean
        get() = itemCount > 0

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is AbstractViewHolder<*>) holder.onViewRecycled()
    }

    fun showLoadMore() {
        insertAtLast(LoadingMoreModel())
    }

    fun hideLoadMore() {
        removeAtLast(LoadingMoreModel::class)
    }

    fun showPageLoad() {
        insertAtLast(LoadingModel())
    }

    fun hidePageLoad() {
        removeAtLast(LoadingModel::class)
    }

    @Deprecated(
            message = "It is better to be explicit about which loading to show",
            level = DeprecationLevel.WARNING
    )
    fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) showLoadMore()
            else showPageLoad()
        }
    }

    @Deprecated(
            message = "It is better to be explicit about which loading to hide",
            level = DeprecationLevel.WARNING
    )
    fun hideLoading() {
        if (isLoading) {
            hideLoadMore()
            hidePageLoad()
        }
    }

    private fun insertAtLast(item: Any) {
        addItem(itemList.size, item)
        notifyItemInserted(itemCount)
    }

    private fun <T: Any>removeAtLast(classType: KClass<T>) {
        if (itemList.isNotEmpty() && itemList[itemList.lastIndex]::class == classType) {
            removeItemAt(itemList.lastIndex)
            notifyItemRemoved(itemList.size)
        }
    }
}