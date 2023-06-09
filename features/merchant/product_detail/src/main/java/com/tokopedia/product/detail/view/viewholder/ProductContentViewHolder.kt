package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductContentDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.ItemDynamicProductContentBinding
import com.tokopedia.product.detail.databinding.ItemProductContentBinding
import com.tokopedia.product.detail.view.fragment.partialview.PartialContentView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 06/05/20
 */
class ProductContentViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductContentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_content
    }

    private val binding = ItemDynamicProductContentBinding.bind(view)
    private val header = PartialContentView(view, listener)

    override fun bind(element: ProductContentDataModel) {
        initializeClickListener(element)

        element.data?.let {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            header.renderData(it, element.isNpl(), element.freeOngkirImgUrl, element.shouldShowCampaign)
        }

        header.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
        header.renderTradein(element.showTradeIn())
    }

    override fun bind(element: ProductContentDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            ProductDetailConstant.PAYLOAD_WISHLIST -> header.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
            ProductDetailConstant.PAYLOAD_TRADEIN_AND_BOE -> {
                header.renderTradein(element.showTradeIn())

                header.updateWishlist(element.isWishlisted, listener.shouldShowWishlist())
                // only triggered when get data from p2, will update with boe/bo imageurl from Restriction Engine p2
                header.renderFreeOngkir(element.freeOngkirImgUrl)
            }
        }
        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun initializeClickListener(element: ProductContentDataModel?) = with(binding) {
        val itemProductContent = ItemProductContentBinding.bind(binding.root)
        itemProductContent.tradeinHeaderContainer.setOnClickListener {
            listener.txtTradeinClicked(getComponentTrackData(element))
        }

        itemProductContent.fabDetailPdp.apply {
            setOnClickListener {
                listener.onFabWishlistClicked(activeState, getComponentTrackData(element))
            }
        }
    }

    private fun getComponentTrackData(element: ProductContentDataModel?) = ComponentTrackDataModel(
        element?.type
            ?: "",
        element?.name ?: "",
        adapterPosition + 1
    )
}
