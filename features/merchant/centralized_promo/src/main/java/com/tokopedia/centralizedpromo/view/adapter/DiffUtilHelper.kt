package com.tokopedia.centralizedpromo.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

object DiffUtilHelper {
    fun calculateDiffInBackground(
            oldList: List<Visitable<*>>,
            newList: List<Visitable<*>>,
            onComplete: KSuspendFunction2<List<Visitable<*>>, DiffUtil.DiffResult, Unit>
    ) = CoroutineScope(Dispatchers.Default).launch {
        val result = DiffUtil.calculateDiff(CentralizedPromoDiffUtil(oldList, newList))
        onComplete(newList, result)
    }
}
