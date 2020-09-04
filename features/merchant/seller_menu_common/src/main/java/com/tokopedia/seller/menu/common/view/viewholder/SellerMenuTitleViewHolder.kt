package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel
import kotlinx.android.synthetic.main.item_seller_menu_title_section.view.*

class SellerMenuTitleViewHolder(
        itemView: View,
        private val tracker: SellerMenuTracker?
) : AbstractViewHolder<SectionTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_title_section
    }

    override fun bind(menu: SectionTitleUiModel) {
        itemView.textTitle.text = itemView.context.getString(menu.title)
        itemView.textCta.text = itemView.context.getString(menu.ctaText)

        menu.onClickApplink?.let { appLink ->
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, appLink)
                sendTracker(menu)
            }
        }
    }

    private fun sendTracker(menu: SectionTitleUiModel) {

    }
}