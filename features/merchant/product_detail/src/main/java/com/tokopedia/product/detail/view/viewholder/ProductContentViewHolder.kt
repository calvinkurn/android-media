package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.fragment.partialview.PartialSnapshotView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

/**
 * Created by Yehezkiel on 06/05/20
 */
class ProductContentViewHolder(private val view: View,
                               private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductContentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_content
    }

    private var header: PartialSnapshotView? = null

    init {
        header = PartialSnapshotView(view, listener)
    }

    override fun bind(element: ProductContentDataModel) {
        initializeClickListener(element)

        element.data?.let {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            header?.renderData(it, element.nearestWarehouseDataModel?.nearestWarehouseStockWording ?: "")
            header?.showOfficialStore(it.data.isPowerMerchant, it.data.isOS)
        }

        header?.updateWishlist(element.isWishlisted)
        header?.renderTradein(element.showTradeIn())
        header?.renderCod(element.showCod())
    }

    override fun bind(element: ProductContentDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> header?.updateWishlist(element.isWishlisted)
            ProductDetailConstant.PAYLOAD_P3 -> header?.renderCod(element.showCod())
        }
    }

    private fun initializeClickListener(element: ProductContentDataModel?) = with(view) {
        tradein_header_container.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }
        fab_detail_pdp.setOnClickListener {
            listener.onFabWishlistClicked(it.isActivated, getComponentTrackData(element))
        }
    }

    private fun getComponentTrackData(element: ProductContentDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}