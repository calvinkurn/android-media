package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.select.data.response.GetSlashPriceProductListToReserveResponse
import com.tokopedia.shopdiscount.select.domain.entity.ProblematicReservableProduct
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import javax.inject.Inject

class ProblematicProductMapper @Inject constructor() {

    fun map(input: DoSlashPriceProductReservationResponse): List<ProblematicReservableProduct> {
        return input.doSlashPriceProductReservation.listFailedProduct.map { product ->
            ProblematicReservableProduct(product.productId, product.message, product.errorCode)
        }
    }
}