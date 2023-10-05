package com.tokopedia.product.detail.postatc.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.adapterdelegate.AdapterDelegatesManager
import com.tokopedia.product.detail.postatc.view.component.addons.AddonsDelegate
import com.tokopedia.product.detail.postatc.view.component.error.ErrorDelegate
import com.tokopedia.product.detail.postatc.view.component.fallback.FallbackDelegate
import com.tokopedia.product.detail.postatc.view.component.loading.LoadingDelegate
import com.tokopedia.product.detail.postatc.view.component.productinfo.ProductInfoDelegate
import com.tokopedia.product.detail.postatc.view.component.recommendation.GlobalRecommendationDelegate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PostAtcAdapter(
    callback: PostAtcCallback,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    override val coroutineContext: CoroutineContext = coroutineDispatcher
) : ListAdapter<PostAtcUiModel, PostAtcViewHolder<*>>(PostAtcDiffItemCallback), CoroutineScope {

    companion object {
        private const val UI_UPDATE_DEBOUNCE = 200L
    }

    private val delegatesManager = AdapterDelegatesManager<PostAtcUiModel>()

    init {
        delegatesManager
            .addDelegate(AddonsDelegate(callback))
            .addDelegate(ErrorDelegate(callback))
            .addDelegate(FallbackDelegate(callback))
            .addDelegate(GlobalRecommendationDelegate(callback))
            .addDelegate(LoadingDelegate())
            .addDelegate(ProductInfoDelegate(callback))
    }

    private val mapUiModels = mutableMapOf<Int, PostAtcUiModel>()

    private val updateUiFlow = MutableSharedFlow<Unit>()

    private val updateUiJob = launch {
        updateUiFlow.debounce(UI_UPDATE_DEBOUNCE).collect {
            val list: List<PostAtcUiModel> = mapUiModels.values.toList()
            submitList(list)
        }
    }

    fun stop() {
        updateUiJob.cancel()
    }

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
        launch { updateUiFlow.emit(Unit) }
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
