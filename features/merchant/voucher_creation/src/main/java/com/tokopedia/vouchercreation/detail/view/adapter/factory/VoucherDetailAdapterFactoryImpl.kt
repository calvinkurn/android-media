package com.tokopedia.vouchercreation.detail.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.detail.model.DividerUiModel
import com.tokopedia.vouchercreation.detail.model.TipsUiModel
import com.tokopedia.vouchercreation.detail.model.UsageProgressUiModel
import com.tokopedia.vouchercreation.detail.model.VoucherHeaderUiModel
import com.tokopedia.vouchercreation.detail.view.viewholder.DividerViewHolder
import com.tokopedia.vouchercreation.detail.view.viewholder.HeaderViewHolder
import com.tokopedia.vouchercreation.detail.view.viewholder.TipsViewHolder
import com.tokopedia.vouchercreation.detail.view.viewholder.UsageProgressViewHolder

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailAdapterFactoryImpl : BaseAdapterTypeFactory(), VoucherDetailAdapterFactory {

    override fun type(model: VoucherHeaderUiModel): Int = HeaderViewHolder.RES_LAYOUT

    override fun type(model: UsageProgressUiModel): Int = UsageProgressViewHolder.RES_LAYOUT

    override fun type(model: DividerUiModel): Int = DividerViewHolder.RES_LAYOUT

    override fun type(model: TipsUiModel): Int = TipsViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HeaderViewHolder.RES_LAYOUT -> HeaderViewHolder(parent)
            UsageProgressViewHolder.RES_LAYOUT -> UsageProgressViewHolder(parent)
            DividerViewHolder.RES_LAYOUT -> DividerViewHolder(parent)
            TipsViewHolder.RES_LAYOUT -> TipsViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}