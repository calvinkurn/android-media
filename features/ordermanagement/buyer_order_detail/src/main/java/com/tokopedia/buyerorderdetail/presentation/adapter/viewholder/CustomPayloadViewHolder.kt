package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

open class CustomPayloadViewHolder<T : Visitable<*>>(view: View) : AbstractViewHolder<T>(view) {

    override fun bind(element: T) {}

    override fun bind(element: T, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return

        val customPayload = payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                Pair(it.first, it.second)
            } else {
                null
            }
        }

        bindPayload(customPayload)
    }

    open fun bindPayload(payloads: Pair<*, *>?) {}
}
