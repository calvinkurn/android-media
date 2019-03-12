package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel

/**
 * @author by milhamj on 11/03/19.
 */
class FilterViewHolder(itemView: View) : AbstractViewHolder<FilterViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.widget_af_chip
    }

    override fun bind(element: FilterViewModel?) {

    }
}