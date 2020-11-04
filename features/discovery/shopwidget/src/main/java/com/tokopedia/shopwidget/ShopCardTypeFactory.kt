package com.tokopedia.shopwidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface ShopCardTypeFactory {
    fun type(type: String): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}