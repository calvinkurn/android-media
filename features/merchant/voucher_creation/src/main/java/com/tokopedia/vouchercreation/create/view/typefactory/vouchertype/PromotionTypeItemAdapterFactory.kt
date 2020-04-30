package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationViewHolder
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldViewHolder
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.NextButtonViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.widget.PromotionTypeTickerViewHolder

class PromotionTypeItemAdapterFactory : BaseAdapterTypeFactory(), PromotionTypeItemTypeFactory, VoucherCommonTypeFactory, CreateVoucherTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PromotionTypeTickerViewHolder.LAYOUT -> PromotionTypeTickerViewHolder(parent)
            VoucherTextFieldViewHolder.LAYOUT -> VoucherTextFieldViewHolder(parent)
            PromotionExpenseEstimationViewHolder.LAYOUT -> PromotionExpenseEstimationViewHolder(parent)
            NextButtonViewHolder.LAYOUT -> NextButtonViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int = PromotionTypeTickerViewHolder.LAYOUT
    override fun type(voucherTextFieldUiModel: VoucherTextFieldUiModel): Int = VoucherTextFieldViewHolder.LAYOUT
    override fun type(promotionExpenseEstimationUiModel: PromotionExpenseEstimationUiModel): Int = PromotionExpenseEstimationViewHolder.LAYOUT
}