package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
    }

    override fun bind(element: SeparatorViewModel) {
    }
}