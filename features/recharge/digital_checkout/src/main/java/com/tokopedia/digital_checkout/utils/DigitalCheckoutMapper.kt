package com.tokopedia.digital_checkout.utils

import com.tokopedia.digital_checkout.data.model.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.response.atc.AttributesCart

/**
 * @author by jessica on 11/01/21
 */

object DigitalCheckoutMapper {
    fun mapDigitalInfo(digitalInfoDetails: List<AttributesCart.InfoDetail>): List<CartItemDigital> {
        return digitalInfoDetails.map {
            CartItemDigital(it.label ?: "", it.value ?: "")
        }
    }

    fun mapAdditionalInfo(digitalInfoDetails: List<AttributesCart.AdditionalInfo>): List<CartItemDigitalWithTitle> {
        return digitalInfoDetails.map {
            CartItemDigitalWithTitle(
                    it.title ?: "",
                    it.detail?.map { detail -> CartItemDigital(detail.label ?: "",
                            detail.value ?: "") } ?: listOf()
            )
        }
    }
}