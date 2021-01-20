package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
    }

    override fun bind(element: SeparatorDataModel) {
    }
}