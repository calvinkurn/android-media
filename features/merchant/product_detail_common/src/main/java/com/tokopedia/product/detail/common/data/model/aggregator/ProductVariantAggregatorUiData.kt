package com.tokopedia.product.detail.common.data.model.aggregator

import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantOption
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

        var reData: RestrictionInfoResponse = RestrictionInfoResponse(),

        var uspImageUrl: String = "",

        var cashBackPercentage: Int = 0,

        //region only for tracker
        var simpleBasicInfo: SimpleBasicInfo = SimpleBasicInfo(),

        var shopType: String = "",

        var boData: BebasOngkir = BebasOngkir(),

        var isCod: Boolean = false
        //endregion
) {
    fun getP2RatesEstimateByProductId(productId: String): P2RatesEstimate? {
        var result: P2RatesEstimate? = null
        rates.forEach {
            if (productId in it.listfProductId) result = it
        }
        return result
    }

    fun shouldHideRatesBottomSheet(rates: P2RatesEstimate?): Boolean {
        if (rates == null) return true
        return rates.p2RatesData.p2RatesError.isEmpty() || rates.p2RatesData.p2RatesError.firstOrNull()?.errorCode == 0
    }

    fun getBebasOngkirStringType(productId: String): String {
        val boType = boData.boProduct.firstOrNull {
            it.productId == productId
        }?.boType ?: ProductDetailCommonConstant.NO_BEBAS_ONGKIR

        return ProductCartHelper.getBoTrackerString(boType)
    }

    fun isAggregatorEmpty(): Boolean {
        return (!variantData.hasChildren && !variantData.hasVariant) || cardRedirection.isEmpty() || nearestWarehouse.isEmpty()
    }

    private fun getFirstLevelVariantOptions(): List<VariantOption> {
        return variantData.variants.firstOrNull()?.options ?: emptyList()
    }

    fun getFirstLevelVariantImage(variantOptionId: String): String? {
        if (variantOptionId.isEmpty()) return null
        val variantOption = getFirstLevelVariantOptions().firstOrNull { it.id == variantOptionId }
        val variantImage = variantOption?.picture?.original?.takeIf { it.isNotEmpty() }
        return variantImage ?: simpleBasicInfo.defaultMediaURL.takeIf { it.isNotEmpty() }
    }

    fun getVariantGalleryItems(): List<ProductDetailGallery.Item> {
        val variantOptions = getFirstLevelVariantOptions()
        return variantOptions.mapNotNull { option ->
            val optionId = option.id ?: "0"
            val imageUrl = option.picture?.original
            val tag = option.value ?: ""

            imageUrl?.takeIf { it.isNotEmpty() }?.let {
                ProductDetailGallery.Item(
                    id = optionId,
                    url = imageUrl,
                    tag = tag,
                    type = ProductDetailGallery.Item.Type.Image
                )
            }
        }
    }
}
