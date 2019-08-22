package com.tokopedia.vouchergame.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductCategoryViewHolder
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameDetailAdapterFactory(val callback: BaseEmptyViewHolder.Callback, val listener: VoucherGameProductViewHolder.OnClickListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            VoucherGameProductViewHolder.LAYOUT -> return VoucherGameProductViewHolder(parent, listener)
//            VoucherGameProductShimmeringViewHolder.LAYOUT -> return VoucherGameProductShimmeringViewHolder(parent)
            VoucherGameProductCategoryViewHolder.LAYOUT -> return VoucherGameProductCategoryViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(product: VoucherGameProduct): Int {
        return VoucherGameProductViewHolder.LAYOUT
    }

    fun type(dataCollection: VoucherGameProductData.DataCollection): Int {
        return VoucherGameProductCategoryViewHolder.LAYOUT
    }
}