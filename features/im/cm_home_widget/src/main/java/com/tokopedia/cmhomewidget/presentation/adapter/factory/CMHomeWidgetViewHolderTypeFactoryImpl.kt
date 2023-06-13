package com.tokopedia.cmhomewidget.presentation.adapter.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.databinding.*
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.*
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetPaymentCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetProductCardListener
import com.tokopedia.cmhomewidget.listener.CMHomeWidgetViewAllCardListener
import com.tokopedia.cmhomewidget.presentation.adapter.viewholder.*
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetViewHolderTypeFactoryImpl @Inject constructor(
    private val cmHomeWidgetProductCardListener: CMHomeWidgetProductCardListener,
    private val cmHomeWidgetViewAllCardListener: CMHomeWidgetViewAllCardListener,
    private val cmHomeWidgetPaymentCardListener: CMHomeWidgetPaymentCardListener,
    ) : CMHomeWidgetViewHolderTypeFactory, BaseAdapterTypeFactory() {

    override fun type(cmHomeWidgetProductCardData: CMHomeWidgetProductCardData): Int {
        return CMHomeWidgetProductCardViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetViewAllCardData: CMHomeWidgetViewAllCardData): Int {
        return CMHomeWidgetViewAllCardViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetProductCardShimmerData: CMHomeWidgetProductCardShimmerData): Int {
        return CMHomeWidgetProductCardShimmerViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetViewAllCardShimmerData: CMHomeWidgetViewAllCardShimmerData): Int {
        return CMHomeWidgetViewAllCardShimmerViewHolder.LAYOUT
    }

    override fun type(cmHomeWidgetPaymentData: CMHomeWidgetPaymentData): Int {
        return CMHomeWidgetPaymentCardViewHolder.LAYOUT
    }

    override fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<*> {
        return when (viewType) {
            CMHomeWidgetProductCardViewHolder.LAYOUT -> {
                CMHomeWidgetProductCardViewHolder(
                    getProductCardBinding(parent),
                    cmHomeWidgetProductCardListener
                )
            }
            CMHomeWidgetViewAllCardViewHolder.LAYOUT -> {
                CMHomeWidgetViewAllCardViewHolder(
                    getViewAllCardBinding(parent),
                    cmHomeWidgetViewAllCardListener
                )
            }
            CMHomeWidgetProductCardShimmerViewHolder.LAYOUT -> {
                CMHomeWidgetProductCardShimmerViewHolder(
                    getProductCardShimmerBinding(parent)
                )
            }
            CMHomeWidgetViewAllCardShimmerViewHolder.LAYOUT -> {
                CMHomeWidgetViewAllCardShimmerViewHolder(
                    getViewAllCardShimmerBinding(parent)
                )
            }
            CMHomeWidgetPaymentCardViewHolder.LAYOUT -> {
                CMHomeWidgetPaymentCardViewHolder(
                    getPaymentCardBinding(parent),
                    cmHomeWidgetPaymentCardListener
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

    private fun getProductCardBinding(parent: ViewGroup): LayoutCmHomeWidgetProductCardBinding {
        return LayoutCmHomeWidgetProductCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    private fun getViewAllCardBinding(parent: ViewGroup): LayoutCmHtwdViewAllCardBinding {
        return LayoutCmHtwdViewAllCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    private fun getProductCardShimmerBinding(parent: ViewGroup): LayoutCmHomeWidgetProductCardShimmerBinding {
        return LayoutCmHomeWidgetProductCardShimmerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    private fun getViewAllCardShimmerBinding(parent: ViewGroup): LayoutCmHomeWidgetViewAllCardShimmerBinding {
        return LayoutCmHomeWidgetViewAllCardShimmerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    private fun getPaymentCardBinding(parent: ViewGroup): LayoutCmHomeWidgetPaymentCardBinding {
        return LayoutCmHomeWidgetPaymentCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }
}