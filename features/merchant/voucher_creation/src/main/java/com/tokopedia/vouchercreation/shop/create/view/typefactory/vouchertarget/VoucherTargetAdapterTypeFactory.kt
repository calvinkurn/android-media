package com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.VoucherTargetUiModel
import com.tokopedia.vouchercreation.shop.create.view.viewholder.NextButtonViewHolder
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.widgets.FillVoucherNameViewHolder
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.widgets.VoucherTargetViewHolder

class VoucherTargetAdapterTypeFactory : BaseAdapterTypeFactory(), VoucherTargetTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            VoucherTargetViewHolder.LAYOUT -> VoucherTargetViewHolder(parent)
            NextButtonViewHolder.LAYOUT -> NextButtonViewHolder(parent)
            FillVoucherNameViewHolder.LAYOUT -> FillVoucherNameViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(voucherTargetUiModel: VoucherTargetUiModel): Int = VoucherTargetViewHolder.LAYOUT
    override fun type(fillVoucherNameUiModel: FillVoucherNameUiModel): Int = FillVoucherNameViewHolder.LAYOUT
}