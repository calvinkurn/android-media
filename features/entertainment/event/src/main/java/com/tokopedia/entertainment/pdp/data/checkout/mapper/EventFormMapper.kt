package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackageItem
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

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
                val packagePdp = getPackage(productDetailData, eventCheckoutAdditionalData.idPackage)
                val packageItem = getPackageItem(packagePdp, eventCheckoutAdditionalData.idItem)
                packageItem.formsItems.map {
                    formData.add(it)
                }
            }
            
            AdditionalType.NULL_DATA -> {
                productDetailData.forms.map {
                    formData.add(it)
                }
            }
        }
        return formData
    }

    fun setListBottomSheetForm(list : List<String>):ArrayList<ListItemUnify>{
        val array = arrayListOf<ListItemUnify>()
        list.mapIndexed { index, s ->
            val itemUnify = ListItemUnify(s, "")
            itemUnify.setVariant(null,ListItemUnify.RADIO_BUTTON)
            array.add(itemUnify)
        }
        return array
    }

    fun clearRadioState(listItemUnify: ArrayList<ListItemUnify>, index:Int) {
        val removedList = arrayListOf<ListItemUnify>()
        removedList.addAll(listItemUnify)
        removedList.removeAt(index)
        removedList.forEach {
            it.listRightRadiobtn?.isChecked = false
        }
    }
}