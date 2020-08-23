package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopInfoDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialDynamicShopInfoView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_pdp_shop_info.view.*
import kotlinx.android.synthetic.main.partial_product_shop_info.view.base_shop_view
import kotlinx.android.synthetic.main.shimmering_shop_info.view.pdp_shimmering_shop_info

class ProductShopInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductShopInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_shop_info
    }

    private val shopInfoView = PartialDynamicShopInfoView(view.base_shop_view, listener)

    override fun bind(element: ProductShopInfoDataModel) {

        if (element.shouldRenderShopInfo) {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            hideLoading()
            shopInfoView.renderShop(
                    element.shopName,
                    element.shopLocation,
                    element.shopLastActive,
                    element.shopAvatar,
                    element.isAllowManage,
                    element.isOs,
                    element.isPm,
                    listener.isOwner(),
                    element.isFavorite,
                    ComponentTrackDataModel(element.type, element.name, adapterPosition + 1))
        } else {
            showLoading()
        }

        shopInfoView.renderShopBadge(element.shopBadge ?: "")
        shopInfoView.renderShopFeature(element.isGoAPotik)
    }

    override fun bind(element: ProductShopInfoDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }
        when (payloads[0] as Int) {
            2 -> shopInfoView.toggleClickableFavoriteBtn(element.enableButtonFavorite)
            else -> {
                shopInfoView.updateFavorite(element.isFavorite)
                shopInfoView.toggleClickableFavoriteBtn(element.enableButtonFavorite)
            }
        }
    }

    private fun showLoading() {
        view.pdp_shimmering_shop_info.show()
        view.pdp_shop_info_container.hide()
    }

    private fun hideLoading() {
        view.pdp_shimmering_shop_info.hide()
        view.pdp_shop_info_container.show()
    }

    private fun getComponentTrackData(element: ProductShopInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}