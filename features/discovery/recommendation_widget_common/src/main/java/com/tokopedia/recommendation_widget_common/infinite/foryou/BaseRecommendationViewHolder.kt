@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.recommendation_widget_common.infinite.foryou

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

abstract class BaseRecommendationViewHolder <T : Visitable<*>>(
    view: View,
    private val typeClass: Class<T>
) : AbstractViewHolder<T>(view) {

    override fun bind(element: T) {}

    override fun bind(element: T, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return

        val newItem = getNewItem(payloads)

        bindPayload(newItem)
    }

    open fun bindPayload(newItem: T?) {}

    private fun getNewItem(payloads: MutableList<Any>): T? {
        val newItem = payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (typeClass.isInstance(oldItem) && typeClass.isInstance(newItem)) {
                    if (oldItem != newItem) {
                        newItem as? T
                    } else {
                        null
                    }
                } else {
                    null
                }
            } else null
        }
        return newItem
    }
}

