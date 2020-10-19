package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavItemViewModel

class MainNavItemViewHolder(itemView: View,
                            mainNavListener: MainNavListener
): AbstractViewHolder<MainNavItemViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_main_nav_item
    }

    override fun bind(element: MainNavItemViewModel) {
    }
}