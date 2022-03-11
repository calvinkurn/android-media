package com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.BasicVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.DottedVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.ImageVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips.BasicVoucherTipsItemViewHolder
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips.DottedVoucherTipsItemViewHolder
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips.ImageVoucherTipsItemViewHolder

class VoucherTipsItemAdapterTypeFactory : BaseAdapterTypeFactory(), VoucherTipsItemTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ImageVoucherTipsItemViewHolder.LAYOUT -> ImageVoucherTipsItemViewHolder(parent)
            DottedVoucherTipsItemViewHolder.LAYOUT -> DottedVoucherTipsItemViewHolder(parent)
            BasicVoucherTipsItemViewHolder.LAYOUT -> BasicVoucherTipsItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(imageVoucherTipsItemUiModel: ImageVoucherTipsItemUiModel): Int = ImageVoucherTipsItemViewHolder.LAYOUT

    override fun type(dottedVoucherTipsItemUiModel: DottedVoucherTipsItemUiModel): Int = DottedVoucherTipsItemViewHolder.LAYOUT

    override fun type(basicVoucherTipsItemUiModel: BasicVoucherTipsItemUiModel): Int = BasicVoucherTipsItemViewHolder.LAYOUT
}