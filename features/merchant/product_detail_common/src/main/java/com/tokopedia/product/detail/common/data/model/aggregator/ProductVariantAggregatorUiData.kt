package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirProduct
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
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

        //region only for tracker
        var simpleBasicInfo: SimpleBasicInfo = SimpleBasicInfo(),

        var shopType: String = "",

        var boData: List<BebasOngkirProduct> = listOf()
        //endregion
) {

    fun getIsFreeOngkirByBoType(productId: String): Boolean {
        val boType = boData.firstOrNull {
            it.productId == productId
        }?.boType ?: ProductDetailCommonConstant.NO_BEBAS_ONGKIR

        return boType != ProductDetailCommonConstant.NO_BEBAS_ONGKIR
    }

    fun isAggregatorEmpty(): Boolean {
        return (!variantData.hasChildren && !variantData.hasVariant) || cardRedirection.isEmpty() || nearestWarehouse.isEmpty()
    }
}
