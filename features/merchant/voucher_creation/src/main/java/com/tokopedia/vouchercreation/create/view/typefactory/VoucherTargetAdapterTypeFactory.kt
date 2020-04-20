package com.tokopedia.vouchercreation.create.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.VoucherTargetViewHolder

class VoucherTargetAdapterTypeFactory : BaseAdapterTypeFactory(), VoucherTargetTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            VoucherTargetViewHolder.LAYOUT -> VoucherTargetViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(voucherTargetUiModel: VoucherTargetUiModel): Int = VoucherTargetViewHolder.LAYOUT
}