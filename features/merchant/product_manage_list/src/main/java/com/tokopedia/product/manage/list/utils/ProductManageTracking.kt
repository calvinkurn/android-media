package com.tokopedia.product.manage.list.utils

import android.text.TextUtils
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.nishikino.model.EventTracking
import com.tokopedia.product.manage.list.constant.*
import com.tokopedia.product.manage.list.constant.option.CatalogProductOption
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption
import com.tokopedia.product.manage.list.constant.option.PictureStatusProductOption
import com.tokopedia.product.manage.list.data.model.ProductManageFilterModel
import com.tokopedia.seller.product.manage.constant.ProductManageConstant
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
        eventProductManage(AppEventTracking.Action.CLICK, label)
    }

    fun trackingFilter(productManageFilterModel: ProductManageFilterModel) {
        val filters = ArrayList<String>()
        if (productManageFilterModel.categoryId != ProductManageConstant.FILTER_ALL_CATEGORY.toString()) {
            filters.add(CATEGORY)
        }

        if (productManageFilterModel.etalaseProductOption != ProductManageConstant.FILTER_ALL_PRODUK) {
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