package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel


import androidx.annotation.LayoutRes
import android.view.View
import android.widget.LinearLayout

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.compoundview.HeaderHomeView

/**
 * @author anggaprasetiyo on 11/12/17.
 */

class HeaderViewHolder(itemView: View, private val listener: HomeCategoryListener) : AbstractViewHolder<HeaderViewModel>(itemView) {
    private val mainContainer: LinearLayout = itemView.findViewById(R.id.container)

    override fun bind(element: HeaderViewModel) {
        mainContainer.removeAllViews()
        mainContainer.addView(HeaderHomeView(itemView.context, element, listener), 0)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_item_widget_ovo_tokopoint
    }
}
