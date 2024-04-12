package com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomProductCardMapper
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastFlashsaleProductItemBinding

class TopChatRoomBroadcastFlashSaleProductViewHolder(
    itemView: View,
    private val broadcastUiModel: TopChatRoomBroadcastUiModel,
    private val productListener: TopChatRoomBroadcastProductListener
) : AbstractViewHolder<ProductAttachmentUiModel>(itemView) {

    private val binding: TopchatChatroomBroadcastFlashsaleProductItemBinding? by viewBinding()
    private var uiModel: ProductAttachmentUiModel? = null

    init {
        setListener()
    }

    override fun bind(uiModel: ProductAttachmentUiModel) {
        this.uiModel = uiModel
        bindProduct(uiModel)
        impressTracker(uiModel)
    }

    private fun bindProduct(uiModel: ProductAttachmentUiModel) {
        binding?.topchatChatroomBroadcastFlashsaleLoaderProductCard?.showWithCondition(uiModel.isLoading)
        binding?.topchatChatroomBroadcastFlashsaleProductCard?.setProductModel(
            TopChatRoomProductCardMapper.mapToProductCard(uiModel)
        )
    }

    private fun impressTracker(uiModel: ProductAttachmentUiModel) {
        binding?.topchatChatroomBroadcastFlashsaleProductCard?.addOnImpressionListener(
            uiModel.impressHolder
        ) {
            val banner = broadcastUiModel.banner
            if (banner != null) {
                productListener.onImpressionBroadcastProduct(
                    blastId = broadcastUiModel.blastId,
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString(),
                    productId = uiModel.productId,
                    position = layoutPosition,
                    totalProduct = broadcastUiModel.productCarousel?.products?.size.orZero()
                )
            }
        }
    }

    private fun setListener() {
        binding?.root?.setOnClickListener {
            val banner = broadcastUiModel.banner
            if (banner != null) {
                productListener.onClickBroadcastProduct(
                    blastId = broadcastUiModel.blastId,
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString(),
                    productId = uiModel?.productId.orEmpty(),
                    position = layoutPosition,
                    totalProduct = broadcastUiModel.productCarousel?.products?.size.orZero(),
                    androidUrl = uiModel?.androidUrl.orEmpty(),
                    productUrl = uiModel?.productUrl.orEmpty()
                )
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.topchatChatroomBroadcastFlashsaleProductCard?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topchat_chatroom_broadcast_flashsale_product_item
    }
}
