package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemDynamicProductMediaBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 04/05/20
 */
class ProductMediaViewHolder(private val view: View,
                             private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMediaDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_dynamic_product_media
    }
    private val binding = ItemDynamicProductMediaBinding.bind(view)

    override fun bind(element: ProductMediaDataModel) {

        setupViewpager(element)

        element.shouldAnimateLabel = false
        element.shouldUpdateImage = false

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    override fun bind(element: ProductMediaDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        payloads.forEach { processPayload(it as? Int, element) }
    }

    private fun processPayload(payload: Int?, element: ProductMediaDataModel) = when (payload) {
        ProductDetailConstant.PAYLOAD_SCROLL_IMAGE_VARIANT -> {
            binding.viewMediaPager.scrollToPosition(
                element.indexOfSelectedVariantOptionId(),
                true
            )
        }
        ProductDetailConstant.PAYLOAD_MEDIA_UPDATE -> {
            setupViewpager(element, true)
        }
        else -> {}
    }

    private fun setupViewpager(element: ProductMediaDataModel, resetPosition: Boolean = false) {
        val scrollPosition = if (resetPosition) 0 else element.getScrollPosition()

        binding.viewMediaPager.setup(
            media = element.listOfMedia,
            listener = listener,
            componentTrackDataModel = getComponentTrackData(element),
            initialScrollPosition = scrollPosition,
            containerType = element.containerType
        )
    }

    fun detachView() {
        listener.getProductVideoCoordinator()?.onPause()
    }

    private fun getComponentTrackData(element: ProductMediaDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}
