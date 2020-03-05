package com.tokopedia.centralized_promo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object DiffUtilHelper {
    fun calculateDiffInBackground(oldList: List<Visitable<*>>, newList: List<Visitable<*>>) = runBlocking(Dispatchers.IO) {
        DiffUtil.calculateDiff(CentralizedPromoDiffUtil(oldList, newList))
    }
}