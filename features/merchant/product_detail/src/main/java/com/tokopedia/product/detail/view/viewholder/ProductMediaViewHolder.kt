package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_product_media.view.*

/**
 * Created by Yehezkiel on 04/05/20
 */
class ProductMediaViewHolder(private val view: View,
                             private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMediaDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_dynamic_product_media
    }

    init {
        measureScreenHeight()
    }

    override fun bind(element: ProductMediaDataModel) {
        with(view) {
            viewMediaPager.shouldRenderViewPager = element.shouldRefreshViewPagger
            viewMediaPager.renderData(element.listOfMedia, listener::onImageClicked, listener::onSwipePicture, listener.getProductFragmentManager(), getComponentTrackData(element),
                    listener::onImageClickedTrack, listener.getLifecycleFragment())

            viewMediaPager.renderShopStatusDynamicPdp(element.shopStatus, element.statusTitle, element.statusMessage,
                    element.basicStatus)
            element.shouldRefreshViewPagger = false

            viewMediaPager?.showImageReview(element.shouldShowImageReview) {
                listener.onImageReviewMediaClicked(getComponentTrackData(element))
            }

            if (element.shouldRenderImageVariant) {
                viewMediaPager.updateImage(element.listOfMedia)
                element.shouldRenderImageVariant = false
            }

            viewMediaPager?.isVisibleOnTheScreen({},{
                viewMediaPager?.stopVideo()
            })
        }
    }

    override fun bind(element: ProductMediaDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_UPDATE_IMAGE -> {
                view.viewMediaPager.updateImage(element.listOfMedia)
            }
        }
    }

    private fun measureScreenHeight() = with(view) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        viewMediaPager.layoutParams.height = screenWidth
    }

    private fun getComponentTrackData(element: ProductMediaDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}