package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
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
    private lateinit var header: PartialSnapshotView

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    override fun bind(element: ProductSnapshotDataModel) {
        view.view_picture_search_bar.layoutParams.height = element.screenHeight

        if (!::header.isInitialized) {
            header = PartialSnapshotView(view, listener)
        }

        element.dynamicProductInfoP1?.let {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))

            }

            header.renderData(it)
            header.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
            element.nearestWarehouse?.let { nearestWarehouse ->
                if (nearestWarehouse.warehouseInfo.id.isNotBlank())
                    header.updateStockAndPriceWarehouse(nearestWarehouse, it.data.campaign)
            }
            view.view_picture_search_bar.renderShopStatusDynamicPdp(element.shopStatus, element.statusTitle, element.statusMessage,
                    it.basic.status)
        }

        renderWishlist(element.isAllowManage, element.isWishlisted)

        renderCod(element.shouldShowCod)
        renderTradein(element.shouldShowTradein)

        view.tv_trade_in_promo.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }

        view.fab_detail.setOnClickListener {
            listener.onEditProductClick(element, element.dynamicProductInfoP1, getComponentTrackData(element))
            listener.onFabWishlistClicked(it.isActivated, getComponentTrackData(element))
        }
        element.media?.let {
            view.view_picture_search_bar.renderData(it, listener::onImageClicked, listener::onSwipePicture, listener.getProductFragmentManager(),
                    element.shouldReinitVideoPicture, getComponentTrackData(element), listener::onImageClickedTrack)
            element.shouldReinitVideoPicture = false
        }

        view.view_picture_search_bar.isVisibleOnTheScreen({
            view.fab_detail.show()
        }, {
            view.fab_detail.hide()
            view.view_picture_search_bar.stopVideo()
        })
    }

    override fun bind(element: ProductSnapshotDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> renderWishlist(element.isAllowManage, element.isWishlisted)
            ProductDetailConstant.PAYLOAD_COD -> {
                view.label_cod.visibility = if (element.shouldShowCod) View.VISIBLE else View.GONE
                renderCod(element.shouldShowCod)
            }
            ProductDetailConstant.PAYLOAD_TRADEIN -> renderTradein(element.shouldShowTradein)
            ProductDetailConstant.PAYLOAD_CONFIGURATION_CHANGED -> changeImageHeight(element.screenHeight)
        }
    }

    private fun renderCod(shouldShowCod: Boolean) {
        if (::header.isInitialized) {
            header.renderCod(shouldShowCod)
        }
    }

    private fun changeImageHeight(imageHeight: Int) {
        if (imageHeight != 0) {
            view.view_picture_search_bar.layoutParams.height = imageHeight
        }
    }

    private fun renderTradein(shouldShowTradein: Boolean) {
        if (::header.isInitialized) {
            view.tv_trade_in_promo.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.tradein_white), null, null, null)
            header.renderTradein(shouldShowTradein)
        }
    }

    private fun renderWishlist(isAllowManage: Int, wishlisted: Boolean) {
        view.context?.let {
            view.fab_detail.show()
            if (isAllowManage == 1) {
                view.fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) {
        view.context?.let {
            if (wishlisted) {
                view.fab_detail.hide()
                view.fab_detail.isActivated = true
                view.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_selected_pdp))
                view.fab_detail.show()
            } else {
                view.fab_detail.hide()
                view.fab_detail.isActivated = false
                view.fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_unselected_pdp))
                view.fab_detail.show()
            }
        }
    }

    private fun getComponentTrackData(element: ProductSnapshotDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}