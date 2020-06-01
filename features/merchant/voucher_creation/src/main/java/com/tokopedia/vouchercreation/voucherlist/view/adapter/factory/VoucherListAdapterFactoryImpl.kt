package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.voucherlist.model.ui.*
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.*

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListAdapterFactoryImpl(
        private val voucherListener: VoucherViewHolder.Listener
) : BaseAdapterTypeFactory(), VoucherListAdapterFactory {

    override fun type(voucher: VoucherUiModel): Int = VoucherViewHolder.RES_LAYOUT

    override fun type(emptyState: EmptyStateUiModel): Int = EmptyStateViewHolder.RES_LAYOUT

    override fun type(errorStateUiModel: ErrorStateUiModel): Int = ErrorStateViewHolder.RES_LAYOUT

    override fun type(noResultStateUiModel: NoResultStateUiModel): Int = NoResultStateViewHolder.RES_LAYOUT

    override fun type(model: LoadingStateUiModel): Int = LoadingStateVoucherViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VoucherViewHolder.RES_LAYOUT -> VoucherViewHolder(parent, voucherListener)
            LoadingStateVoucherViewHolder.RES_LAYOUT -> LoadingStateVoucherViewHolder(parent)
            NoResultStateViewHolder.RES_LAYOUT -> NoResultStateViewHolder(parent)
            ErrorStateViewHolder.RES_LAYOUT -> ErrorStateViewHolder(parent, voucherListener::onErrorTryAgain)
            EmptyStateViewHolder.RES_LAYOUT -> EmptyStateViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}