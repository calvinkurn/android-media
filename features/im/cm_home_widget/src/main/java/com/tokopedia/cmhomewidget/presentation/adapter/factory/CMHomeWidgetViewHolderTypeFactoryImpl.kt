package com.tokopedia.cmhomewidget.presentation.adapter.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetCardBinding
import com.tokopedia.cmhomewidget.databinding.LayoutCmHomeWidgetProductBinding
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetCard
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProduct
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductListener
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetCardViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.CMHomeWidgetProductViewHolder
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetViewHolderTypeFactoryImpl @Inject constructor(
    private val cmHomeWidgetProductListener: CMHomeWidgetProductListener,
    private val cmHomeWidgetCardListener: CMHomeWidgetCardListener
) : CMHomeWidgetViewHolderTypeFactory, BaseAdapterTypeFactory() {

    override fun type(cmHomeWidgetProduct: CMHomeWidgetProduct): Int {
        return CMHomeWidgetProductViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetCard: CMHomeWidgetCard): Int {
        return CMHomeWidgetCardViewHolder.LAYOUT
    }

    override fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        return when (viewType) {
            CMHomeWidgetProductViewHolder.LAYOUT -> {
                CMHomeWidgetProductViewHolder(
                    LayoutCmHomeWidgetProductBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    cmHomeWidgetProductListener
                )
            }
            CMHomeWidgetCardViewHolder.LAYOUT -> {
                CMHomeWidgetCardViewHolder(
                    LayoutCmHomeWidgetCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    cmHomeWidgetCardListener
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