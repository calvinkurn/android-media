package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailTracking
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant

/**
 * Created by yovi.putra on 25/01/23"
 * Project name: android-tokopedia-core
 **/
object ProductSocialProofTracking {

    fun onTalkClicked(
        productInfo: DynamicProductInfoP1,
        trackDataModel: ComponentTrackDataModel?,
        deepLink: String
    ) {
        DynamicProductDetailTracking.Iris.eventDiscussionClickedIris(
            productInfo = productInfo,
            deeplinkUrl = deepLink,
            shopName = productInfo.basic.shopName,
            componentTrackDataModel = trackDataModel ?: ComponentTrackDataModel()
        )
        DynamicProductDetailTracking.Moengage.sendMoEngageClickDiskusi(productInfo = productInfo)
    }

    fun onMediaClicked(
        userId: String,
        productInfo: DynamicProductInfoP1,
        trackDataModel: ComponentTrackDataModel?
    ) {
        DynamicProductDetailTracking.Click.eventClickBuyerPhotosClicked(
            productInfo = productInfo,
            userId = userId,
            componentTrackDataModel = trackDataModel ?: ComponentTrackDataModel()
        )
    }

    fun onRatingClicked(
        deepLink: String,
        productInfo: DynamicProductInfoP1
    ) {
        DynamicProductDetailTracking.Iris.eventReviewClickedIris(
            productInfo = productInfo,
            deeplinkUrl = deepLink,
            shopName = productInfo.basic.shopName
        )
        DynamicProductDetailTracking.Moengage.sendMoEngageClickReview(productInfo)
    }

    fun onNewProductClicked(
        productInfo: DynamicProductInfoP1?,
        trackDataModel: ComponentTrackDataModel?
    ) {
        val action = "click - product terbaru"
        val trackerId = "41545"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = trackDataModel,
            action = action,
            trackerId = trackerId
        )
    }

    fun onShopRatingClicked(
        productInfo: DynamicProductInfoP1?,
        trackDataModel: ComponentTrackDataModel?
    ) {
        val action = "click - rating toko"
        val trackerId = "41548"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = trackDataModel,
            action = action,
            trackerId = trackerId
        )
    }

    fun onImpression(
        uiModel: SocialProofUiModel,
        productInfo: DynamicProductInfoP1?
    ) {
        when (uiModel.identifier) {
            SocialProofUiModel.Identifier.NewProduct -> onNewProductImpression(productInfo)
            SocialProofUiModel.Identifier.ShopRating -> oShopRatingImpression(productInfo)
            else -> {
                // no-ops
            }
        }
    }

    private fun onNewProductImpression(
        productInfo: DynamicProductInfoP1?
    ) {
        val action = "view - product terbaru - social proof"
        val trackerId = "41544"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = null,
            action = action,
            trackerId = trackerId,
            modify = {
                it[TrackerConstant.EVENT] = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS
            }
        )
    }

    private fun oShopRatingImpression(
        productInfo: DynamicProductInfoP1?
    ) {
        val action = "view - rating toko"
        val trackerId = "41547"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = null,
            action = action,
            trackerId = trackerId,
            modify = {
                it[TrackerConstant.EVENT] = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS
            }
        )
    }
}
