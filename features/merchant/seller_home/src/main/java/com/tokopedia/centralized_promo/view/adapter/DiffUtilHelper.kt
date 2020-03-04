package com.tokopedia.centralized_promo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DiffUtilHelper {
    fun calculate(oldList: List<Visitable<*>>, newList: List<Visitable<*>>, adapter: BaseListAdapter<*, *>) = CoroutineScope(Dispatchers.IO).launchCatchError(block = {
        val result = DiffUtil.calculateDiff(CentralizedPromoDiffUtil(oldList, newList))
        dispatchResult(newList, result, adapter)
    }, onError = {
        throw it
    })

    private suspend fun dispatchResult(newList: List<Visitable<*>>, result: DiffUtil.DiffResult, adapter: BaseListAdapter<*, *>) = withContext(Dispatchers.Main) {
        adapter.setElements(newList)
        result.dispatchUpdatesTo(adapter)
    }
}