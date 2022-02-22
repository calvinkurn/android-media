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

    private val binding = ItemDynamicProductMediaBinding.bind(view).also { measureScreenHeight(it) }

    override fun bind(element: ProductMediaDataModel) {
        with(binding) {
            viewMediaPager.setup(element.listOfMedia,
                    listener,
                    element.initialScrollPosition,
                    getComponentTrackData(element))
            element.initialScrollPosition = -1

            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
        }
    }

    override fun bind(element: ProductMediaDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_SCROLL_IMAGE_VARIANT -> {
                binding.viewMediaPager.scrollToPosition(element.initialScrollPosition)
            }
        }
    }

    fun detachView() {
        listener.getProductVideoCoordinator()?.onPause()
    }

    private fun measureScreenHeight(binding: ItemDynamicProductMediaBinding) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        binding.viewMediaPager.layoutParams.height = screenWidth
    }

    private fun getComponentTrackData(element: ProductMediaDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}