package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.BestSellerInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_best_seller_info.view.*

class BestSellerInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<BestSellerInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_best_seller_info
    }

    override fun bind(element: BestSellerInfoDataModel) {
        if (element.bestSellerInfoContent != null) {
            view.visible()
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }
            view.best_seller_info_content.text = element.bestSellerInfoContent?.content
            view.setOnClickListener {
                RouteManager.route(view.context, element.bestSellerInfoContent?.applink)
            }
        } else {
            view.invisible()
        }
    }

    private fun getComponentTrackData(element: BestSellerInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}