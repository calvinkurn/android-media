package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.DividerDataModel

/**
 * Created by dhaba
 */
class DividerViewHolder (itemView: View): AbstractViewHolder<DividerDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_divider
    }

    override fun bind(element: DividerDataModel) {
        //no-op
    }
}