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
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

class ProductSnapshotViewHolder(private val view: View,
                                private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSnapshotDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_snapshot
    }

    private var header: PartialSnapshotView? = null

    init {
        header = PartialSnapshotView(view, listener)
    }

    override fun bind(element: ProductSnapshotDataModel) {
        with(view) {
            initializeClickListener(element)
            element.data?.let {
                addOnImpressionListener(element.impressHolder) {
                    listener.onImpressComponent(getComponentTrackData(element))
                }
                header?.renderData(it, element.nearestWarehouseDataModel?.nearestWarehouseStockWording
                        ?: "")
                header?.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
            }
            header?.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
            header?.renderTradein(element.showTradeIn())
            header?.renderCod(element.showCod())
        }
    }

    override fun bind(element: ProductSnapshotDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> header?.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
            ProductDetailConstant.PAYLOAD_P3 -> header?.renderCod(element.showCod())
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

    private fun getComponentTrackData(element: ProductSnapshotDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}