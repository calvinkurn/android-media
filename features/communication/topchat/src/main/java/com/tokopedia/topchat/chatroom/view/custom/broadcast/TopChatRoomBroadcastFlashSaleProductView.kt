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
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomProductCardMapper
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
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

    init {
        binding = TopchatChatroomBroadcastFlashsaleProductBinding.inflate(
            LayoutInflater.from(context),
            this
        )
    }

    private var productListener: TopChatRoomBroadcastProductListener? = null
    private var uiModel: TopChatRoomBroadcastUiModel? = null

    fun setListener(productListener: TopChatRoomBroadcastProductListener) {
        this.productListener = productListener
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
            binding.topchatChatroomBroadcastFlashsaleSingleProduct.hide()
            setProductCarouselListener()
            bindProductCarousel(carouselProduct)
        } else if (singleProduct != null) {
            binding.topchatChatroomBroadcastFlashsaleRv.hide()
            bindSingleProduct(singleProduct)
        } else {
            binding.topchatChatroomBroadcastFlashsaleSingleProduct.hide()
            binding.topchatChatroomBroadcastFlashsaleRv.hide()
        }
    }

    private fun bindProductCarousel(productCarousel: ProductCarouselUiModel) {
        binding.topchatChatroomBroadcastFlashsaleRv.show()
        binding.topchatChatroomBroadcastFlashsaleRv.updateData(productCarousel.products)
        setRvHeight(productCarousel.products)
    }

    private fun setRvHeight(productList: List<Visitable<*>>) {
        launch {
            val listProductCard = productList.mapNotNull {
                if (it is ProductAttachmentUiModel) {
                    TopChatRoomProductCardMapper.mapToProductCard(it)
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

    private fun bindSingleProduct(product: ProductAttachmentUiModel) {
        binding.topchatChatroomBroadcastFlashsaleSingleProduct.show()
        binding.topchatChatroomBroadcastFlashsaleSingleProduct.setProductModel(
            TopChatRoomProductCardMapper.mapToProductCard(product)
        )
        binding.topchatChatroomBroadcastFlashsaleSingleProduct.addOnImpressionListener(
            product.impressHolder
        ) {
            impressTracker()
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
        binding.topchatChatroomBroadcastFlashsaleSingleProduct.setOnClickListener {
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
        if (productListener != null && uiModel != null) {
            binding.topchatChatroomBroadcastFlashsaleRv.setListener(productListener!!, uiModel!!)
        }
    }

    fun cleanUp() {
        binding.topchatChatroomBroadcastFlashsaleSingleProduct.recycle()
    }
}
