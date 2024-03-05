package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.DiscountUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading1UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading2UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading3UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.NormalTextUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ProductUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.SeparatorUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ShippingUiModel
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.DiscountViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.DividerViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.Heading1ViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.Heading2ViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.Heading3ViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.NormalTextViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.ProductViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail.ShippingViewHolder

class PurchaseDetailTypeFactory: BaseAdapterTypeFactory()  {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when(type) {
            Heading1ViewHolder.LAYOUT_ID -> return Heading1ViewHolder(parent)
            Heading2ViewHolder.LAYOUT_ID -> return Heading2ViewHolder(parent)
            Heading3ViewHolder.LAYOUT_ID -> return Heading3ViewHolder(parent)
            NormalTextViewHolder.LAYOUT_ID -> return NormalTextViewHolder(parent)
            ProductViewHolder.LAYOUT_ID -> return ProductViewHolder(parent)
            DiscountViewHolder.LAYOUT_ID -> return DiscountViewHolder(parent)
            ShippingViewHolder.LAYOUT_ID -> return ShippingViewHolder(parent)
            DividerViewHolder.LAYOUT_ID -> return DividerViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(model: Heading1UiModel): Int {
        return Heading1ViewHolder.LAYOUT_ID
    }

    fun type(model: Heading2UiModel): Int {
        return Heading2ViewHolder.LAYOUT_ID
    }

    fun type(model: Heading3UiModel): Int {
        return Heading3ViewHolder.LAYOUT_ID
    }

    fun type(model: NormalTextUiModel): Int {
        return NormalTextViewHolder.LAYOUT_ID
    }

    fun type(model: ProductUiModel): Int {
        return ProductViewHolder.LAYOUT_ID
    }

    fun type(model: DiscountUiModel): Int {
        return DiscountViewHolder.LAYOUT_ID
    }

    fun type(model: ShippingUiModel): Int {
        return ShippingViewHolder.LAYOUT_ID
    }

    fun type(model: SeparatorUiModel): Int {
        return DividerViewHolder.LAYOUT_ID
    }
}
