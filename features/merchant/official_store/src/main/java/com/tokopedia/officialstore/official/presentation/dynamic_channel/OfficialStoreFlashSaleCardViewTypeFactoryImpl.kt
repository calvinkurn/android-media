package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.viewmodel.ProductFlashSaleDataModel

class OfficialStoreFlashSaleCardViewTypeFactoryImpl(
        private val dcEventHandler: DynamicChannelEventHandler,
        private val transparentProductFlashSaleClickListener: TransparentProductFlashSaleClickListener?,
        private val channel: Channel
) : BaseAdapterTypeFactory(), OfficialStoreFlashSaleCardViewTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductFlashSaleViewHolder.LAYOUT -> ProductFlashSaleViewHolder(parent, channel, dcEventHandler)
            TransparentProductFlashSaleViewHolder.LAYOUT -> TransparentProductFlashSaleViewHolder(parent, transparentProductFlashSaleClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(productFlashSaleDataModel: ProductFlashSaleDataModel): Int {
        return ProductFlashSaleViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return TransparentProductFlashSaleViewHolder.LAYOUT
    }
}
