package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductBannerModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

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
        val bind = ViewRechargeHomeProductBannerBinding.bind(itemView)
        val section = element.section
        if (section.items.isNotEmpty()) {
            setBackground(bind, section)
            setHeader(bind, element.channelModel, element.visitableId())
            setProduct(bind, section)

            bind.viewRechargeHomeProductBannerShimmering.root.hide()
            bind.viewRechargeHomeProductBannerLayout.show()
        } else {
            bind.viewRechargeHomeProductBannerShimmering.root.show()
            bind.viewRechargeHomeProductBannerLayout.hide()

            listener.loadRechargeSectionData(element.visitableId())
        }
    }

    private fun setHeader(
        bind: ViewRechargeHomeProductBannerBinding,
        channel: ChannelModel?,
        sectionId: String
    ) {
        RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
            bind.viewRechargeHomeProductBannerHeader,
            channel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    listener.onRechargeSectionEmpty(sectionId)
                }
            }
        )
    }

    private fun setBackground(
        bind: ViewRechargeHomeProductBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        try {
            if (section.label1.isNotEmpty()) {
                bind.viewRechargeHomeProductBannerBackground.setBackgroundColor(
                    Color.parseColor(
                        section.label1
                    )
                )
            } else {
                bind.viewRechargeHomeProductBannerBackground.setBackgroundColor(
                    MethodChecker.getColor(
                        bind.root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    )
                )
            }
        } catch (e: Throwable) {
            /* do nothing */
        }
    }

    private fun setProduct(
        bind: ViewRechargeHomeProductBannerBinding,
        section: RechargeHomepageSections.Section
    ) {
        section.items.firstOrNull()?.run {
            setProductListener(bind, section, this)
            setProductName(bind, title)
            setProductDescription(bind, subtitle)
            setProductPrice(bind, label3)
            setProductSlashedPrice(bind, label2)
            setProductImage(bind, mediaUrl)
            setProductDiscountLabel(bind, label1)
        }
    }

    private fun setProductListener(
        bind: ViewRechargeHomeProductBannerBinding,
        section: RechargeHomepageSections.Section,
        item: RechargeHomepageSections.Item
    ) {
        with(bind) {
            btnRechargeHomeProductBannerBuy.setOnClickListener {
                listener.onRechargeSectionItemClicked(item)
            }
            root.addOnImpressionListener(section) {
                listener.onRechargeSectionItemImpression(section)
            }
            ivRechargeHomeProductBannerCloseButton.setOnClickListener {
                listener.onRechargeProductBannerClosed(section)
            }
        }
    }

    private fun setProductName(bind: ViewRechargeHomeProductBannerBinding, productName: String) {
        bind.tvRechargeHomeProductBannerName.displayTextOrHide(productName)
    }

    private fun setProductDescription(
        bind: ViewRechargeHomeProductBannerBinding,
        productDesc: String
    ) {
        bind.tvRechargeHomeProductBannerDesc.displayTextOrHide(productDesc)
    }

    private fun setProductPrice(bind: ViewRechargeHomeProductBannerBinding, price: String) {
        bind.tvRechargeHomeProductBannerPrice.displayTextOrHide(price)
    }

    private fun setProductSlashedPrice(
        bind: ViewRechargeHomeProductBannerBinding,
        slashedPrice: String
    ) {
        with(bind) {
            if (slashedPrice.isEmpty()) {
                tvRechargeHomeProductBannerSlashedPrice.hide()
            } else {
                tvRechargeHomeProductBannerSlashedPrice.text = MethodChecker.fromHtml(slashedPrice)
                tvRechargeHomeProductBannerSlashedPrice.show()
            }
        }
    }

    private fun setProductImage(bind: ViewRechargeHomeProductBannerBinding, imageUrl: String) {
        bind.viewRechargeHomeProductBannerImage.cardImage.loadImage(imageUrl)
    }

    private fun setProductDiscountLabel(bind: ViewRechargeHomeProductBannerBinding, label: String) {
        if (label.isNotEmpty()) {
            bind.tvRechargeHomeProductBannerDiscountLabel.show()
            bind.tvRechargeHomeProductBannerDiscountLabel.setLabel(label)
        } else {
            bind.tvRechargeHomeProductBannerDiscountLabel.hide()
        }
    }
}
