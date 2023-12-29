package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView

interface InspirationKeywordsTypeFactory {
    fun type(item: InspirationKeywordDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}
