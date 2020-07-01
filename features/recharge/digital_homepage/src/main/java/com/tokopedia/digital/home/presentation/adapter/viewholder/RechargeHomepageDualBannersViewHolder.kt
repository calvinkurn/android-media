package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageDualBannersModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DUAL_BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DUAL_BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_dual_banners.view.*

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageDualBannersViewHolder(itemView: View?, val listener: OnItemBindListener) :
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
                view_recharge_home_dual_banners_title.displayTextOrHide(section.title)
                view_recharge_home_dual_banners_image_1.loadImage(items[0].mediaUrl)
                view_recharge_home_dual_banners_image_2.loadImage(items[1].mediaUrl)
                view_recharge_home_dual_banners_image_1.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[0], adapterPosition, DUAL_BANNER_CLICK)
                }
                view_recharge_home_dual_banners_image_2.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[1], adapterPosition, DUAL_BANNER_CLICK)
                }
                addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(items, DUAL_BANNER_IMPRESSION)
                }
            }
            if (items == null) listener.onRechargeSectionEmpty(adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_dual_banners
    }
}