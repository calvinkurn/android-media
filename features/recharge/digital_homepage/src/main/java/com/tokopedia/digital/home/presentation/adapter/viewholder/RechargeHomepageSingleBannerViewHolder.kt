package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSingleBannerModel
import com.tokopedia.digital.home.presentation.Util.RechargeHomepageSectionMapper
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.*
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
                view_recharge_home_single_banner_container.show()
                view_recharge_home_single_banner_shimmering.hide()

                val item = section.items[0]
                RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                        view_recharge_home_single_banner_header, section
                )

                view_recharge_home_single_banner_image.loadImage(item.mediaUrl)
                view_recharge_home_single_banner_image.setOnClickListener {
                    listener.onRechargeSectionItemClicked(item)
                }
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }

                view_recharge_home_single_banner_title.displayTextOrHide(section.title)
                view_recharge_home_single_banner_label.displayTextOrHide(section.subtitle)
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