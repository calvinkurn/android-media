package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.rates.ErrorBottomSheet
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 18/05/21
 */
data class ProductVariantAggregatorUiData(
        var variantData: ProductVariant = ProductVariant(),

        val cardRedirection: Map<String, CartTypeData> = mapOf(),

        var nearestWarehouse: Map<String, WarehouseInfo> = mapOf(),

        var alternateCopy: List<AlternateCopy> = listOf(),

        var rates: List<P2RatesEstimate> = listOf(),

        //region only for tracker
        var simpleBasicInfo: SimpleBasicInfo = SimpleBasicInfo(),

        var shopType: String = "",

        var boData: BebasOngkir = BebasOngkir()
        //endregion
) {

    fun getRatesEstimateByProductId(productId: String): P2RatesEstimateData? {
        var result: P2RatesEstimateData? = null
        rates.forEach {
            if (productId in it.listfProductId) result = it.p2RatesData
        }
        return result
    }

    fun getRatesBottomSheetData(productId: String): ErrorBottomSheet? {
        var result: ErrorBottomSheet? = null
        rates.forEach {
            if (productId in it.listfProductId) result = it.errorBottomSheet
        }
        return result
    }


    fun getIsFreeOngkirByBoType(productId: String): Boolean {
        val boType = boData.boProduct.firstOrNull {
            it.productId == productId
        }?.boType ?: ProductDetailCommonConstant.NO_BEBAS_ONGKIR

        return boType != ProductDetailCommonConstant.NO_BEBAS_ONGKIR
    }

    fun getIsFreeOngkirImageUrl(productId: String): String {
        val boType = boData.boProduct.firstOrNull {
            it.productId == productId
        }?.boType ?: ProductDetailCommonConstant.NO_BEBAS_ONGKIR

        return boData.boImages.firstOrNull {
            it.boType == boType
        }?.imageURL ?: ""
    }

    fun isAggregatorEmpty(): Boolean {
        return (!variantData.hasChildren && !variantData.hasVariant) || cardRedirection.isEmpty() || nearestWarehouse.isEmpty()
    }
}
