package com.tokopedia.product.manage.feature.list.analytics

import com.tokopedia.product.manage.feature.list.constant.ProductManageDataLayer
import com.tokopedia.product.manage.feature.list.constant.CLICK
import com.tokopedia.track.TrackApp

object ProductManageTracking {

    private fun eventProductManage(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(EventTracking(
                ProductManageDataLayer.EVENT_NAME,
                ProductManageDataLayer.EVENT_CATEGORY,
                action,
                label
        ).dataTracking)
    }

    private fun eventEditProduct(action: String, shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(EventTracking(
                ProductManageDataLayer.EVENT_NAME_EDIT_PRODUCT,
                ProductManageDataLayer.EVENT_CATEGORY_EDIT_PRODUCT,
                action,
                "",
                shopId
        ).dataTracking)
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

    fun eventEditVariants(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_EDIT_VARIANTS, label)
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

    fun eventSettingsCashback(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_CASHBACK, label)
    }

    fun eventSettingsFeatured(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_SETTINGS_FEATURED, label)
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
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_SAVE, "")
    }

    fun eventFeaturedProductPopUpMore() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_FEATURED_PRODUCT_POP_UP_MORE, "")
    }

    fun eventCashbackSettingsSave(label: String) {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_CASHBACK_SETTINGS, label)
    }

    fun eventCashbackSettingsPopUp() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_CASHBACK_SETTINGS_POP_UP, "")
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
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER} - $filterName - $state"
        eventProductManage(action, "")
    }

    fun eventMoreOthersFilterSave() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_MORE_OTHERS_FILTER_SAVE, "")
    }

    fun eventBulkSettings() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS, "")
    }

    fun eventBulkSettingsMoveEtalase() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_MOVE_ETALASE, "")
    }

    fun eventBulkSettingsDeactive() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DEACTIVE, "")
    }

    fun eventBulkSettingsDeleteBulk() {
        eventProductManage(ProductManageDataLayer.EVENT_ACTION_CLICK_ON_BULK_SETTINGS_DELETE_BULK, "")
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
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_SORTING_FILTER_NAME} - $sortName"
        eventProductManage(action, "")
    }

    fun eventOthersFilterName(filterName: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_ON_OTHERS_FILTER_NAME} - $filterName"
        eventProductManage(action, "")
    }

    fun eventClickBackOnPromotionPage(shopId: String) {
        val action = ProductManageDataLayer.EVENT_ACTION_CLICK_ON_PROMOTION_PAGE
        eventEditProduct(action, shopId)
    }

    fun eventClickCashbackValue(value: Int, shopId: String) {
        val action = "${ProductManageDataLayer.EVENT_ACTION_CLICK_CASHBACK_VALUE} $value%"
        eventEditProduct(action, shopId)
    }

    fun eventClickNoCashbackValue(shopId: String) {
        val action = ProductManageDataLayer.EVENT_ACTION_CLICK_NO_CASHBACK_VALUE
        eventEditProduct(action, shopId)
    }

    fun eventClickSavePromotion(shopId: String) {
        val action = ProductManageDataLayer.EVENT_ACTION_CLICK_SAVE_PROMOTION
        eventEditProduct(action, shopId)
    }

}