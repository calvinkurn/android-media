package com.tokopedia.vouchergame.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.databinding.ViewDigitalEmptyProductListBinding
import com.tokopedia.common.topupbills.view.viewholder.TopupBillsEmptyViewHolder
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListShimmeringViewHolder
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameListAdapterFactory(val listener: VoucherGameListViewHolder.OnClickListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherGameListViewHolder.LAYOUT -> VoucherGameListViewHolder(parent, listener)
            VoucherGameListShimmeringViewHolder.LAYOUT -> VoucherGameListShimmeringViewHolder(parent)
            TopupBillsEmptyViewHolder.LAYOUT -> createVoucherGameEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(voucherGameOperator: VoucherGameOperator): Int {
        return VoucherGameListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return VoucherGameListShimmeringViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return TopupBillsEmptyViewHolder.LAYOUT
    }

    private fun createVoucherGameEmptyViewHolder(parent: View): AbstractViewHolder<out Visitable<*>> {
        val binding = ViewDigitalEmptyProductListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent as ViewGroup,
            false
        )
        return TopupBillsEmptyViewHolder(binding)
    }
}
