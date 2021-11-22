package com.tokopedia.cmhomewidget.presentation.adapter.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductCardBinding
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetViewAllCardBinding
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetViewAllCardViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductCardViewHolder
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetViewHolderTypeFactoryImpl @Inject constructor(
    private val cmHomeWidgetProductCardListener: CMHomeWidgetProductCardListener,
    private val cmHomeWidgetViewAllCardListener: CMHomeWidgetViewAllCardListener
) : CMHomeWidgetViewHolderTypeFactory, BaseAdapterTypeFactory() {

    override fun type(cmHomeWidgetProductCardData: CMHomeWidgetProductCardData): Int {
        return CMHomeWidgetProductCardViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetViewAllCardData: CMHomeWidgetViewAllCardData): Int {
        return CMHomeWidgetViewAllCardViewHolder.LAYOUT
    }

    override fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        return when (viewType) {
            CMHomeWidgetProductCardViewHolder.LAYOUT -> {
                CMHomeWidgetProductCardViewHolder(
                    LayoutCmHomeWidgetProductCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    cmHomeWidgetProductCardListener
                )
            }
            CMHomeWidgetViewAllCardViewHolder.LAYOUT -> {
                CMHomeWidgetViewAllCardViewHolder(
                    LayoutCmHomeWidgetViewAllCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    cmHomeWidgetViewAllCardListener
                )
            }
            else -> {
                super.createViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(viewType, parent, false), viewType
                )
            }
        }
    }
}