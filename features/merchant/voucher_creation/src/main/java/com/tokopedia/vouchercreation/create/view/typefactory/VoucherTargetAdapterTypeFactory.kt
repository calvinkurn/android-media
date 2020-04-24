package com.tokopedia.vouchercreation.create.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.VoucherTargetUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.widgets.FillVoucherNameViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.widgets.NextButtonViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.widgets.VoucherTargetViewHolder

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
    override fun type(nextButtonUiModel: NextButtonUiModel): Int = NextButtonViewHolder.LAYOUT
    override fun type(fillVoucherNameUiModel: FillVoucherNameUiModel): Int = FillVoucherNameViewHolder.LAYOUT
}