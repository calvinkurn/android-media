package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.track.constant.TrackerConstant

/**
 * Created by yovi.putra on 25/01/23"
 * Project name: android-tokopedia-core
 **/
object ProductSocialProofTracking {

    fun onTalkClicked(
        productInfo: ProductInfoP1,
        trackDataModel: ComponentTrackDataModel?,
        deepLink: String
    ) {
        ProductDetailTracking.Iris.eventDiscussionClickedIris(
            productInfo = productInfo,
            deeplinkUrl = deepLink,
            shopName = productInfo.basic.shopName,
            componentTrackDataModel = trackDataModel ?: ComponentTrackDataModel()
        )
    }

    fun onMediaClicked(
        userId: String,
        productInfo: ProductInfoP1,
        trackDataModel: ComponentTrackDataModel?
    ) {
        ProductDetailTracking.Click.eventClickBuyerPhotosClicked(
            productInfo = productInfo,
            userId = userId,
            componentTrackDataModel = trackDataModel ?: ComponentTrackDataModel()
        )
    }

    fun onRatingClicked(
        deepLink: String,
        productInfo: ProductInfoP1
    ) {
        ProductDetailTracking.Iris.eventReviewClickedIris(
            productInfo = productInfo,
            deeplinkUrl = deepLink,
            shopName = productInfo.basic.shopName
        )
    }

    fun onNewProductClicked(
        productInfo: ProductInfoP1?,
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
        productInfo: ProductInfoP1?,
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
        productInfo: ProductInfoP1?
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
        productInfo: ProductInfoP1?
    ) {
        val action = "view - product terbaru - social proof"
        val trackerId = "41544"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = null,
            action = action,
            trackerId = trackerId,
            modifier = {
                it[TrackerConstant.EVENT] = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS
            }
        )
    }

    private fun oShopRatingImpression(
        productInfo: ProductInfoP1?
    ) {
        val action = "view - rating toko"
        val trackerId = "41547"

        TrackingUtil.addClickEvent(
            productInfo = productInfo,
            trackDataModel = null,
            action = action,
            trackerId = trackerId,
            modifier = {
                it[TrackerConstant.EVENT] = ProductTrackingConstant.PDP.EVENT_VIEW_PG_IRIS
            }
        )
    }
}
