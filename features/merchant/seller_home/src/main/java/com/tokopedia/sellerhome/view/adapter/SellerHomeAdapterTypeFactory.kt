package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.TrendLineViewUiModel
import com.tokopedia.sellerhome.view.viewholder.TrendLineViewHolder

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(trendLineViewUiModel: TrendLineViewUiModel): Int {
        return TrendLineViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TrendLineViewHolder.RES_LAYOUT -> TrendLineViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}