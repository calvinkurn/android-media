package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 25/01/23"
 * Project name: android-tokopedia-core
 **/
object ProductThumbnailVariantTracking {

    fun onItemClicked(
        trackingQueue: TrackingQueue?,
        productInfo: DynamicProductInfoP1?,
        singleVariant: ProductSingleVariantDataModel?,
        variantId: String,
        variantKey: String,
        componentPosition: Int,
        userId: String
    ) {
        val action = "click - product media variant thumbnail with image"
        val itemName = "$variantKey - $variantId"
        val trackerId = "40922"
        val mapEvent = TrackingUtil.createCommonImpressionTracker(
            productInfo = productInfo,
            componentTrackDataModel = ComponentTrackDataModel(
                adapterPosition = componentPosition,
                componentType = singleVariant?.type().orEmpty(),
                componentName = singleVariant?.name().orEmpty()
            ),
            userId = userId,
            customAction = action,
            customItemName = itemName,
            customItemId = "",
            lcaWarehouseId = ""
        )?.apply {
            put(TrackingConstant.Hit.TRACKER_ID, trackerId)
            put(TrackerConstant.SHOP_ID, productInfo?.basic?.shopID.orEmpty())
        }

        trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>)
    }

    fun onImpression(
        trackingQueue: TrackingQueue?,
        componentTrackDataModel: ComponentTrackDataModel,
        productInfo: DynamicProductInfoP1?,
        userId: String
    ) {
        val action = "impression - product media variant thumbnail with image"
        val itemName = ""
        val trackerId = "40921"
        val mapEvent = TrackingUtil.createCommonImpressionTracker(
            productInfo = productInfo,
            componentTrackDataModel = componentTrackDataModel,
            userId = userId,
            customAction = action,
            customItemName = itemName,
            customItemId = "",
            lcaWarehouseId = ""
        )?.apply {
            put(TrackingConstant.Hit.TRACKER_ID, trackerId)
            put(TrackerConstant.SHOP_ID, productInfo?.basic?.shopID.orEmpty())
        }

        trackingQueue?.putEETracking(mapEvent as HashMap<String, Any>)
    }
}
