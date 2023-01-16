package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface ViewToViewItemTypeFactory: AdapterTypeFactory {
    fun useBigLayout(useBigLayout: Boolean)
    fun type(viewToView: ViewToViewItemData): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
