package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageProductBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_recharge_home_card_image.view.*
import kotlinx.android.synthetic.main.view_recharge_home_product_banner.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageProductBannerViewHolder(
        val view: View,
        val listener: OnItemBindListener
): AbstractViewHolder<RechargeHomepageProductBannerModel>(view) {

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_recharge_home_product_banner

        // TODO: Replace background color with backend data
        val PRODUCT_BANNER_BACKGROUND_GRADIENT = arrayListOf("#32AFFF", "#0066A9")
    }

    override fun bind(element: RechargeHomepageProductBannerModel) {
        val section = element.section
        if (section.items.isNotEmpty()) {
            itemView.view_recharge_home_product_banner_shimmering.hide()

            setBackground(section)
            setHeader(section)
            setProduct(section)
        } else {
            itemView.view_recharge_home_product_banner_shimmering.show()

            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setHeader(section: RechargeHomepageSections.Section) {
        RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                itemView.view_recharge_home_product_banner_header, section
        )
    }

    private fun setBackground(section: RechargeHomepageSections.Section) {
        itemView.view_recharge_home_product_banner_background.setGradientBackground(PRODUCT_BANNER_BACKGROUND_GRADIENT)
    }

    private fun setProduct(section: RechargeHomepageSections.Section) {
        section.items.firstOrNull()?.run {
            setProductListener(section, this)
            setProductName(title)
            setProductDescription(subtitle)
            setProductPrice(label3)
            setProductSlashedPrice(label2)
            setProductImage(mediaUrl)
            setProductDiscountLabel(label1)
        }
    }

    private fun setProductListener(section: RechargeHomepageSections.Section, item: RechargeHomepageSections.Item) {
        with (itemView) {
            btn_recharge_home_product_banner_buy.setOnClickListener {
                listener.onRechargeSectionItemClicked(item)
            }
            addOnImpressionListener(section) {
                listener.onRechargeSectionItemImpression(section)
            }
            iv_recharge_home_product_banner_close_button.setOnClickListener {
                listener.onRechargeProductBannerClosed(section)
            }
        }
    }

    private fun setProductName(productName: String) {
        itemView.tv_recharge_home_product_banner_name.displayTextOrHide(productName)
    }

    private fun setProductDescription(productDesc: String) {
        itemView.tv_recharge_home_product_banner_desc.displayTextOrHide(productDesc)
    }

    private fun setProductPrice(price: String) {
        itemView.tv_recharge_home_product_banner_price.displayTextOrHide(price)
    }

    private fun setProductSlashedPrice(slashedPrice: String) {
        with (itemView) {
            tv_recharge_home_product_banner_slashed_price.displayTextOrHide(slashedPrice)
            tv_recharge_home_product_banner_slashed_price.paintFlags =
                    tv_recharge_home_product_banner_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setProductImage(imageUrl: String) {
        itemView.view_recharge_home_product_banner_image.card_image.loadImage(imageUrl)
    }

    private fun setProductDiscountLabel(label: String) {
        if (label.isNotEmpty()) {
            itemView.tv_recharge_home_product_banner_discount_label.show()
            itemView.tv_recharge_home_product_banner_discount_label.setLabel(label)
        } else {
            itemView.tv_recharge_home_product_banner_discount_label.hide()
        }
    }
}
