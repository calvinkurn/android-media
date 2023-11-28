package com.tokopedia.product.detail.view.fragment.delegate

import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.product.detail.view.componentization.ComponentCallback
import com.tokopedia.product.detail.view.componentization.ComponentEvent
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator

/**
 * Created by yovi.putra on 14/11/23"
 * Project name: android-tokopedia-core
 **/

abstract class BaseComponentCallback<Event : ComponentEvent>(
    private val mediator: PdpComponentCallbackMediator
) : ComponentCallback<ComponentEvent> {

    protected val context
        get() = mediator.rootView.context

    protected val viewModel
        get() = mediator.pdpViewModel

    protected val queueTracker
        get() = mediator.queueTracker

    @Suppress("UNCHECKED_CAST")
    override fun event(event: ComponentEvent) {
        when (event) {
            is BasicComponentEvent.OnImpressed -> {
                onImpressComponent(trackData = event.trackData)
            }

            is BasicComponentEvent.GoToAppLink -> {
                RouteManager.route(context, event.applink)
            }

            else -> {
                val mEvent = event as? Event

                if (mEvent != null) {
                    onEvent(event = mEvent)
                }
            }
        }
    }

    protected abstract fun onEvent(event: Event)

    private fun onImpressComponent(trackData: ComponentTrackDataModel) {
        if (viewModel.getDynamicProductInfoP1?.cacheState?.isPrefetch == true) return

        val purchaseProtectionUrl = when (trackData.componentName) {
            ProductDetailConstant.PRODUCT_PROTECTION -> getPurchaseProtectionUrl()
            else -> ""
        }
        val promoId = when (trackData.componentName) {
            ProductDetailConstant.SHIPMENT_V2 -> getShipmentPlusText()
            else -> ""
        }

        DynamicProductDetailTracking.Impression
            .eventImpressionComponent(
                trackingQueue = queueTracker,
                componentTrackDataModel = trackData,
                productInfo = viewModel.getDynamicProductInfoP1,
                componentName = "",
                purchaseProtectionUrl = purchaseProtectionUrl,
                userId = viewModel.userId,
                lcaWarehouseId = getLcaWarehouseId(),
                promoId = promoId
            )
    }

    private fun getLcaWarehouseId(): String {
        return viewModel.getUserLocationCache().warehouse_id
    }

    private fun getPurchaseProtectionUrl(): String {
        return viewModel.p2Data.value?.productPurchaseProtectionInfo?.ppItemDetailPage?.linkURL.orEmpty()
    }

    private fun getShipmentPlusText(): String {
        return viewModel.getP2ShipmentPlusByProductId()?.text.orEmpty()
    }

    protected fun ComponentTrackDataModel.asCommonTracker(): CommonTracker? {
        val productInfo = viewModel.getDynamicProductInfoP1 ?: return null
        return CommonTracker(
            productInfo = productInfo,
            userId = viewModel.userId,
            componentTracker = this
        )
    }
}
