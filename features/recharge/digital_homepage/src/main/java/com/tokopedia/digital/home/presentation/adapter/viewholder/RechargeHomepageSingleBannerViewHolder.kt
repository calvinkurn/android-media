package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSingleBannerModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SINGLE_BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.SINGLE_BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_single_banner.view.*

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageSingleBannerViewHolder(itemView: View?, val listener: OnItemBindListener) :
        AbstractViewHolder<RechargeHomepageSingleBannerModel>(itemView) {

    override fun bind(element: RechargeHomepageSingleBannerModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                val item = section.items[0]
                RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                        view_recharge_home_single_banner_header, section
                )

                view_recharge_home_single_banner_image.loadImage(item.mediaUrl)
                view_recharge_home_single_banner_image.setOnClickListener {
                    listener.onRechargeSectionItemClicked(item, adapterPosition, SINGLE_BANNER_CLICK)
                }
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section.items, SINGLE_BANNER_IMPRESSION)
                }

                view_recharge_home_single_banner_title.displayTextOrHide(section.title)
                view_recharge_home_single_banner_label.displayTextOrHide(section.subtitle)
            } else {
                listener.onRechargeSectionEmpty(adapterPosition)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_single_banner
    }
}