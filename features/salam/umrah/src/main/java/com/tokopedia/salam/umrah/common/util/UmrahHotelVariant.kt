package com.tokopedia.salam.umrah.common.util

import com.tokopedia.salam.umrah.common.data.UmrahVariant

/**
 * @author by M on 31/10/2019
 */
object UmrahHotelVariant {
    fun getHotelVariantbyID(hotelVariants: List<UmrahVariant>, id: String): String {
        var variantName = ""
        for (variant in hotelVariants) {
            if (id == variant.id) {
                variantName = variant.name
            }
        }
        return variantName
    }
}