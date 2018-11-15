package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.LinearInterpolator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.ekstension.*
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoHeaderViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_camp_detail.view.*

class CampaignInfoHeaderViewHolder(view: View, private val sellerStatus: SellerStatus,
                                   val onClickProductList: () -> Unit): AbstractViewHolder<CampaignInfoHeaderViewModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_camp_detail
    }

    override fun bind(element: CampaignInfoHeaderViewModel) {
        val campaign = element.campaign
        itemView.ivImageCampaign.loadUrl(campaign.cover, 20f)
        itemView.tvCampaignType.text = campaign.name
        itemView.tvCampaignName.text = campaign.name
        itemView.tvCampaignDate.text = campaign.campaignPeriod
        itemView.tvCampaignType.text = campaign.campaignType
        val imageStepRes = FlashSaleConstant.statusStepImages.get(campaign.statusInfo.label.toLowerCase())
        if (imageStepRes == null){
            itemView.image_step.gone()
        } else {
            itemView.image_step.visible()
            itemView.image_step.setImageResource(imageStepRes)
        }
        with(itemView.tvStatus){
            text = campaign.statusInfo.label
            val (textColor, bgColor) = FlashSaleConstant.statusColorList.get(campaign.statusInfo.label.toLowerCase()) ?:
            FlashSaleConstant.defaultPairColor
            setTextDrawableColor(ContextCompat.getColor(itemView.context, textColor))
            val bgDrawable = background.mutate() as GradientDrawable
            bgDrawable.setColor(ContextCompat.getColor(context, bgColor))
            bgDrawable.invalidateSelf()
        }
        itemView.title_process.text = campaign.statusInfo.head
        itemView.desc_process.text = campaign.statusInfo.subText

        itemView.iv_arrow_down.rotation = 0f
        itemView.iv_arrow_down.setOnClickListener {toggle()}
        itemView.tvStatus.setOnClickListener { toggle() }

        if (sellerStatus.isEligible) {
            itemView.btn_list_product.visible()
            itemView.btn_list_product.setOnClickListener { onClickProductList() }
        } else itemView.btn_list_product.gone()

        toggle()
    }

    private fun toggle(){
        if (itemView.base_expand_view.isVisible) {
            createRotateAnimator(itemView.iv_arrow_down, 180f, 0f).start()
            itemView.base_expand_view.gone()
        } else {
            createRotateAnimator(itemView.iv_arrow_down, 0f, 180f).start()
            itemView.base_expand_view.visible()
        }
    }

    private fun createRotateAnimator(view: View, from: Float, to:Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "rotation", from, to).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
    }
}