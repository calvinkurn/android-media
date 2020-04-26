package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.voucherlist.model.EmptyStateUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ErrorStateUiModel
import com.tokopedia.vouchercreation.voucherlist.model.NoResultStateUiModel
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.EmptyStateViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.ErrorStateViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.NoResultStateViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.VoucherViewHolder

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListAdapterFactoryImpl(
        private val voucherListener: Listener,
        private val isActiveVoucher: Boolean
) : BaseAdapterTypeFactory(), VoucherListAdapterFactory {

    override fun type(voucher: VoucherUiModel): Int = VoucherViewHolder.RES_LAYOUT

    override fun type(emptyState: EmptyStateUiModel): Int = EmptyStateViewHolder.RES_LAYOUT

    override fun type(errorStateUiModel: ErrorStateUiModel): Int = ErrorStateViewHolder.RES_LAYOUT

    override fun type(noResultStateUiModel: NoResultStateUiModel): Int = NoResultStateViewHolder.RES_LAYOUT

    /*override fun type(viewModel: LoadingModel?): Int = if (isActiveVoucher) {
        VoucherActiveLoadingStateViewHolder.RES_LAYOUT
    } else {
        VoucherInactiveLoadingStateViewHolder.RES_LAYOUT
    }*/

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherViewHolder.RES_LAYOUT -> VoucherViewHolder(parent) {
                voucherListener.onMoreClickListener(it)
            }
            NoResultStateViewHolder.RES_LAYOUT -> NoResultStateViewHolder(parent)
            ErrorStateViewHolder.RES_LAYOUT -> ErrorStateViewHolder(parent)
            EmptyStateViewHolder.RES_LAYOUT -> EmptyStateViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {
        fun onMoreClickListener(voucher: VoucherUiModel)
    }
}