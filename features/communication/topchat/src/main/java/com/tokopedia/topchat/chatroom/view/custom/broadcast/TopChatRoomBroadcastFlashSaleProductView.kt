package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.reimagine.getMaxHeightForGridCarouselView
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomProductCardMapper
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomProductCarouselUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomBroadcastFlashsaleProductBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class TopChatRoomBroadcastFlashSaleProductView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), CoroutineScope {

    private val masterJob = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.Main

    private var binding: TopchatChatroomBroadcastFlashsaleProductBinding
    private val mapper = TopChatRoomProductCardMapper(context)

    init {
        binding = TopchatChatroomBroadcastFlashsaleProductBinding.inflate(
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
    }

    fun bind(uiModel: TopChatRoomBroadcastUiModel) {
        this.uiModel = uiModel
        bindProduct(uiModel)
    }

    private fun bindProduct(uiModel: TopChatRoomBroadcastUiModel) {
        val carouselProduct = uiModel.productCarousel
        val singleProduct = uiModel.singleProduct
        when {
            (carouselProduct != null) -> {
                setProductCarouselListener()
                bindSyncProductCarousel(carouselProduct)
                setProductListData(carouselProduct.products)
            }
            (singleProduct != null) -> { // Single product is also RV with 1 items
                setProductCarouselListener()
                bindSyncProduct(singleProduct)
                setProductListData(listOf(singleProduct))
            }
            else -> {
                binding.topchatChatroomBroadcastFlashsaleRv.hide()
            }
        }
    }

    private fun setProductListData(productList: List<Visitable<*>>) {
        binding.topchatChatroomBroadcastFlashsaleRv.show()
        binding.topchatChatroomBroadcastFlashsaleRv.updateData(productList)
        setRvHeight(productList)
    }

    private fun setRvHeight(productList: List<Visitable<*>>) {
        launch {
            val listProductCard = productList.mapNotNull {
                if (it is ProductAttachmentUiModel && !it.isProductDummySeeMore()) {
                    mapper.mapToProductCard(it)
                } else {
                    null
                }
            }
            try {
                context?.let {
                    val layoutParams = binding.topchatChatroomBroadcastFlashsaleRv.layoutParams
                    layoutParams.height = listProductCard.getMaxHeightForGridCarouselView(context)
                    binding.topchatChatroomBroadcastFlashsaleRv.layoutParams = layoutParams
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun bindSyncProductCarousel(productCarousel: TopChatRoomProductCarouselUiModel) {
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

    private fun setProductCarouselListener() {
        val uiModel = uiModel
        if (uiModel != null) {
            productListener?.let {
                binding.topchatChatroomBroadcastFlashsaleRv.setListener(it, uiModel)
            }
        }
    }
}
