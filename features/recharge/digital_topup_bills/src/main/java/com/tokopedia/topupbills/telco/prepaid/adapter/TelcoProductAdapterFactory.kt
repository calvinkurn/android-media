package com.tokopedia.topupbills.telco.prepaid.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topupbills.telco.data.TelcoCatalogDataCollection
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductMccmListViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductTitleViewHolder
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder

class TelcoProductAdapterFactory(val productType: Int, val listener: TelcoProductViewHolder.OnClickListener,
val mccmListener: TelcoProductMccmListViewHolder.OnClickListener?)
    : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TelcoProductTitleViewHolder.LAYOUT -> TelcoProductTitleViewHolder(parent)
            TelcoProductViewHolder.LAYOUT -> TelcoProductViewHolder(parent, productType, listener)
            TelcoProductMccmListViewHolder.LAYOUT -> TelcoProductMccmListViewHolder(parent, mccmListener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(telcoProduct: TelcoProduct): Int {
        return TelcoProductViewHolder.LAYOUT
    }

    fun type(telcoCatalogDataCollection: TelcoCatalogDataCollection): Int {
        return if (telcoCatalogDataCollection.isMccm()) {
            TelcoProductMccmListViewHolder.LAYOUT
        } else {
            TelcoProductTitleViewHolder.LAYOUT
        }
    }
}
