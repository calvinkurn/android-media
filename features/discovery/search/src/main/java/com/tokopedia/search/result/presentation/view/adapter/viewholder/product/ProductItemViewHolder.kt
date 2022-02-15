package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.helper.ExoPlayerListener
import com.tokopedia.productcard.helper.ProductCardViewHelper
import com.tokopedia.productcard.helper.ProductVideoPlayer
import com.tokopedia.productcard.helper.VideoPlayerState
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.model.LabelGroupVariantDataView
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class ProductItemViewHolder(
        itemView: View,
        protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemDataView>(itemView), ExoPlayerListener, ProductVideoPlayer, CoroutineScope {

    abstract val productCardView: IProductCardView?
    protected var helper: ProductCardViewHelper? = null

    protected var productCardModel: ProductCardModel? = null

    private val masterJob = Job()

    override val coroutineContext: CoroutineContext
        get() = masterJob + Dispatchers.IO

    private var videoPlayerStateFlow : MutableStateFlow<VideoPlayerState>? = null

    protected fun initVideoHelper() {
        val videoPlayer = productCardView?.getProductVideoView() ?: return
        helper = ProductCardViewHelper.Builder(videoPlayer.context, videoPlayer)
            .setExoPlayerEventsListener(this)
            .create()
    }

    protected fun ProductItemDataView.toProductCardModel(
        productImage: String,
        isWideContent: Boolean,
    ): ProductCardModel {
        return ProductCardModel(
            productImageUrl = productImage,
            productName = productName,
            discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
            slashedPrice = if (discountPercentage > 0) originalPrice else "",
            formattedPrice = price,
            priceRange = priceRange ?: "",
            shopBadgeList = badgesList.toProductCardModelShopBadges(),
            shopLocation = if (shopCity.isNotEmpty()) shopCity else shopName,
            freeOngkir = freeOngkirDataView.toProductCardModelFreeOngkir(),
            isTopAds = isTopAds || isOrganicAds,
            countSoldRating = ratingString,
            hasThreeDots = true,
            labelGroupList = labelGroupList.toProductCardModelLabelGroup(),
            labelGroupVariantList = labelGroupVariantList.toProductCardModelLabelGroupVariant(),
            isWideContent = isWideContent,
            customVideoURL = customVideoURL
        )
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupDataView>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl = it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupVariantDataView>?.toProductCardModelLabelGroupVariant(): List<ProductCardModel.LabelGroupVariant> {
        return this?.map {
            ProductCardModel.LabelGroupVariant(type = it.type, typeVariant = it.typeVariant, title = it.title, hexColor = it.hexColor)
        } ?: listOf()
    }

    protected fun createImageProductViewHintListener(productItemData: ProductItemDataView): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItemData, adapterPosition)
            }
        }
    }

    fun registerLifecycleObserver(productCardModel: ProductCardModel) {
        val productCardView = productCardView ?: return

        productListener.productCardLifecycleObserver?.register(productCardView, productCardModel)
    }

    override fun onViewRecycled() {
        val productCardView = this.productCardView ?: return
        onViewDetach()

        productCardView.recycle()
        productListener.productCardLifecycleObserver?.unregister(productCardView)
    }

    override val hasProductVideo: Boolean
        get() = productCardModel?.hasVideo ?: false

    override fun playVideo(): Flow<VideoPlayerState> {
        val productCardModel = productCardModel ?: return flowOf(VideoPlayerState.NoVideo)
        if(!productCardModel.hasVideo) return flowOf(VideoPlayerState.NoVideo)
        videoPlayerStateFlow = MutableStateFlow(VideoPlayerState.Starting)
        helper?.play(productCardModel.customVideoURL)
        return videoPlayerStateFlow as Flow<VideoPlayerState>
    }

    override fun stopVideo() {
        helper?.stop()
    }

    private fun onViewDetach(){
        helper?.onViewDetach()
        masterJob.cancelChildren()
    }

    override fun onPlayerIdle() {
        val productCardView = this.productCardView ?: return
        productCardView.getProductVideoView()?.hide()
    }

    override fun onPlayerBuffering() {
        launch {
            videoPlayerStateFlow?.emit(VideoPlayerState.Buffering)
        }
    }

    override fun onPlayerPlaying() {
        val productCardView = this.productCardView ?: return
        productCardView.getProductVideoView()?.show()
        launch {
            videoPlayerStateFlow?.emit(VideoPlayerState.Playing)
        }
    }

    override fun onPlayerPaused() {
        val productCardView = this.productCardView ?: return
        productCardView.getProductVideoView()?.hide()
        launch {
            videoPlayerStateFlow?.emit(VideoPlayerState.Paused)
        }
    }

    override fun onPlayerEnded() {
        val productCardView = this.productCardView ?: return
        productCardView.getProductVideoView()?.hide()
        launch {
            videoPlayerStateFlow?.emit(VideoPlayerState.Ended)
        }
    }

    override fun onPlayerError(errorString: String?) {
        launch {
            videoPlayerStateFlow?.emit(VideoPlayerState.Error(errorString ?: ""))
        }
    }
}