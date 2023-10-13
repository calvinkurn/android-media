package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeDualBannersBinding
import com.tokopedia.digital.home.model.RechargeHomepageDualBannersModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageDualBannersViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
    AbstractViewHolder<RechargeHomepageDualBannersModel>(itemView) {

    override fun bind(element: RechargeHomepageDualBannersModel) {
        val bind = ViewRechargeHomeDualBannersBinding.bind(itemView)
        val section = element.section
        val items = when (section.items.size) {
            in 0..1 -> null
            2 -> section.items
            else -> section.items.subList(0, 2)
        }
        with(bind) {
            items?.run {
                viewRechargeHomeDualBannersContainer.show()
                viewRechargeHomeDualBannersShimmering.root.hide()

                viewRechargeHomeDualBannersTitle.displayTextOrHide(section.title)
                viewRechargeHomeDualBannersImage1.loadImage(items[0].mediaUrl)
                viewRechargeHomeDualBannersImage2.loadImage(items[1].mediaUrl)
                viewRechargeHomeDualBannersImage1.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[0])
                }
                viewRechargeHomeDualBannersImage2.setOnClickListener {
                    listener.onRechargeSectionItemClicked(items[1])
                }
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }
            }
            if (items == null) {
                if (section.title.isNotEmpty()) {
                    listener.onRechargeSectionEmpty(section.id)
                } else {
                    viewRechargeHomeDualBannersContainer.hide()
                    viewRechargeHomeDualBannersShimmering.root.show()

                    listener.loadRechargeSectionData(element.visitableId())
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_dual_banners
    }
}
