package com.tokopedia.topupbills.telco.prepaid.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductEmptyViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductShimmeringViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductTitleViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder

class TelcoProductAdapterFactory(val productType: Int, val listener: TelcoProductViewHolder.OnClickListener)
    : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TelcoProductTitleViewHolder.LAYOUT -> TelcoProductTitleViewHolder(parent)
            TelcoProductViewHolder.LAYOUT -> TelcoProductViewHolder(parent, productType, listener)
            TelcoProductEmptyViewHolder.LAYOUT -> TelcoProductEmptyViewHolder(parent)
            TelcoProductShimmeringViewHolder.LAYOUT -> TelcoProductShimmeringViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: EmptyModel?): Int {
        return TelcoProductEmptyViewHolder.LAYOUT
    }

    fun type(telcoProduct: TelcoProduct): Int {
        return TelcoProductViewHolder.LAYOUT
    }

    fun type(telcoCatalogDataCollection: TelcoCatalogDataCollection): Int {
        return TelcoProductTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return TelcoProductShimmeringViewHolder.LAYOUT
    }
}
