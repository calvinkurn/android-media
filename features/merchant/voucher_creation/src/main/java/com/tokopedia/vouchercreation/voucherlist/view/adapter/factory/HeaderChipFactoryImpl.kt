package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.HeaderChipViewHolder
import com.tokopedia.vouchercreation.voucherlist.model.HeaderChipUiModel

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipFactoryImpl(
        private val onClick: (element: HeaderChipUiModel) -> Unit
) : BaseAdapterTypeFactory(), HeaderChipFactory {

    override fun type(model: HeaderChipUiModel): Int = HeaderChipViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderChipViewHolder.RES_LAYOUT -> HeaderChipViewHolder(parent, onClick)
            else -> super.createViewHolder(parent, type)
        }
    }
}