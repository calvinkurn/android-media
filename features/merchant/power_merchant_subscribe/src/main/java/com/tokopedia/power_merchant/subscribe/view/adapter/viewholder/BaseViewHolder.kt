package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created By @ilhamsuaib on 08/03/21
 */

abstract class BaseViewHolder<T : Visitable<*>>(itemView: View) : AbstractViewHolder<T>(itemView) {

    protected fun <T : View> findView(viewId: Int): Lazy<T> {
        return lazy {
            itemView.findViewById<T>(viewId)
        }
    }
}