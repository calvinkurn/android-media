package com.tokopedia.product.manage.common.feature.list.view.mapper

import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.ADD_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.BROADCAST_CHAT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.DELETE_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.DUPLICATE_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_PRICE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_PRODUCT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.EDIT_STOCK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.ETALASE_LIST
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.MULTI_SELECT
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.PRODUCT_LIST
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_CASHBACK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_FEATURED
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.SET_TOP_ADS
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageAccessConstant.STOCK_REMINDER
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess

object ProductManageAccessMapper {

    fun mapToProductManageAccess(response: Response): ProductManageAccess {
        val accessIds = response.data.access.map { it.id }

        return ProductManageAccess(
            accessIds.contains(ADD_PRODUCT),
            accessIds.contains(EDIT_PRODUCT),
            accessIds.contains(ETALASE_LIST),
            accessIds.contains(MULTI_SELECT),
            accessIds.contains(EDIT_PRICE),
            accessIds.contains(EDIT_STOCK),
            accessIds.contains(DUPLICATE_PRODUCT),
            accessIds.contains(STOCK_REMINDER),
            accessIds.contains(DELETE_PRODUCT),
            accessIds.contains(SET_TOP_ADS),
            accessIds.contains(SET_CASHBACK),
            accessIds.contains(SET_FEATURED),
            accessIds.contains(PRODUCT_LIST),
            accessIds.contains(BROADCAST_CHAT)
        )
    }

    fun mapProductManageOwnerAccess(): ProductManageAccess {
        return ProductManageAccess(
            addProduct = true,
            editProduct = true,
            etalaseList = true,
            multiSelect = true,
            editPrice = true,
            editStock = true,
            duplicateProduct = true,
            setStockReminder = true,
            deleteProduct = true,
            setTopAds = true,
            setCashBack = true,
            setFeatured = true,
            productList = true,
            broadcastChat = true
        )
    }

    fun mapDefaultProductManageAccess(): ProductManageAccess {
        return ProductManageAccess(
            addProduct = false,
            editProduct = false,
            etalaseList = false,
            multiSelect = false,
            editPrice = false,
            editStock = false,
            duplicateProduct = false,
            setStockReminder = false,
            deleteProduct = false,
            setTopAds = false,
            setCashBack = false,
            setFeatured = false,
            productList = false,
            broadcastChat = false
        )
    }
}