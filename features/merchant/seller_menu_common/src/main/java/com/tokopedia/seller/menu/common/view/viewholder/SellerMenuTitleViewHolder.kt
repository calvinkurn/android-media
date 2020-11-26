package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.ORDER_SECTION_TITLE
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.OTHER_SECTION_TITLE
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel.SectionTitleType.PRODUCT_SECTION_TITLE
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
            return if(type == OTHER_SECTION_TITLE) {
                SECTION_OTHER_LAYOUT
            } else {
                SECTION_WITH_CTA_LAYOUT
            }
        }
    }
    
    private val context by lazy { itemView.context }

    override fun bind(menu: SectionTitleUiModel) {
        itemView.textTitle.text = context.getString(menu.title)

        menu.ctaText?.let {
            itemView.textCta.show()
            itemView.textCta.text = context.getString(it)
        }

        menu.onClickApplink?.let { appLink ->
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, appLink)
                trackClickSection(menu)
            }
        }
    }

    private fun trackClickSection(menu: SectionTitleUiModel) {
        when(menu.type) {
            PRODUCT_SECTION_TITLE -> tracker?.sendEventAddProductClick()
            ORDER_SECTION_TITLE -> tracker?.sendEventClickOrderHistory()
            else -> {} // do nothing
        }
    }
}