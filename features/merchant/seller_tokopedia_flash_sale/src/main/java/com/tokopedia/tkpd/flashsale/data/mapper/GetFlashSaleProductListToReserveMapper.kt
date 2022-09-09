package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductListToReserveResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductToReserve
import javax.inject.Inject

class GetFlashSaleProductListToReserveMapper @Inject constructor() {

    fun map(response: GetFlashSaleProductListToReserveResponse) = ProductToReserve(
        selectedProductIds = response.getFlashSaleProductListToReserve.submittedProductIds,
        selectedProductCount = response.getFlashSaleProductListToReserve.submittedProductIds.size,
        productList = mapProduct(response)
    )

    fun mapProduct(response: GetFlashSaleProductListToReserveResponse) = response.getFlashSaleProductListToReserve.productList.map {
        val submittedProductIds = response.getFlashSaleProductListToReserve.submittedProductIds
        val isSubmitted = submittedProductIds.any { productId -> productId == it.productId }
        ChooseProductItem(
            productId = it.productId.toString(),
            productName = it.name,
            imageUrl = it.pictureUrl,
            variantText = it.variantMeta.countVariants.toString() + " Varian Produk",
            variantTips = it.variantMeta.countEligibleVariants.toString()  + " varian sesuai kriteria",
            priceText = mapPrice(it.variantMeta.countVariants, it.price),
            stockText = mapStock(it),
            errorMessage = it.disableDetail.disableTitle,
            hasVariant = it.variantMeta.countVariants.isMoreThanZero(),
            isError = it.disableDetail.isDisabled,
            isEnabled = !it.disableDetail.isDisabled && !isSubmitted, // only enable not submitted data
            showCheckDetailCta = it.disableDetail.showCriteriaCheckingCta,
            isSelected = isSubmitted,
            criteriaId = it.productCriteria.criteriaId
        )
    }

    private fun mapStock(it: GetFlashSaleProductListToReserveResponse.ProductList): String {
        return "Total Stok ${it.stock} " + if (it.countEligibleWarehouses.isMoreThanZero()) {
            "di ${it.countEligibleWarehouses} lokasi"
        } else ""
    }

    private fun mapPrice(
        countVariants: Int,
        price: GetFlashSaleProductListToReserveResponse.Price,
    ) = if (countVariants.isMoreThanZero()) {
        val min = price.lowerPrice.getCurrencyFormatted()
        val max = price.upperPrice.getCurrencyFormatted()
        "$min $max"
    } else {
        price.price.getCurrencyFormatted()
    }

}