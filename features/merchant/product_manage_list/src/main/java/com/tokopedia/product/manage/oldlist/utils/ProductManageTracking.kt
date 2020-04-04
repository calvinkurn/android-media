package com.tokopedia.product.manage.oldlist.utils

import android.text.TextUtils
import com.tokopedia.product.manage.oldlist.analytics.EventTracking
import com.tokopedia.product.manage.oldlist.constant.*
import com.tokopedia.product.manage.oldlist.constant.option.CatalogProductOption
import com.tokopedia.product.manage.oldlist.constant.option.ConditionProductOption
import com.tokopedia.product.manage.oldlist.constant.option.PictureStatusProductOption
import com.tokopedia.product.manage.oldlist.data.model.ProductManageFilterModel
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant
import com.tokopedia.track.TrackApp
import java.util.*

object ProductManageTracking {

    private fun eventProductManage(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(EventTracking(
                EVENT_MANAGE_PRODUCT,
                MANAGE_PRODUCT,
                action,
                label
        ).event)
    }

    fun eventProductManageTopNav(label: String) {
        eventProductManage(CLICK_TOP_NAV, label)
    }

    fun eventProductManageSearch() {
        eventProductManage(CLICK_TOP_NAV, SEARCH_PRODUCT)
    }

    fun eventProductManageClickDetail() {
        eventProductManage(CLICK_PRODUCT_LIST, CLICK_PRODUCT_LIST)
    }

    fun eventProductManageSortProduct(label: String) {
        eventProductManage(CLICK_SORT_PRODUCT, label)
    }

    fun eventProductManageFilterProduct(label: String) {
        eventProductManage(CLICK_FILTER_PRODUCT, label)
    }

    fun eventProductManageOverflowMenu(label: String) {
        eventProductManage(CLICK_OVERFLOW_MENU, label)
    }

    fun eventDraftClick(label: String) {
        eventProductManage(CLICK, label)
    }

    fun trackingFilter(productManageFilterModel: ProductManageFilterModel) {
        val filters = ArrayList<String>()
        if (productManageFilterModel.categoryId != ProductManageListConstant.FILTER_ALL_CATEGORY.toString()) {
            filters.add(CATEGORY)
        }

        if (productManageFilterModel.etalaseProductOption != ProductManageListConstant.FILTER_ALL_PRODUK) {
            filters.add(ETALASE)
        }

        if (productManageFilterModel.catalogProductOption != CatalogProductOption.WITH_AND_WITHOUT) {
            filters.add(CATALOG)
        }

        if (productManageFilterModel.conditionProductOption != ConditionProductOption.ALL_CONDITION) {
            filters.add(CONDITION)
        }

        if (productManageFilterModel.pictureStatusOption != PictureStatusProductOption.WITH_AND_WITHOUT) {
            filters.add(PICTURE_STATUS)
        }

        eventProductManageFilterProduct(TextUtils.join(",", filters))
    }

    fun trackerManageCourierButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_ADD_PRODUCT,
                CATEGORY_ADD_PRODUCT,
                ACTION_CLICK_MANAGE_COURIER,
                "")
    }

    fun trackerSeeProduct() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_ADD_PRODUCT,
                CATEGORY_ADD_PRODUCT,
                ACTION_SEE_PRODUCT,
                "")
    }

    fun trackerLinkClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_ADD_PRODUCT,
                CATEGORY_ADD_PRODUCT,
                ACTION_LINK,
                "")
    }
}