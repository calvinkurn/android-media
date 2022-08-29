package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.HeaderChipResetViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.HeaderChipViewHolder

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipFactoryImpl(
        private val onClick: (element: BaseHeaderChipUiModel) -> Unit
) : BaseAdapterTypeFactory(), HeaderChipFactory {

    override fun type(model: BaseHeaderChipUiModel): Int {
        return when (model) {
            is ResetChip -> HeaderChipResetViewHolder.RES_LAYOUT
            else -> HeaderChipViewHolder.RES_LAYOUT
        }
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderChipViewHolder.RES_LAYOUT -> HeaderChipViewHolder(parent, onClick)
            HeaderChipResetViewHolder.RES_LAYOUT -> HeaderChipResetViewHolder(parent) {
                onClick(it)
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}