package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeSingleBannerBinding
import com.tokopedia.digital.home.model.RechargeHomepageSingleBannerModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage

/**
 * @author by resakemal on 21/06/20.
 */

class RechargeHomepageSingleBannerViewHolder(itemView: View?, val listener: RechargeHomepageItemListener) :
        AbstractViewHolder<RechargeHomepageSingleBannerModel>(itemView) {

    override fun bind(element: RechargeHomepageSingleBannerModel) {
        val bind = ViewRechargeHomeSingleBannerBinding.bind(itemView)
        val section = element.section
        with(bind) {
            if (section.items.isNotEmpty()) {
                val item = section.items[0]
                RechargeHomepageSectionMapper.setDynamicHeaderViewChannel(
                    viewRechargeHomeSingleBannerHeader, element.channelModel,
                        object : HeaderListener {
                            override fun onSeeAllClick(link: String) {

                            }

                            override fun onChannelExpired(channelModel: ChannelModel) {
                                listener.onRechargeSectionEmpty(element.visitableId())
                            }
                        }
                )

                viewRechargeHomeSingleBannerImage.loadImage(item.mediaUrl)
                viewRechargeHomeSingleBannerImage.setOnClickListener {
                    listener.onRechargeSectionItemClicked(item)
                }
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }

                if ((section.items.firstOrNull()?.subtitle ?: "").isNotEmpty()) {
                    viewRechargeHomeSingleBannerLabel.show()
                    viewRechargeHomeSingleBannerLabel.text = section.items.firstOrNull()?.subtitle
                } else viewRechargeHomeSingleBannerLabel.hide()

                viewRechargeHomeSingleBannerContainer.show()
                viewRechargeHomeSingleBannerShimmering.root.hide()

            } else {
                viewRechargeHomeSingleBannerContainer.hide()
                viewRechargeHomeSingleBannerShimmering.root.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_single_banner
    }
}