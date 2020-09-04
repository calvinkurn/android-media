package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage

object EventFormMapper {
    fun eventFormMapper(productDetailData: ProductDetailData, eventCheckoutAdditionalData: EventCheckoutAdditionalData):MutableList<Form>{
        val formData: MutableList<Form> = mutableListOf()
        when(eventCheckoutAdditionalData.additionalType){
             AdditionalType.PACKAGE_UNFILL -> {
                 val packagePdp = getPackage(productDetailData, eventCheckoutAdditionalData.idPackage)
                 packagePdp.formsPackages.map { 
                     formData.add(it) 
                 }
             }

            AdditionalType.ITEM_UNFILL -> {
                
            }
            
            AdditionalType.NULL_DATA -> {
                productDetailData.forms.map {
                    formData.add(it)
                }
            }
        }
        return formData
    }
}