package com.tokopedia.salam.umrah.common.util

import com.tokopedia.salam.umrah.common.data.UmrahVariant
/**
 * @author by M on 31/10/2019
 */
object UmrahHotelVariant {
    fun getAllHotelVariants(hotelVariants: List<UmrahVariant>): String {
        val variants = mutableListOf<String>()
        for (variant in hotelVariants){
            variants.add(variant.name)
        }
        var all = variants[0]
        for (i in 1 until variants.size){
            all += if(i<variants.size-1) ", "
            else ", dan "
            all+= variants[i]
        }
        return all
    }

    fun getHotelVariantbyID(hotelVariants: List<UmrahVariant>, id: String): String{
        var variantName= ""
        for (variant in hotelVariants){
            if(id.equals(variant.id)){
                variantName = variant.name
            }
        }
        return variantName
    }

}