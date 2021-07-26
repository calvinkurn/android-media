package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSingleBannerModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_recharge_home_single_banner.view.*

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageSingleBannerViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageSingleBannerModel>(itemView) {

    override fun bind(element: RechargeHomepageSingleBannerModel) {
        val section = element.section
        with(itemView) {
            if (section.items.isNotEmpty()) {
                val item = section.items[0]
                RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                        view_recharge_home_single_banner_header, element.channelModel,
                        object : HeaderListener {
                            override fun onSeeAllClick(link: String) {

                            }

                            override fun onChannelExpired(channelModel: ChannelModel) {
                                listener.onRechargeSectionEmpty(element.visitableId())
                            }
                        }
                )

                view_recharge_home_single_banner_image.loadImage(item.mediaUrl)
                view_recharge_home_single_banner_image.setOnClickListener {
                    listener.onRechargeSectionItemClicked(item)
                }
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }

                view_recharge_home_single_banner_title.displayTextOrHide(section.items.firstOrNull()?.title ?: "")

                if ((section.items.firstOrNull()?.subtitle ?: "").isNotEmpty()) {
                    view_recharge_home_single_banner_label.show()
                    view_recharge_home_single_banner_label.text = section.items.firstOrNull()?.subtitle
                } else view_recharge_home_single_banner_label.hide()

                view_recharge_home_single_banner_container.show()
                view_recharge_home_single_banner_shimmering.hide()

            } else {
                view_recharge_home_single_banner_container.hide()
                view_recharge_home_single_banner_shimmering.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_single_banner
    }
}