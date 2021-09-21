package com.tokopedia.vouchergame.common.util

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.data.product.CatalogProductData
import com.tokopedia.vouchergame.detail.data.VoucherGameDetailData
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData

object VoucherGameDataMapper {

    fun convertCatalogDataToVoucherGameDetailData(
        catalogData: CatalogData
    ): VoucherGameDetailData{
        return VoucherGameDetailData(
            needEnquiry = catalogData.needEnquiry,
            isShowingProduct = catalogData.isShowingProduct,
            enquiryFields = catalogData.enquiryFields,
            product = convertToVoucherGameProductData(catalogData.product)
        )
    }

    private fun convertToVoucherGameProductData(
        catalogProductData: CatalogProductData
    ): VoucherGameProductData {
        return VoucherGameProductData(
            name = catalogProductData.name,
            text = catalogProductData.text,
            dataCollections = catalogProductData.dataCollections.map {
                convertToVoucherGameDataCollection(it)
            }
        )
    }

    private fun convertToVoucherGameDataCollection(
        commonDataCollection: CatalogProductData.DataCollection
    ): VoucherGameProductData.DataCollection {
        return VoucherGameProductData.DataCollection(
            name = commonDataCollection.name,
            products = commonDataCollection.products.map {
                convertToVoucherGameProduct(it)
            }
        )
    }

    private fun convertToVoucherGameProduct(catalogProduct: CatalogProduct): VoucherGameProduct {
        return VoucherGameProduct(
            id = catalogProduct.id,
            attributes = catalogProduct.attributes
        )
    }
}