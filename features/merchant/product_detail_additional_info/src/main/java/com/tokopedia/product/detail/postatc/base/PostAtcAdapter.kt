package com.tokopedia.product.detail.postatc.base

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.product.detail.postatc.component.error.ErrorDelegate
import com.tokopedia.product.detail.postatc.component.loading.LoadingDelegate
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoDelegate
import com.tokopedia.product.detail.postatc.component.recommendation.RecommendationDelegate

class PostAtcAdapter(
    listener: PostAtcListener
) : BaseAdapter<PostAtcUiModel>() {
    init {
        delegatesManager
            .addDelegate(ProductInfoDelegate(listener))
            .addDelegate(RecommendationDelegate(listener))
            .addDelegate(ErrorDelegate(listener))
            .addDelegate(LoadingDelegate())
    }

    fun addComponent(item: PostAtcUiModel) {
        addItem(item)
        notifyItemChanged(lastIndex)
    }

    fun removeComponent(uiModelId: Int) {
        val items = getItems()
        val findIndex = items.indexOfFirst { it.id == uiModelId }
        if (findIndex > -1) {
            removeItemAt(findIndex)
            notifyItemRemoved(findIndex)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : PostAtcUiModel> updateComponent(
        uiModelId: Int,
        updater: T.() -> Unit
    ) {
        val items = getItems()
        val findIndex = items.indexOfFirst { it.id == uiModelId }
        if (findIndex > -1) {
            val item = items[findIndex] as? T
            if (item != null) {
                updater.invoke(item)
                notifyItemChanged(findIndex)
            }
        }
    }

    /**
     * Replace current adapter items with new items
     */
    fun replaceComponents(items: List<PostAtcUiModel>) {
        val diffCallback = PostAtcDiffCallback(getItems(), items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        setItems(items)
    }
}

