package com.tokopedia.feedcomponent.util.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel

/**
 * @author by yoasfs on 2019-12-30
 */

fun  MutableList<Visitable<*>>.copy() : MutableList<DynamicPostModel> {
    val newList : MutableList<DynamicPostModel> = mutableListOf()
    for (i in this) {
        if (i is DynamicPostModel) {
            newList.add(i.copy())
        }
    }
    return newList
}
