package com.tokopedia.campaignlist.page.presentation.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.databinding.CampaignListItemLayoutBinding
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.Label

class ActiveCampaignViewHolder(
        private val binding: CampaignListItemLayoutBinding,
        private val clickListener: OnShareButtonClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnShareButtonClickListener {
        fun onShareButtonClicked(activeCampaign: ActiveCampaign)
    }

    companion object {
        private const val AVAILABLE_STATUS_ID = "5"
        private const val UPCOMING_STATUS_ID = "6"
        private const val ONGOING_STATUS_ID = "7"
        private const val UPCOMING_IN_NEAR_TIME_STATUS_ID = "14"
    }

    private var context: Context? = null

    init {
        context = binding.root.context
        binding.tpgShareButton.setOnClickListener {
            val activeCampaign = binding.root.getTag(R.id.active_campaign) as ActiveCampaign
            clickListener.onShareButtonClicked(activeCampaign)
        }
    }

    fun bindData(activeCampaign: ActiveCampaign, onImpressed : (ActiveCampaign) -> Unit) {
        // tag active campaign data to item view
        binding.root.setTag(R.id.active_campaign, activeCampaign)
        // render campaign type name
        binding.tpgCampaignType.text = activeCampaign.campaignType
        // render campaign status
        binding.labelCampaignStatus.text = activeCampaign.campaignStatus
        when (activeCampaign.campaignStatusId) {
            ONGOING_STATUS_ID -> binding.labelCampaignStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            UPCOMING_STATUS_ID, UPCOMING_IN_NEAR_TIME_STATUS_ID -> binding.labelCampaignStatus.setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
            AVAILABLE_STATUS_ID -> binding.labelCampaignStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            else -> binding.labelCampaignStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
        }
        // render campaign image
        binding.iuCampaignPicture.loadImage(activeCampaign.campaignPictureUrl)
        //render campaign name
        binding.tpgCampaignName.text = activeCampaign.campaignName
        // render product quantity
        context?.let {
            val productQuantity = it.getString(R.string.campaign_list_product_quantity_label, activeCampaign.productQty)
            binding.tpgProductQty.text = productQuantity
        }
        // render campaign start date, end date
        binding.tpgCampaignStartDate.text = activeCampaign.startDate
        binding.tpgCampaignEndDate.text = activeCampaign.endDate
        // render campaign start time, end time
        context?.let {
            val campaignStartTime = it.getString(R.string.campaign_time_template, activeCampaign.startTime)
            binding.tpgCampaignStartTime.text = campaignStartTime
            val campaignEndTime = it.getString(R.string.campaign_time_template, activeCampaign.endTime)
            binding.tpgCampaignEndTime.text = campaignEndTime
        }
        addImpressionListener(activeCampaign, onImpressed)
    }

    private fun addImpressionListener(campaign : ActiveCampaign, onImpressed : (ActiveCampaign) -> Unit) {
        binding.root.addOnImpressionListener(ImpressHolder()) { onImpressed(campaign) }
    }
}