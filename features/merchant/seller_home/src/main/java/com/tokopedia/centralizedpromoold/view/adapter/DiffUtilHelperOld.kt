package com.tokopedia.centralizedpromoold.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

object DiffUtilHelperOld {
    fun calculateDiffInBackground(
            oldList: List<Visitable<*>>,
            newList: List<Visitable<*>>,
            onComplete: KSuspendFunction2<List<Visitable<*>>, DiffUtil.DiffResult, Unit>
    ) = CoroutineScope(Dispatchers.Default).launch {
        val result = DiffUtil.calculateDiff(CentralizedPromoDiffUtilOld(oldList, newList))
        onComplete(newList, result)
    }
}