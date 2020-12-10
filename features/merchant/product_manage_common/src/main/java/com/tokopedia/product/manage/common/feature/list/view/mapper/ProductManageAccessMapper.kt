package com.tokopedia.product.manage.common.feature.list.view.mapper

import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.ADD_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.DELETE_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.DUPLICATE_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_PRICE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_STOCK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.MOVE_ETALASE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.MULTI_SELECT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_CASHBACK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_FEATURED
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_TOP_ADS
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.STOCK_REMINDER
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.UPDATE_STOCK
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess

object ProductManageAccessMapper {

    fun mapToProductManageAccess(response: Response): ProductManageAccess {
        val accessIds = response.data.access.map { it.id }

        return ProductManageAccess(
            accessIds.contains(ADD_PRODUCT),
            accessIds.contains(EDIT_PRODUCT),
            accessIds.contains(MOVE_ETALASE),
            accessIds.contains(MULTI_SELECT),
            accessIds.contains(EDIT_PRICE),
            accessIds.contains(EDIT_STOCK),
            accessIds.contains(UPDATE_STOCK),
            accessIds.contains(DUPLICATE_PRODUCT),
            accessIds.contains(STOCK_REMINDER),
            accessIds.contains(DELETE_PRODUCT),
            accessIds.contains(SET_TOP_ADS),
            accessIds.contains(SET_CASHBACK),
            accessIds.contains(SET_FEATURED)
        )
    }

    fun mapProductManageOwnerAccess(): ProductManageAccess {
        return ProductManageAccess(
            addProduct = true,
            editProduct = true,
            changeEtalase = true,
            multiSelect = true,
            editPrice = true,
            editStock = true,
            updateStock = false,
            duplicateProduct = true,
            setStockReminder = true,
            deleteProduct = true,
            setTopAds = true,
            setCashBack = true,
            setFeatured = true
        )
    }
}