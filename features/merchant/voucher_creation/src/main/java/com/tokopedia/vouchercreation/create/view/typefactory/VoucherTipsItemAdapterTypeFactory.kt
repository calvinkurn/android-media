package com.tokopedia.vouchercreation.create.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.BasicVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.DottedVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.ImageVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertips.BasicVoucherTipsItemViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertips.DottedVoucherTipsItemViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertips.ImageVoucherTipsItemViewHolder

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