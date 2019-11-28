package com.tokopedia.digital.productV2.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.TopupBillsEmptyViewHolder
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListShimmeringViewHolder
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder

/**
 * @author by resakemal on 12/08/19
 */

class DigitalProductAdapterFactory(val listener: VoucherGameListViewHolder.OnClickListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherGameListViewHolder.LAYOUT -> VoucherGameListViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(voucherGameOperator: VoucherGameOperator): Int {
        return VoucherGameListViewHolder.LAYOUT
    }

}