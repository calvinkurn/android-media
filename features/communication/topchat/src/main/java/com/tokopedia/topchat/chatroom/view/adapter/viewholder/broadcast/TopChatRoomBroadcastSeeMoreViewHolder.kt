package com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.clearImage
import com.tokopedia.productcard.compact.productcardcarousel.util.ProductCardExtension.setProductCarouselWidth
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastProductSeeMoreItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomBroadcastSeeMoreViewHolder(
    itemView: View,
    private val broadcastUiModel: TopChatRoomBroadcastUiModel,
    private val productListener: TopChatRoomBroadcastProductListener
) : AbstractViewHolder<ProductAttachmentUiModel>(itemView) {

    private val binding: TopchatChatroomBroadcastProductSeeMoreItemBinding? by viewBinding()
    private var uiModel: ProductAttachmentUiModel? = null

    init {
        binding?.topchatChatroomBroadcastSeeMoreCard?.setProductCarouselWidth()
        setListener()
    }

    override fun bind(uiModel: ProductAttachmentUiModel) {
        this.uiModel = uiModel
        binding?.topchatChatroomBroadcastSeeMoreTv?.text = uiModel.productName
        impressTracker(uiModel)
    }

    private fun impressTracker(uiModel: ProductAttachmentUiModel) {
        val banner = broadcastUiModel.banner
        if (banner != null && !banner.isLoading) {
            binding?.root?.addOnImpressionListener(uiModel.impressHolder) {
                productListener.onImpressionBroadcastSeeMoreProduct(
                    blastId = broadcastUiModel.blastId,
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString()
                )
            }
        }
    }

    private fun setListener() {
        binding?.root?.setOnClickListener {
            val banner = broadcastUiModel.banner
            if (banner != null) {
                val appLink = uiModel?.androidUrl.takeIf { it?.isNotBlank() == true }
                    ?: uiModel?.productUrl.takeIf { it?.isNotBlank() == true }
                    ?: ""
                productListener.onClickBroadcastSeeMoreProduct(
                    blastId = broadcastUiModel.blastId,
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString(),
                    appLink = appLink
                )
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.topchatChatroomBroadcastSeeMoreArrow?.clearImage()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topchat_chatroom_broadcast_product_see_more_item
    }
}
