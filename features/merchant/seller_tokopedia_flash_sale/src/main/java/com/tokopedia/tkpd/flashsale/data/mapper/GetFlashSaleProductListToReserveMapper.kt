package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductListToReserveResponse
import javax.inject.Inject

class GetFlashSaleProductListToReserveMapper @Inject constructor() {

    fun map(response: GetFlashSaleProductListToReserveResponse) = response.getFlashSaleProductListToReserve.productList.map {
        val submittedProductIds = response.getFlashSaleProductListToReserve.submittedProductIds
        ChooseProductItem(
            productId = it.productId.toString(),
            productName = it.name,
            imageUrl = it.pictureUrl,
            variantText = it.variantMeta.countVariants.toString() + " Varian Produk",
            variantTips = it.variantMeta.countEligibleVariants.toString()  + " varian sesuai kriteria",
            priceText = mapPrice(it.variantMeta.countVariants, it.price),
            stockText = "Total Stok ${it.stock} di ${it.countEligibleWarehouses} lokasi",
            errorMessage = it.disableDetail.disableTitle,
            isError = it.disableDetail.isDisabled,
            isEnabled = !it.disableDetail.isDisabled,
            showCheckDetailCta = it.disableDetail.showCriteriaCheckingCta,
            isSelected = submittedProductIds.any { productId -> productId == it.productId },
        )
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