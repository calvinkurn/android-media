package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageDualBannersModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.view_recharge_home_dual_banners.view.*

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageDualBannersViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageDualBannersModel>(itemView) {

    override fun bind(element: RechargeHomepageDualBannersModel) {
        val section = element.section
        val items = when (section.items.size) {
            in 0..1 -> null
            2 -> section.items
            else -> section.items.subList(0, 2)
        }
        with (itemView) {
            items?.run {
                view_recharge_home_dual_banners_container.show()
                view_recharge_home_dual_banners_shimmering.hide()

                view_recharge_home_dual_banners_title.displayTextOrHide(section.title)
                view_recharge_home_dual_banners_image_1.loadImage(items[0].mediaUrl)
                view_recharge_home_dual_banners_image_2.loadImage(items[1].mediaUrl)
                view_recharge_home_dual_banners_image_1.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[0])
                }
                view_recharge_home_dual_banners_image_2.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[1])
                }
                this@with.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            }
            if (items == null) {
                if (section.title.isNotEmpty()) {
                    listener.onRechargeSectionEmpty(section.id)
                } else {
                    view_recharge_home_dual_banners_container.hide()
                    view_recharge_home_dual_banners_shimmering.show()

                    listener.loadRechargeSectionData(element.visitableId())
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_dual_banners
    }
}