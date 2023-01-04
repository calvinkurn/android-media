package com.tokopedia.tkpd.flashsale.data.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductListToReserveResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductToReserve
import javax.inject.Inject

class GetFlashSaleProductListToReserveMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val MINIMUM_COUNT_ELIGIBLE_WAREHOUSE = 1
    }

    private var lastSelectedProductCount: Int = 0

    fun map(response: GetFlashSaleProductListToReserveResponse): ProductToReserve {
        return response.getFlashSaleProductListToReserve.submittedProductIds.let {
            val selectedProductCount = getValidProductCount(it)
            ProductToReserve(
                selectedProductIds = it,
                selectedProductCount = selectedProductCount,
                productList = mapProduct(response)
            )
        }
    }

    fun mapProduct(response: GetFlashSaleProductListToReserveResponse) = response.getFlashSaleProductListToReserve.productList.map {
        ChooseProductItem(
            productId = it.productId.toString(),
            productName = it.name,
            imageUrl = it.pictureUrl,
            variantText = context.getString(R.string.choose_product_variant_count_format, it.variantMeta.countVariants),
            variantTips = context.getString(R.string.chooseproduct_criteria_variant_format, it.variantMeta.countEligibleVariants),
            priceText = mapPrice(it.price),
            stockText = mapStock(it),
            errorMessage = it.disableDetail.disableTitle,
            hasVariant = it.variantMeta.countVariants.isMoreThanZero(),
            isError = it.disableDetail.isDisabled,
            isEnabled = !it.disableDetail.isDisabled,
            showCheckDetailCta = it.disableDetail.showCriteriaCheckingCta,
            criteriaId = it.productCriteria.criteriaId
        )
    }

    /**
     * To prevent resetting selected product count to 0
     * this only happened when searching product using keyword, but has no result
     */
    private fun getValidProductCount(selectedList: List<Long>): Int {
        return if (selectedList.isEmpty()) {
            lastSelectedProductCount
        } else {
            lastSelectedProductCount = selectedList.size
            selectedList.size
        }
    }

    private fun mapStock(it: GetFlashSaleProductListToReserveResponse.ProductList): String {
        val stockTotalTitle = context.getString(R.string.chooseproduct_total_stock_format)
        return "$stockTotalTitle ${it.stock} " + if (it.countEligibleWarehouses > MINIMUM_COUNT_ELIGIBLE_WAREHOUSE) {
            context.getString(R.string.choose_product_location_format, it.countEligibleWarehouses)
        } else ""
    }

    private fun mapPrice(price: GetFlashSaleProductListToReserveResponse.Price) =
        if (price.lowerPrice != price.upperPrice) {
            val min = price.lowerPrice.getCurrencyFormatted()
            val max = price.upperPrice.getCurrencyFormatted()
            "$min - $max"
        } else {
            price.price.getCurrencyFormatted()
        }

}