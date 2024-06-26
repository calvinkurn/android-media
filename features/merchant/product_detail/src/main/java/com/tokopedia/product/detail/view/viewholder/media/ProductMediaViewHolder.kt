package com.tokopedia.product.detail.view.viewholder.media

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicProductMediaBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder

/**
 * Created by Yehezkiel on 04/05/20
 */
class ProductMediaViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
) : ProductDetailPageViewHolder<ProductMediaDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_dynamic_product_media
    }

    private val binding = ItemDynamicProductMediaBinding.bind(view)

    val mediaPosition
        get() = binding.viewMediaPager.lastPositionIsSku

    override fun bind(element: ProductMediaDataModel) {
        setupViewpager(element)

        element.shouldAnimateLabel = false
        element.shouldUpdateImage = false

        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    override fun bind(element: ProductMediaDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        payloads.forEach { processPayload(it as? Int, element) }
        view.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun processPayload(payload: Int?, element: ProductMediaDataModel) = when (payload) {
        ProductMediaDataModel.PAYLOAD_SCROLL_IMAGE_VARIANT -> {
            binding.viewMediaPager.scrollToPosition(
                element.indexOfSelectedVariantOptionId(),
                true
            )
        }

        ProductMediaDataModel.PAYLOAD_MEDIA_UPDATE -> {
            setupViewpager(element, true)
        }

        else -> {
            //NO-OP
        }
    }

    private fun setupViewpager(element: ProductMediaDataModel, resetPosition: Boolean = false) {
        val scrollPosition = if (resetPosition) 0 else element.getScrollPosition()

        binding.viewMediaPager.setup(
            media = element.listOfMedia,
            listener = listener,
            componentTrackDataModel = getComponentTrackData(element),
            initialScrollPosition = scrollPosition,
            containerType = element.containerType,
            recommendation = element.recommendation,
            liveIndicator = element.liveIndicator,
            isPrefetch = element.isPrefetch
        )

        if (resetPosition) {
            binding.viewMediaPager.scrollToPosition(0)
        }
    }

    fun detachView() {
        listener.getProductVideoCoordinator()?.onPause()
    }
}
