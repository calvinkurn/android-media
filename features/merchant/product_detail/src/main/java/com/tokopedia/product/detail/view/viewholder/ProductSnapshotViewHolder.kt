package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.fragment.partialview.PartialSnapshotView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_pdp_snapshot.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(private val view: View,
                                private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSnapshotDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    private var header: PartialSnapshotView? = null

    init {
        header = PartialSnapshotView(view, listener)
        measureScreenHeight()
    }

    override fun bind(element: ProductSnapshotDataModel) {
        with(view) {
            view_picture_search_bar.shouldRenderViewPager = element.shouldRefreshViewPager
            initializeClickListener(element)
            element.dynamicProductInfoP1?.let {
                view.addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
                header?.renderData(it, element.nearestWarehouseDataModel?.nearestWarehouseStockWording
                        ?: "")
                header?.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
                view_picture_search_bar.renderShopStatusDynamicPdp(element.shopStatus, element.statusTitle, element.statusMessage,
                        it.basic.status)
            }
            header?.updateWishlist(element.isWishlisted)
            header?.renderTradein(element.showTradeIn())
            header?.renderCod(element.showCod())

            element.media?.let {
                view_picture_search_bar.renderData(it, listener::onImageClicked, listener::onSwipePicture, listener.getProductFragmentManager(),
                        getComponentTrackData(element), listener::onImageClickedTrack, listener.getLifecycleFragment())
                if (element.shouldRenderImageVariant) {
                    view.view_picture_search_bar.updateImage(element.media)
                }
                element.shouldRefreshViewPager = false
                element.shouldRenderImageVariant = false
            }
        }
    }

    override fun bind(element: ProductSnapshotDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> header?.updateWishlist(element.isWishlisted)
            ProductDetailConstant.PAYLOAD_P3 -> header?.renderCod(element.showCod())
            ProductDetailConstant.PAYLOAD_UPDATE_IMAGE -> view.view_picture_search_bar.updateImage(element.media)
        }
    }

    private fun initializeClickListener(element: ProductSnapshotDataModel?) = with(view) {
        tradein_header_container.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }
        fab_detail_pdp.setOnClickListener {
            listener.onFabWishlistClicked(it.isActivated, getComponentTrackData(element))
        }
    }

    private fun measureScreenHeight() = with(view) {
        val screenWidth = view.resources.displayMetrics.widthPixels
        view_picture_search_bar.layoutParams.height = screenWidth
    }

    private fun getComponentTrackData(element: ProductSnapshotDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}