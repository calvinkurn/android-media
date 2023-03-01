package com.tokopedia.product.detail.postatc.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.adapterdelegate.AdapterDelegatesManager
import com.tokopedia.product.detail.postatc.component.error.ErrorDelegate
import com.tokopedia.product.detail.postatc.component.loading.LoadingDelegate
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoDelegate
import com.tokopedia.product.detail.postatc.component.recommendation.RecommendationDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PostAtcAdapter(
    listener: PostAtcListener,
    override val coroutineContext: CoroutineContext = Dispatchers.IO
) : ListAdapter<PostAtcUiModel, PostAtcViewHolder<*>>(PostAtcDiffItemCallback), CoroutineScope {

    private val delegatesManager = AdapterDelegatesManager<PostAtcUiModel>()

    init {
        delegatesManager
            .addDelegate(ProductInfoDelegate(listener))
            .addDelegate(RecommendationDelegate(listener))
            .addDelegate(ErrorDelegate(listener))
            .addDelegate(LoadingDelegate())
    }

    private val mapUiModels = mutableMapOf<Int, PostAtcUiModel>()

    fun replaceComponents(items: List<PostAtcUiModel>) {
        mapUiModels.clear()
        items.forEach {
            mapUiModels[it.id] = it
        }
        updateUi()
    }

    fun removeComponent(uiModelId: Int) {
        mapUiModels.remove(uiModelId)
        updateUi()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : PostAtcUiModel> updateComponent(
        uiModelId: Int,
        updater: T.() -> Unit
    ) {
        val item = mapUiModels[uiModelId]?.newInstance() as? T
        if (item != null) {
            updater.invoke(item)
            mapUiModels[uiModelId] = item
            updateUi()
        }
    }

    private fun updateUi() {
        val list: List<PostAtcUiModel> = mapUiModels.values.toList()
        submitList(list)
    }

    /**
     * Start - Override Area
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAtcViewHolder<*> {
        return delegatesManager.onCreateViewHolder(parent, viewType) as PostAtcViewHolder<*>
    }

    override fun onBindViewHolder(holder: PostAtcViewHolder<*>, position: Int) {
        delegatesManager.onBindViewHolder(currentList, position, holder)
    }

    override fun onBindViewHolder(
        holder: PostAtcViewHolder<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            delegatesManager.onBindViewHolder(
                currentList,
                position,
                holder,
                payloads = payloads.filterIsInstance<Bundle>().reduce { acc, bundle ->
                    acc.apply { putAll(bundle) }
                }
            )
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(currentList, position)
    }

    /**
     * Finish - Override Area
     */
}
