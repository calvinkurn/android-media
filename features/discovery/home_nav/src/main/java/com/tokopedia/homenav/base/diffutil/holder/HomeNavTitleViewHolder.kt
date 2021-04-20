package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import kotlinx.android.synthetic.main.holder_home_nav_title.view.*

class HomeNavTitleViewHolder(itemView: View
): AbstractViewHolder<HomeNavTitleDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_title
    }

    override fun bind(element: HomeNavTitleDataModel) {
        itemView.title?.text = element.title
    }
}