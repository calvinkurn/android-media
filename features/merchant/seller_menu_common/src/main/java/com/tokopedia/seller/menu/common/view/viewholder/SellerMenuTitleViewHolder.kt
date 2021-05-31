package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.*
import kotlinx.android.synthetic.main.item_seller_menu_title_section.view.*

class SellerMenuTitleViewHolder(
        itemView: View,
        private val tracker: SellerMenuTracker?
) : AbstractViewHolder<SectionTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val SECTION_WITH_CTA_LAYOUT = R.layout.item_seller_menu_title_section

        @LayoutRes
        val SECTION_OTHER_LAYOUT = R.layout.item_seller_menu_title_other_section

        fun getLayout(type: SectionTitleType): Int {
            return if (type == OTHER_SECTION_TITLE) {
                SECTION_OTHER_LAYOUT
            } else {
                SECTION_WITH_CTA_LAYOUT
            }
        }
    }

    override fun bind(menu: SectionTitleUiModel) {
        itemView.textTitle.shouldShowWithAction(!menu.title.isNullOrBlank()) {
            itemView.textTitle.text = menu.title
        }

        itemView.textCta.shouldShowWithAction(!menu.ctaText.isNullOrBlank()) {
            itemView.textCta.text = menu.ctaText
        }

        menu.onClickApplink?.let { appLink ->
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, appLink)
                trackClickSection(menu)
            }
        }
    }

    private fun trackClickSection(menu: SectionTitleUiModel) {
        when (menu.type) {
            PRODUCT_SECTION_TITLE -> tracker?.sendEventAddProductClick()
            ORDER_SECTION_TITLE -> tracker?.sendEventClickOrderHistory()
            else -> {
                // no op
            }
        }
    }
}