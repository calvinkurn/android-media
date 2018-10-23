package com.tokopedia.flashsale.management.view.adapter.viewholder

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.widget.ExpandableView.ExpandableLayoutListener
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.loadUrl
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoHeaderViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_camp_detail.view.*

class CampaignInfoHeaderViewHolder(view: View): AbstractViewHolder<CampaignInfoHeaderViewModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_camp_detail
    }

    override fun bind(element: CampaignInfoHeaderViewModel) {
        val campaign = element.campaign
        itemView.ivImageCampaign.loadUrl(campaign.cover, 20f)
        itemView.tvCampaignType.text = campaign.campaignType
        itemView.tvCampaignName.text = campaign.name
        itemView.tvStatus.text = campaign.status

        itemView.iv_arrow_down.setOnClickListener { itemView.base_expand_view.toggle() }
        itemView.iv_arrow_down.rotation = 0f
        itemView.base_expand_view.apply {
            setExpanded(false)
            setListener(object : ExpandableLayoutListener {
                override fun onAnimationEnd() {}

                override fun onOpened() {}

                override fun onAnimationStart() {}

                override fun onPreOpen() {
                    createRotateAnimator(itemView.iv_arrow_down, 0f, 180f).start()
                }

                override fun onClosed() {}

                override fun onPreClose() {
                    createRotateAnimator(itemView.iv_arrow_down, 180f, 0f).start()
                }
            })
        }
    }

    private fun createRotateAnimator(view: View, from: Float, to:Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "rotation", from, to).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
    }
}