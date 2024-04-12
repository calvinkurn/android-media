package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomProductCardMapper
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastPromoProductBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TopChatRoomBroadcastPromoProductView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CoroutineScope {

    private val masterJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var binding: TopchatChatroomBroadcastPromoProductBinding

    init {
        binding = TopchatChatroomBroadcastPromoProductBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    private var productListener: TopChatRoomBroadcastProductListener? = null
    private var deferredAttachment: DeferredViewHolderAttachment? = null
    private var uiModel: TopChatRoomBroadcastUiModel? = null

    fun setListener(
        productListener: TopChatRoomBroadcastProductListener,
        deferredViewHolderAttachment: DeferredViewHolderAttachment
    ) {
        this.productListener = productListener
        this.deferredAttachment = deferredViewHolderAttachment
        setSingleProductListener()
    }

    fun bind(uiModel: TopChatRoomBroadcastUiModel) {
        this.uiModel = uiModel
        bindProduct(uiModel)
    }

    private fun bindProduct(uiModel: TopChatRoomBroadcastUiModel) {
        val carouselProduct = uiModel.productCarousel
        val singleProduct = uiModel.singleProduct
        if (carouselProduct != null && carouselProduct.products.isNotEmpty()) {
            binding.topchatChatroomBroadcastPromoSingleProduct.hide()
            setProductCarouselListener()
            bindProductCarousel(carouselProduct)
        } else if (singleProduct != null) {
            binding.topchatChatroomBroadcastPromoRv.hide()
            bindSingleProduct(singleProduct)
        } else {
            binding.topchatChatroomBroadcastPromoSingleProduct.hide()
            binding.topchatChatroomBroadcastPromoRv.hide()
        }
    }

    private fun bindProductCarousel(productCarousel: ProductCarouselUiModel) {
        bindSyncProductCarousel(productCarousel)
        binding.topchatChatroomBroadcastPromoRv.show()
        binding.topchatChatroomBroadcastPromoRv.updateData(productCarousel.products)
    }

    private fun bindSingleProduct(product: ProductAttachmentUiModel) {
        bindSyncProduct(product)
        binding.topchatChatroomBroadcastPromoLoaderSingleProduct.showWithCondition(product.isLoading)
        binding.topchatChatroomBroadcastPromoSingleProduct.show()
        binding.topchatChatroomBroadcastPromoSingleProduct.bind(
            TopChatRoomProductCardMapper.mapToProductCard(product)
        )
        binding.topchatChatroomBroadcastPromoSingleProduct.addOnImpressionListener(
            product.impressHolder
        ) {
            impressTracker()
        }
    }

    private fun bindSyncProductCarousel(productCarousel: ProductCarouselUiModel) {
        if (!productCarousel.isLoading()) return
        productCarousel.products.forEach {
            if (it is ProductAttachmentUiModel && !it.isProductDummySeeMore()) {
                bindSyncProduct(it)
            }
        }
    }

    private fun bindSyncProduct(product: ProductAttachmentUiModel) {
        if (!product.isLoading) return
        deferredAttachment?.let {
            val chatAttachments = deferredAttachment?.getLoadedChatAttachments()
            val attachment = chatAttachments?.get(product.attachmentId) ?: return
            if (attachment is ErrorAttachment) {
                product.syncError()
            } else {
                product.updateData(attachment.parsedAttributes)
            }
        }
    }

    private fun impressTracker() {
        val singleProduct = uiModel?.singleProduct
        val banner = uiModel?.banner
        if (singleProduct != null && banner != null) {
            productListener?.onImpressionBroadcastProduct(
                blastId = uiModel?.blastId.orEmpty(),
                campaignStatus = banner.getCampaignStatusString(),
                campaignCountDown = banner.getCampaignCountDownString(),
                productId = singleProduct.productId
            )
        }
    }

    private fun setSingleProductListener() {
        binding.topchatChatroomBroadcastPromoSingleProduct.setOnClickListener {
            val singleProduct = uiModel?.singleProduct
            val banner = uiModel?.banner
            if (singleProduct != null && banner != null) {
                productListener?.onClickBroadcastProduct(
                    blastId = uiModel?.blastId.orEmpty(),
                    campaignStatus = banner.getCampaignStatusString(),
                    campaignCountDown = banner.getCampaignCountDownString(),
                    productId = singleProduct.productId,
                    androidUrl = singleProduct.androidUrl,
                    productUrl = singleProduct.productUrl
                )
            }
        }
    }

    private fun setProductCarouselListener() {
        val uiModel = uiModel
        if (uiModel != null) {
            productListener?.let {
                binding.topchatChatroomBroadcastPromoRv.setListener(it, uiModel)
            }
        }
    }

    fun cleanUp() {
        binding.topchatChatroomBroadcastPromoSingleProduct.cleanUp()
    }
}
