package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R

/**
 * Created by dhaba
 */
class EmptyBlankViewHolder(itemView: View) : AbstractViewHolder<DynamicChannelDataModel>(itemView) {

    override fun bind(element: DynamicChannelDataModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_blank
    }
}