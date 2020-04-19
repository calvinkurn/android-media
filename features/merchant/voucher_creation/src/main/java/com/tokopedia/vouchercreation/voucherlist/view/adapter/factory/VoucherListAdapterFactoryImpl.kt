package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.fragment.VoucherListFragment
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.VoucherViewHolder

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListAdapterFactoryImpl(
        private val fragment: VoucherListFragment,
        private val isActiveVoucher: Boolean
) : BaseAdapterTypeFactory(), VoucherListAdapterFactory {

    override fun type(voucher: VoucherUiModel): Int = VoucherViewHolder.RES_LAYOUT

    /*override fun type(viewModel: EmptyModel?): Int = EmptyStateViewHolder.RES_LAYOUT

    override fun type(viewModel: LoadingModel?): Int = if (isActiveVoucher) {
        VoucherActiveLoadingStateViewHolder.RES_LAYOUT
    } else {
        VoucherInactiveLoadingStateViewHolder.RES_LAYOUT
    }*/

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherViewHolder.RES_LAYOUT -> VoucherViewHolder(parent, fragment)
            else -> super.createViewHolder(parent, type)
        }
    }
}