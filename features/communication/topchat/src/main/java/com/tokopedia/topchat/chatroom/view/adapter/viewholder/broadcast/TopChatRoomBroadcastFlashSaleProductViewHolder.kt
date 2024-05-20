package com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomProductCardMapper
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastFlashsaleProductItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatRoomBroadcastFlashSaleProductViewHolder(
    itemView: View,
    private val broadcastUiModel: TopChatRoomBroadcastUiModel,
    private val productListener: TopChatRoomBroadcastProductListener
) : AbstractViewHolder<ProductAttachmentUiModel>(itemView) {

    private val binding: TopchatChatroomBroadcastFlashsaleProductItemBinding? by viewBinding()
    private var uiModel: ProductAttachmentUiModel? = null
    private val mapper = TopChatRoomProductCardMapper(itemView.context)

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
            mapper.mapToProductCard(uiModel)
        )
    }

    private fun impressTracker(uiModel: ProductAttachmentUiModel) {
        val banner = broadcastUiModel.banner
        if (banner != null && !banner.isLoading) {
            binding?.topchatChatroomBroadcastFlashsaleProductCard?.addOnImpressionListener(
                uiModel.impressHolder
            ) {
                productListener.onImpressionBroadcastProduct(
                    blastId = broadcastUiModel.blastId,
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString(),
                    productId = uiModel.productId,
                    position = layoutPosition,
                    totalProduct = getTotalProduct()
                )
            }
        }
    }

    private fun setListener() {
        binding?.topchatChatroomBroadcastFlashsaleProductCard?.setOnClickListener {
            productListener.onClickBroadcastProduct(
                blastId = broadcastUiModel.blastId,
                campaignStatus = broadcastUiModel.banner?.getCampaignStatusString().orEmpty(),
                campaignCountDown = broadcastUiModel.banner?.getCampaignCountDownString().orEmpty(),
                productId = uiModel?.productId.orEmpty(),
                position = layoutPosition,
                totalProduct = getTotalProduct(),
                androidUrl = uiModel?.androidUrl.orEmpty(),
                productUrl = uiModel?.productUrl.orEmpty()
            )
        }
    }

    private fun getTotalProduct(): Int {
        var totalProduct = broadcastUiModel.productCarousel?.products?.size.orZero()
        broadcastUiModel.productCarousel?.products?.find {
            it is ProductAttachmentUiModel && it.isProductDummySeeMore()
        }.let {
            totalProduct--
        }
        return totalProduct.coerceAtLeast(0)
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
