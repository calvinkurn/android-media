package com.tokopedia.cmhomewidget.presentation.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProduct
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable

interface CMHomeWidgetViewHolderTypeFactory : AdapterTypeFactory {

    fun type(cmHomeWidgetProduct: CMHomeWidgetProduct): Int

    fun type(cmHomeWidgetCard: CMHomeWidgetCard): Int

    fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*>
}