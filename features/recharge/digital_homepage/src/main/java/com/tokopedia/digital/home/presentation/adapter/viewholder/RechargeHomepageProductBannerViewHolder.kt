package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageProductBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.PRODUCT_BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.PRODUCT_BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
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
    }

    override fun bind(element: RechargeHomepageProductBannerModel) {
        val section = element.section
        if (section.items.isNotEmpty()) {
            setBackground(section)
            setHeader(section)
            setProduct(section)
        } else {
            listener.onRechargeSectionEmpty(adapterPosition)
        }
    }

    private fun setHeader(section: RechargeHomepageSections.Section) {
        RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                itemView.view_recharge_home_product_banner_header, section
        )
    }

    // TODO: Set static gradient background & remove temporary background color
    private fun setBackground(section: RechargeHomepageSections.Section) {
//        itemView.deals_background.setGradientBackground(it.gradientColor)
    }

    private fun setProduct(section: RechargeHomepageSections.Section) {
        section.items.firstOrNull()?.run {
            setProductListener(section, this)
            setProductName(title)
            setProductDescription(subtitle)
            setProductPrice(label1)
            setProductSlashedPrice(label2)
            setProductImage(mediaUrl)
            setProductDiscountLabel(label3)
        }
    }

    private fun setProductListener(section: RechargeHomepageSections.Section, item: RechargeHomepageSections.Item) {
        with (itemView) {
            btn_recharge_home_product_banner_buy.setOnClickListener {
                listener.onRechargeSectionItemClicked(item, adapterPosition, PRODUCT_BANNER_CLICK)
            }
            addOnImpressionListener(section) {
                listener.onRechargeSectionItemImpression(section.items, PRODUCT_BANNER_IMPRESSION)
            }
            iv_recharge_home_product_banner_close_button.setOnClickListener {
                view_recharge_home_product_banner_container.hide()
                listener.onRechargeProductBannerClose(section)
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
            tv_recharge_home_product_banner_slashed_price.paintFlags = tv_recharge_home_product_banner_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
