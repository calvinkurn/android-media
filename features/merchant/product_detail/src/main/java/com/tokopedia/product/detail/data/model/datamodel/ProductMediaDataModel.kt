package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.product.detail.view.viewholder.media.model.LiveIndicatorUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

data class ProductMediaDataModel(
    val type: String = "",
    val name: String = "",
    var listOfMedia: List<MediaDataModel> = listOf(),
    var initialScrollPosition: Int = -1,
    var variantOptionIdScrollAnchor: String = "",
    var shouldUpdateImage: Boolean = false,
    var shouldAnimateLabel: Boolean = true,
    var containerType: MediaContainerType = MediaContainerType.Square,
    var recommendation: ProductMediaRecomData = ProductMediaRecomData(),
    var liveIndicator: LiveIndicatorUiModel = LiveIndicatorUiModel(),
    var isPrefetch: Boolean = false
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        isFinishedLoading = { false },
        customBlocksName = "ProductMediaDataModel"
    ) {
    companion object {
        const val VIDEO_TYPE = "video"
        const val IMAGE_TYPE = "image"

        const val PAYLOAD_SCROLL_IMAGE_VARIANT = 5
        const val PAYLOAD_MEDIA_UPDATE = 6
    }

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.LEFT

    fun isMediaContainsVideo(): Boolean = listOfMedia.any { it.type == VIDEO_TYPE }

    /**
     * Not Found will return 0
     */
    fun indexOfSelectedVariantOptionId(): Int {
        return listOfMedia.indexOfFirst {
            it.variantOptionId == variantOptionIdScrollAnchor
        }.takeIf { it > -1 } ?: 0
    }

    fun getScrollPosition(): Int {
        val optionIdAnchor = variantOptionIdScrollAnchor
        return if (optionIdAnchor.isNotEmpty() && shouldUpdateImage) {
            listOfMedia.indexOfFirst {
                it.variantOptionId == optionIdAnchor
            }.takeIf { it > -1 } ?: 0
        } else {
            initialScrollPosition
        }
    }

    override fun isLoading(): Boolean {
        return listOfMedia.isEmpty()
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMediaDataModel) {
            listOfMedia.hashCode() == newData.listOfMedia.hashCode() &&
                initialScrollPosition == newData.initialScrollPosition &&
                variantOptionIdScrollAnchor == newData.variantOptionIdScrollAnchor &&
                liveIndicator == newData.liveIndicator
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductMediaDataModel) {
            if (listOfMedia.hashCode() != newData.listOfMedia.hashCode()) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    PAYLOAD_MEDIA_UPDATE
                )
            } else if (newData.variantOptionIdScrollAnchor.isEmpty()) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    ProductDetailConstant.PAYLOAD_DO_NOTHING
                )
            } else if (variantOptionIdScrollAnchor != newData.variantOptionIdScrollAnchor) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    PAYLOAD_SCROLL_IMAGE_VARIANT
                )
            }

            return bundle.takeIf { !it.isEmpty }
        } else {
            null
        }
    }

}

data class MediaDataModel(
    val id: String = "",
    val type: String = "",
    val url300: String = "",
    val urlOriginal: String = "",
    val urlThumbnail: String = "",
    val mediaDescription: String = "",
    val videoUrl: String = "",
    val isAutoPlay: Boolean = false,
    val variantOptionId: String = "",
    val isPrefetch: Boolean = false,
    val isLive: Boolean = false
) {
    var prefetchResource: Drawable? = null
    fun isVideoType(): Boolean = type == ProductMediaDataModel.VIDEO_TYPE
}

sealed class MediaContainerType(val type: String, val ratio: String) {
    object Square : MediaContainerType(type = "square", "H,1:1")
    object Portrait : MediaContainerType(type = "portrait", "H,4:5")
}

data class ProductMediaRecomData(
    val lightIcon: String = "",
    val darkIcon: String = "",
    val iconText: String = ""
) {
    fun getIconUrl(context: Context): String {
        return if (context.isDarkMode()) darkIcon else lightIcon
    }

    fun shouldShow(): Boolean {
        return (darkIcon.isNotBlank() || lightIcon.isNotBlank()) && iconText.isNotBlank()
    }
}

internal fun String?.asMediaContainerType(): MediaContainerType = when (this) {
    MediaContainerType.Portrait.type -> MediaContainerType.Portrait
    else -> MediaContainerType.Square
}
