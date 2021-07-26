package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageProductBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_recharge_home_card_image.view.*
import kotlinx.android.synthetic.main.view_recharge_home_product_banner.view.*

/**
 * @author by resakemal on 15/06/20.
 */

class RechargeHomepageProductBannerViewHolder(
        val view: View,
        val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeHomepageProductBannerModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_product_banner
    }

    override fun bind(element: RechargeHomepageProductBannerModel) {
        val section = element.section
        if (section.items.isNotEmpty()) {
            setBackground(section)
            setHeader(element.channelModel, element.visitableId())
            setProduct(section)

            itemView.view_recharge_home_product_banner_shimmering.hide()
            itemView.view_recharge_home_product_banner_layout.show()
        } else {
            itemView.view_recharge_home_product_banner_shimmering.show()
            itemView.view_recharge_home_product_banner_layout.hide()

            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setHeader(channel: ChannelModel?,
                          sectionId: String) {
        RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                itemView.view_recharge_home_product_banner_header, channel,
                object : HeaderListener {
                    override fun onSeeAllClick(link: String) {

                    }

                    override fun onChannelExpired(channelModel: ChannelModel) {
                        listener.onRechargeSectionEmpty(sectionId)
                    }
                }
        )
    }

    private fun setBackground(section: RechargeHomepageSections.Section) {
        try {
            itemView.view_recharge_home_product_banner_background.setBackgroundColor(Color.parseColor(section.label1))
        } catch (e: Throwable) {
            /* do nothing */
        }
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
        with(itemView) {
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
        with(itemView) {
            if (slashedPrice.isEmpty()) {
                tv_recharge_home_product_banner_slashed_price.hide()
            } else {
                tv_recharge_home_product_banner_slashed_price.text = MethodChecker.fromHtml(slashedPrice)
                tv_recharge_home_product_banner_slashed_price.show()
            }
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
