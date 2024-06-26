package com.tokopedia.product.detail.view.viewholder.promo_price.event

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent

sealed interface ProductPriceEvent : ComponentEvent {

    data class OnPromoPriceClicked(
        val url: String,
        val subtitle: String,
        val defaultPriceFmt: String,
        val slashPriceFmt: String,
        val promoPriceFmt: String,
        val promoId: List<String>,
        val bottomSheetParams: String,
        val trackerData: ComponentTrackDataModel
    ) : ProductPriceEvent
}
