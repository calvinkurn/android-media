package com.tokopedia.product.manage.common.feature.list.analytics

import com.tokopedia.product.manage.common.feature.list.constant.CLICK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.BUSINESS_UNIT_BROADCAST_CHAT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.BUSINESS_UNIT_BROADCAST_CHAT_SA
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.CURRENT_SITE_BROADCAST_CHAT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.CURRENT_SITE_BROADCAST_CHAT_SA
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_ACTION_CLICK_BROADCAST_CHAT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_ACTION_CLICK_ON_CAROUSEL
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_CATEGORY
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_CATEGORY_PRODUCT_LIST_PAGE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_CATEGORY_PRODUCT_MANAGE_PAGE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageDataLayer.EVENT_LABEL_BROADCAST_CHAT
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.track.TrackApp

object ProductManageTracking {

    private fun eventProductManage(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME,
                EVENT_CATEGORY,
                action,
                label
            ).dataTracking
        )
    }

    private fun eventEditProduct(action: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME_EDIT_PRODUCT,
                ProductManageDataLayer.EVENT_CATEGORY_EDIT_PRODUCT,
                action,
                "",
                shopId
            ).dataTracking
        )
    }

    private infix fun String.withAllocationType(isVariant: Boolean): String {
        val allocationType =
            if (isVariant) {
                ProductManageDataLayer.ALLOCATION_VARIANT_PRODUCT
            } else {
                ProductManageDataLayer.ALLOCATION_SINGLE_PRODUCT
            }
        return this.plus(allocationType)
    }

    private fun eventClickBroadcastChatCustom(
        action: String,
        category: String,
        label: String,
        userId: String,
        currentSite: String,
        businessUnit: String,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME,
                category,
                action,
                label
            ).dataTracking.customDimension(userId, currentSite, businessUnit)
        )
    }

    fun eventClickNotifyMeIcon(
        productId: String, parentId: String = "0"
    ) {
        val label = arrayOf(productId, parentId).joinToString(" - ")
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME_CLICK_PG,
                EVENT_CATEGORY_PRODUCT_LIST_PAGE,
                ProductManageDataLayer.EVENT_ACTION_CLICK_OOS_NOTIFY_ME,
                label
            ).dataTracking.customDimension("36711")
        )
    }

    fun eventClickAturStockNotifyMe(productId: String, parentId: String = "0") {
        val label = arrayOf(productId, parentId).joinToString(" - ")
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME_CLICK_PG,
                EVENT_CATEGORY_PRODUCT_LIST_PAGE,
                ProductManageDataLayer.EVENT_ACTION_CLICK_ATUR_STOCK_OOS_NOTIFY_ME,
                label
            ).dataTracking.customDimension("36712")
        )
    }

    fun eventClickFilterNotifyMe() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            EventTracking(
                ProductManageDataLayer.EVENT_NAME_CLICK_PG,
                EVENT_CATEGORY_PRODUCT_LIST_PAGE,
                ProductManageDataLayer.EVENT_ACTION_CLICK_FILTER_NOTIFY_ME,
                ""
            ).dataTracking.customDimension("36718")
        )
    }

    fun eventDraftClick(label: String) {
        eventProductManage(CLICK, label)
    }

    fun eventAddProduct() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ADD, "")
    }

    fun eventMultipleSelect() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_MULTIPLE, "")
    }

    fun eventEditPrice(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_EDIT_PRICE, label)
    }

    fun eventEditStock(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_EDIT_STOCK, label)
    }

    fun eventInventory(tabName: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_INVENTORY} - $tabName"
        eventProductManage(action, "")
    }

    fun eventOnProduct(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_PRODUCT, label)
    }

    fun eventSettingsPreview(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_PREVIEW, label)
    }

    fun eventSettingsDuplicate(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_DUPLICATE, label)
    }

    fun eventSettingsReminder(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_REMINDER, label)
    }

    fun eventSettingsDelete(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_DELETE, label)
    }

    fun eventSettingsTopads(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_TOPADS, label)
    }

    fun eventSettingsTopadsDetail(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_TOPADS_DETAIL, label)
    }

    fun eventSettingsFeatured(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_FEATURED, label)
    }

    fun eventClickBroadcastChat(userId: String = "", productId: String = "", isCarousel: Boolean) {
        if (isCarousel) {
            eventClickBroadcastChatCustom(
                action = EVENT_ACTION_CLICK_ON_CAROUSEL,
                category = EVENT_CATEGORY_PRODUCT_MANAGE_PAGE,
                label = EVENT_LABEL_BROADCAST_CHAT,
                userId = userId,
                currentSite = CURRENT_SITE_BROADCAST_CHAT,
                businessUnit = BUSINESS_UNIT_BROADCAST_CHAT
            )
        } else {
            eventClickBroadcastChatCustom(
                action = EVENT_ACTION_CLICK_BROADCAST_CHAT,
                category = EVENT_CATEGORY,
                label = productId,
                userId = userId,
                currentSite = CURRENT_SITE_BROADCAST_CHAT_SA,
                businessUnit = BUSINESS_UNIT_BROADCAST_CHAT_SA
            )
        }
    }

    fun eventToggleReminder(state: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_TOGGLE_REMINDER} - $state"
        eventProductManage(action, "")
    }

    fun eventToggleReminderSave(state: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_TOGGLE_REMINDER_SAVE} $state"
        eventProductManage(action, "")
    }

    fun eventFeaturedProductPopUpSave() {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_SAVE,
            ""
        )
    }

    fun eventFeaturedProductPopUpMore() {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_MORE,
            ""
        )
    }

    fun eventDeleteProduct(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_DELETE_PRODUCT, label)
    }

    fun eventSortingFilter() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_SORTING_FILTER, "")
    }

    fun eventEtalaseFilter(etalaseName: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_ETALASE_FILTER} - $etalaseName"
        eventProductManage(action, "")
    }

    fun eventCategoryFilter() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_CATEGORY_FILTER, "")
    }

    fun eventOthersFilter() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_OTHERS_FILTER, "")
    }

    fun eventFilter() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_FILTER, "")
    }

    fun eventMoreSorting(sortName: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_MORE_SORTING} - $sortName"
        eventProductManage(action, "")
    }

    fun eventMoreOthersFilter(filterName: String, state: String) {
        val action =
            "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER} - $filterName - $state"
        eventProductManage(action, "")
    }

    fun eventMoreOthersFilterSave() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER_SAVE, "")
    }

    fun eventBulkSettings() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS, "")
    }

    fun eventBulkSettingsMoveEtalase() {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_MOVE_ETALASE,
            ""
        )
    }

    fun eventBulkSettingsDeactive() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DEACTIVE, "")
    }

    fun eventBulkSettingsDeleteBulk() {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DELETE_BULK,
            ""
        )
    }

    fun eventEditPriceSave(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_PRICE_SAVE, label)
    }

    fun eventEditStockToggle(state: String, label: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_STOCK_TOGGLE} $state"
        eventProductManage(action, label)
    }

    fun eventEditStockSave(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_STOCK_SAVE, label)
    }

    fun eventContactCs(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_CONTACT_CS, label)
    }

    fun eventSortingFilterName(sortName: String) {
        val action =
            "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_SORTING_FILTER_NAME} - $sortName"
        eventProductManage(action, "")
    }

    fun eventOthersFilterName(filterName: String) {
        val action =
            "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_OTHERS_FILTER_NAME} - $filterName"
        eventProductManage(action, "")
    }

    fun eventClickEditPriceVariant() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_PRICE_VARIANT, "")
    }

    fun eventClickEditPriceVariantSave() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_PRICE_VARIANT_SAVE, "")
    }

    fun eventClickEditStockVariant() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_STOCK_VARIANT, "")
    }

    fun eventClickStatusToggleVariant(status: ProductStatus) {
        with(ProductManageDataLayer) {
            val label = if (status == ProductStatus.ACTIVE) {
                STATUS_TOGGLE_ON
            } else {
                STATUS_TOGGLE_OFF
            }
            eventProductManage(EVENT_ACTION_CLICK_ON_STATUS_TOGGLE_VARIANT, label)
        }
    }

    fun eventClickChangeAmountVariant() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_CHANGE_AMOUNT_VARIANT, "")
    }

    fun eventClickEditStockVariantSave() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_EDIT_STOCK_VARIANT_SAVE, "")
    }

    fun eventClickMoreMenuEllipses() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_MENU_MORE_ELLIPSES, "")
    }

    fun eventClickMoreMenuShopShowcase() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_MENU_MORE_SHOP_SHOWCASE, "")
    }

    fun eventClickCloseStockAllocation(isVariant: Boolean) {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_CLOSE withAllocationType isVariant,
            ""
        )
    }

    fun eventClickAllocationMainStock(isVariant: Boolean) {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_ON_MAIN_STOCK withAllocationType isVariant,
            ""
        )
    }

    fun eventClickAllocationProductStatus(
        isVariant: Boolean,
        isOn: Boolean,
        source: String = "",
        productId: String = "",
        shopId: String = ""
    ) {
        var label = if (isOn) {
            ProductManageDataLayer.EVENT_LABEL_ALLOCATION_ON
        } else {
            ProductManageDataLayer.EVENT_LABEL_ALLOCATION_OFF
        }
        if (source.isNotEmpty()) label = "$label - $source"
        label = addProductIdAndShopId(label, productId, shopId)
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_PRODUCT_STATUS withAllocationType isVariant,
            label
        )
    }

    fun eventClickAllocationDecreaseStock(
        isVariant: Boolean,
        source: String = "",
        productId: String = "",
        shopId: String = ""
    ) {
        var label = source
        label = addProductIdAndShopId(label, productId, shopId)
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_DECREASE_STOCK withAllocationType isVariant,
            label
        )
    }

    fun eventClickAllocationInputStock(isVariant: Boolean) {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_INPUT_STOCK withAllocationType isVariant,
            ""
        )
    }

    fun eventClickAllocationIncreaseStock(
        isVariant: Boolean,
        source: String = "",
        productId: String = "",
        shopId: String = ""
    ) {
        var label = source
        label = addProductIdAndShopId(label, productId, shopId)
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_INCREASE_STOCK withAllocationType isVariant,
            label
        )
    }

    fun eventClickAllocationOnStockCampaign(isVariant: Boolean) {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_ON_STOCK_CAMPAIGN withAllocationType isVariant,
            ""
        )
    }

    fun eventClickAllocationSaveStock(
        isVariant: Boolean,
        isMain: Boolean,
        source: String,
        productId: String,
        shopId: String
    ) {
        var label = if (isMain) {
            "${ProductManageDataLayer.EVENT_LABEL_ALLOCATION_MAIN} - $source"
        } else {
            ProductManageDataLayer.EVENT_LABEL_ALLOCATION_CAMPAIGN
        }
        label = label.plus(" - $productId - $shopId")
        val eventAction =
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_SAVE_STOCK withAllocationType isVariant
        eventProductManage(eventAction, label)
    }

    fun eventClickPreviewVariantProduct() {
        eventProductManage(
            ProductManageDataLayer.EVENT_ACTION_CLICK_ALLOCATION_PREVIEW_VARIANT_PRODUCT,
            ""
        )
    }

    fun eventClickCreateProductCoupon(shopId: String) {
        val eventMap =
            EventTracking(
                ProductManageDataLayer.EVENT_NAME_CLICK_PG,
                ProductManageDataLayer.EVENT_CATEGORY_PRODUCT_LIST_PAGE,
                ProductManageDataLayer.EVENT_ACTION_CLICK_CREATE_PRODUCT_COUPON,
                ""
            ).dataTracking.plus(
                mapOf(
                    ProductManageDataLayer.CURRENT_SITE to ProductManageDataLayer.TOKOPEDIA_SELLER,
                    ProductManageDataLayer.SHOP_ID to shopId,
                    ProductManageDataLayer.BUSINESS_UNIT to ProductManageDataLayer.PHYSICAL_GOODS
                )
            )

        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun sendScreen(screenName: String, pageSource: String) {
        val customDimension =
            mapOf(ProductManageDataLayer.CUSTOM_DIMENSION_PAGE_SOURCE to pageSource)
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    fun sendStockAllocationScreen(isVariant: Boolean) {
        val screenName =
            if (isVariant) {
                ProductManageDataLayer.SCREEN_NAME_STOCK_ALLOCATION_VARIANT
            } else {
                ProductManageDataLayer.SCREEN_NAME_STOCK_ALLOCATION_SINGLE
            }
        sendScreen(screenName)
    }

    private fun addProductIdAndShopId(
        label: String,
        productId: String,
        shopId: String
    ): String {
        var newLabel = label
        if (productId.isNotEmpty() && shopId.isNotEmpty()) {
            newLabel = label.plus(" - $productId - $shopId")
        }
        return newLabel
    }
}
