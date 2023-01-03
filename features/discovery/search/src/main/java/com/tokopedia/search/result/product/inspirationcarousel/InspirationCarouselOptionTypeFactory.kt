package com.tokopedia.search.result.product.inspirationcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface InspirationCarouselOptionTypeFactory {
    fun type(type: String): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
