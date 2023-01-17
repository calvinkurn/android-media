package com.tokopedia.tokofood.feature.merchant.presentation.mvc

import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl

/**
 * These class is created because we will need to implement custom trackers for Tokofood related trackers.
 * However, we do not need some of the default trackers to be hit, so we create a class that implement methods with no operations.
 */
class TokofoodMerchantMvcTrackerImpl: MvcTrackerImpl {

    override fun userClickEntryPoints(
        shopId: String,
        userId: String?,
        source: Int,
        isTokomember: Boolean,
        productId: String
    ) {
        // no-op
    }

    override fun userClickEntryPointOnMVCLockToProduct(
        shopId: String,
        userId: String?,
        source: Int,
        productId: String
    ) {
        // no-op
    }

    override fun viewMVCLockToProduct(
        shopId: String,
        userId: String?,
        source: Int,
        productId: String
    ) {
        // no-op
    }

    override fun clickFollowButton(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int,
        buttonTitle: String?
    ) {
        // no-op
    }

    override fun viewFollowButtonToast(
        shopId: String,
        userId: String?,
        source: Int,
        isSuccess: Boolean
    ) {
        // no-op
    }

    override fun viewCoupons(widgetType: String, shopId: String, userId: String?, source: Int) {
        // no-op
    }

    override fun viewWidgetImpression(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int
    ) {
        // no-op
    }

    override fun clickJadiMemberButton(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int,
        buttonTitle: String?
    ) {
        // no-op
    }

    override fun viewJadiMemberToast(
        shopId: String,
        userId: String?,
        source: Int,
        isSuccess: Boolean
    ) {
        // no-op
    }

    override fun clickCekInfoButton(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int,
        buttonTitle: String?
    ) {
        // no-op
    }

    override fun clickCekInfoButtonClose(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int,
        buttonTitle: String?
    ) {
        // no-op
    }

    override fun viewTokomemberBottomSheet(shopId: String, userId: String?, source: Int) {
        // no-op
    }

    override fun clickDaftarJadiMember(shopId: String, userId: String?, source: Int) {
        // no-op
    }

    override fun closeMainBottomSheet(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int
    ) {
        // no-op
    }

    override fun clickLihatExpand(shopId: String, userId: String?, source: Int) {
        // no-op
    }

    override fun clickMulaiBelanjaButton(
        widgetType: String,
        shopId: String,
        userId: String?,
        source: Int,
        buttonTitle: String?
    ) {
        // no-op
    }

    override fun userClickBottomSheetCTA(widgetType: String, label: String, userId: String) {
        // no-op
    }

    override fun tokomemberImpressionOnPdp(shopId: String, userId: String?, isTokomember: Boolean) {
        // no-op
    }

    override fun viewMVCCoupon(label: String, mapData: HashMap<String, Any>, source: Int) {
        // no-op
    }

    override fun mvcMultiShopCardClick(
        shopName: String,
        eventAction: String,
        source: Int,
        userId: String?,
        productPosition: Int,
        label: String
    ) {
        // no-op
    }
}
