package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldViewHolder
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.PromotionTypeTickerViewHolder

class PromotionTypeItemAdapterFactory : BaseAdapterTypeFactory(), PromotionTypeItemTypeFactory, VoucherCommonTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PromotionTypeTickerViewHolder.LAYOUT -> PromotionTypeTickerViewHolder(parent)
            VoucherTextFieldViewHolder.LAYOUT -> VoucherTextFieldViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int = PromotionTypeTickerViewHolder.LAYOUT
    override fun type(voucherTextFieldUiModel: VoucherTextFieldUiModel): Int = VoucherTextFieldViewHolder.LAYOUT
}