package com.tokopedia.cmhomewidget.presentation.adapter.factory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardShimmerData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardShimmerData

interface CMHomeWidgetViewHolderTypeFactory : AdapterTypeFactory {

    fun type(cmHomeWidgetProductCardData: CMHomeWidgetProductCardData): Int

    fun type(cmHomeWidgetViewAllCardData: CMHomeWidgetViewAllCardData): Int

    fun type(cmHomeWidgetProductCardShimmerData: CMHomeWidgetProductCardShimmerData): Int

    fun type(cmHomeWidgetViewAllCardShimmerData: CMHomeWidgetViewAllCardShimmerData): Int

    fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*>
}