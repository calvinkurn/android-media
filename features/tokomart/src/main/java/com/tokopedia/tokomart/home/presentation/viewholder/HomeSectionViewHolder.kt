package com.tokopedia.tokomart.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel
import kotlinx.android.synthetic.main.item_tokomart_home_section.view.*

class HomeSectionViewHolder(
    itemView: View
): AbstractViewHolder<HomeSectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_home_section
    }

    override fun bind(data: HomeSectionUiModel) {
        itemView.apply {
            textTitle.text = data.title
        }
    }
}
