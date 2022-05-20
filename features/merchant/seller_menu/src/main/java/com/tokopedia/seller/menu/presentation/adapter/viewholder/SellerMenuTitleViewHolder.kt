package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.databinding.ItemSellerMenuTitleOtherSectionBinding
import com.tokopedia.seller.menu.databinding.ItemSellerMenuTitleSectionBinding
import com.tokopedia.seller.menu.presentation.uimodel.SectionTitleUiModel
import com.tokopedia.seller.menu.presentation.uimodel.SectionTitleUiModel.SectionTitleType
import com.tokopedia.seller.menu.presentation.uimodel.SectionTitleUiModel.SectionTitleType.*
import com.tokopedia.unifyprinciples.Typography

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

    private var textTitle: Typography? = null
    private var textCta: Typography? = null

    override fun bind(menu: SectionTitleUiModel) {
        val layoutType = getLayout(menu.type)
        setupBinding(layoutType)
        textTitle?.shouldShowWithAction(!menu.title.isNullOrBlank()) {
            textTitle?.text = menu.title
        }

        if (layoutType == SECTION_WITH_CTA_LAYOUT) {
            textCta?.shouldShowWithAction(!menu.ctaText.isNullOrBlank()) {
                textCta?.text = menu.ctaText
            }
        }

        menu.onClickApplink?.let { appLink ->
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, appLink)
                trackClickSection(menu)
            }
        }
    }

    private fun setupBinding(layoutType: Int) {
        if (layoutType == SECTION_WITH_CTA_LAYOUT) {
            val binding = ItemSellerMenuTitleSectionBinding.bind(itemView)
            textTitle = binding.textTitle
            textCta = binding.textCta
        } else {
            val binding = ItemSellerMenuTitleOtherSectionBinding.bind(itemView)
            textTitle = binding.textTitle
        }
    }

    private fun trackClickSection(menu: com.tokopedia.seller.menu.presentation.uimodel.SectionTitleUiModel) {
        when (menu.type) {
            PRODUCT_SECTION_TITLE -> tracker?.sendEventAddProductClick()
            ORDER_SECTION_TITLE -> tracker?.sendEventClickOrderHistory()
            else -> {
                // no op
            }
        }
    }
}