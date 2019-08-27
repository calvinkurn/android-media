package com.tokopedia.vouchergame.detail.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.TopupBillsEmptyViewHolder
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductCategoryViewHolder
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductShimmeringViewHolder
import com.tokopedia.vouchergame.detail.view.adapter.viewholder.VoucherGameProductViewHolder

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameDetailAdapterFactory(val callback: BaseEmptyViewHolder.Callback, val listener: VoucherGameProductViewHolder.OnClickListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherGameProductViewHolder.LAYOUT -> VoucherGameProductViewHolder(parent, listener)
            VoucherGameProductShimmeringViewHolder.LAYOUT -> VoucherGameProductShimmeringViewHolder(parent)
            VoucherGameProductCategoryViewHolder.LAYOUT -> VoucherGameProductCategoryViewHolder(parent)
            TopupBillsEmptyViewHolder.LAYOUT -> TopupBillsEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(product: VoucherGameProduct): Int {
        return VoucherGameProductViewHolder.LAYOUT
    }

    fun type(dataCollection: VoucherGameProductData.DataCollection): Int {
        return VoucherGameProductCategoryViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return VoucherGameProductShimmeringViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return TopupBillsEmptyViewHolder.LAYOUT
    }
}