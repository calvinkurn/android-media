package com.tokopedia.tokofood.common.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

open class CustomPayloadViewHolder<T : Visitable<*>>(view: View) : AbstractViewHolder<T>(view) {

    override fun bind(element: T) {}

    override fun bind(element: T, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isEmpty()) return

        val customPayload = payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                it
            } else null
        }

        bindPayload(customPayload)
    }

    open fun bindPayload(payloads: Pair<*, *>?) {}
}
