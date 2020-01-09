package com.tokopedia.feedcomponent.util.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel

/**
 * @author by yoasfs on 2019-12-30
 */

fun  MutableList<Visitable<*>>.copy() : MutableList<DynamicPostViewModel> {
    val newList : MutableList<DynamicPostViewModel> = mutableListOf()
    for (i in this) {
        if (i is DynamicPostViewModel) {
            newList.add(i.copy())
        }
    }
    return newList
}
