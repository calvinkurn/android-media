package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSectionHeaderBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowSectionHeaderViewHolder(
    itemView: View
) : AbstractViewHolder<TokoNowSectionHeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_section_header
    }

    private var binding: ItemTokopedianowSectionHeaderBinding? by viewBinding()

    override fun bind(section: TokoNowSectionHeaderUiModel) {
        binding?.apply {
            textTitle.text = section.title
            if (section.seeAllAppLink.isNotEmpty()) {
                textSeeAll.setOnClickListener {
                    RouteManager.route(it.context, section.seeAllAppLink)
                }
                textSeeAll.show()
            } else {
                textSeeAll.hide()
            }
        }
    }
}