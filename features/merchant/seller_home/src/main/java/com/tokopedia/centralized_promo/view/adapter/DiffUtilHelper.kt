package com.tokopedia.centralized_promo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object DiffUtilHelper {
    fun calculateDiffInBackground(oldList: List<Visitable<*>>, newList: List<Visitable<*>>) = runBlocking(Dispatchers.IO) {
        Thread.sleep(5000)
        DiffUtil.calculateDiff(CentralizedPromoDiffUtil(oldList, newList))
    }
}