package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.holder_home_nav_menu.view.*

class HomeNavMenuViewHolder(
        itemView: View,
        private val listener: HomeNavListener
): AbstractViewHolder<HomeNavMenuViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_menu
    }

    override fun bind(element: HomeNavMenuViewModel) {
        itemView.menu_title?.text = element.itemTitle
        itemView.menu_image.loadImage(element.srcImage, R.drawable.grey_button_rounded)
        itemView.setOnClickListener {
            listener.onMenuClick(element)
        }
    }
}