package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.common.view.VoucherCommonTypeFactory
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationViewHolder
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldViewHolder
import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.NextButtonViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item.CashbackTypePickerViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item.PromotionTypeInputListViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.item.PromotionTypeTickerViewHolder

class PromotionTypeItemAdapterFactory : BaseAdapterTypeFactory(), PromotionTypeItemTypeFactory, VoucherCommonTypeFactory, CreateVoucherTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PromotionTypeTickerViewHolder.LAYOUT -> PromotionTypeTickerViewHolder(parent)
            VoucherTextFieldViewHolder.LAYOUT -> VoucherTextFieldViewHolder(parent)
            PromotionExpenseEstimationViewHolder.LAYOUT -> PromotionExpenseEstimationViewHolder(parent)
            CashbackTypePickerViewHolder.LAYOUT -> CashbackTypePickerViewHolder(parent)
            NextButtonViewHolder.LAYOUT -> NextButtonViewHolder(parent)
            PromotionTypeInputListViewHolder.LAYOUT -> PromotionTypeInputListViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int = PromotionTypeTickerViewHolder.LAYOUT
    override fun type(voucherTextFieldUiModel: VoucherTextFieldUiModel): Int = VoucherTextFieldViewHolder.LAYOUT
    override fun type(promotionExpenseEstimationUiModel: PromotionExpenseEstimationUiModel): Int = PromotionExpenseEstimationViewHolder.LAYOUT
    override fun type(cashbackTypePickerUiModel: CashbackTypePickerUiModel): Int = CashbackTypePickerViewHolder.LAYOUT
    override fun type(promotionTypeInputListUiModel: PromotionTypeInputListUiModel): Int = PromotionTypeInputListViewHolder.LAYOUT
}