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
    fun eventFormMapper(productDetailData: ProductDetailData, eventCheckoutAdditionalData: EventCheckoutAdditionalData): MutableList<Form> {
        val formData: MutableList<Form> = mutableListOf()
        when (eventCheckoutAdditionalData.additionalType) {
            AdditionalType.PACKAGE_UNFILL, AdditionalType.PACKAGE_FILLED -> {
                val packagePdp = getPackage(productDetailData, eventCheckoutAdditionalData.idPackage)
                packagePdp.formsPackages.map {
                    formData.add(it)
                }
            }

            AdditionalType.ITEM_UNFILL, AdditionalType.ITEM_FILLED -> {
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

    fun setListBottomSheetForm(mapData: LinkedHashMap<String, String>): ArrayList<ListItemUnify> {
        val array = arrayListOf<ListItemUnify>()
        mapData.map {
            val itemUnify = ListItemUnify(it.value, "")
            itemUnify.isBold = false
            array.add(itemUnify)
        }
        return array
    }

    fun searchHashMap(keyword: String, mapData: LinkedHashMap<String, String>): LinkedHashMap<String, String> {
        return mapData.filter {
            it.value.contains(keyword, true)
        } as LinkedHashMap<String, String>
    }

    fun getSearchableList(mapData: LinkedHashMap<String, String>): ArrayList<ListItemUnify> {
        val array = arrayListOf<ListItemUnify>()
        mapData.map {
            val itemUnify = ListItemUnify(it.value, "")
            itemUnify.isBold = false
            array.add(itemUnify)
        }
        return array
    }

    fun mapFormToString(list: List<Form>): List<String> {
        return list.map {
            if (it.elementType.equals("list"))
                it.valueList else it.value
        }
    }

    fun getFirstName(name: String): String {
        val splittedName = name.split(" ")
        return if (!splittedName.isNullOrEmpty()) {
            splittedName.get(splittedName.size - 1)
        } else ""
    }

    fun getLastName(name: String): String{
        val lastName = name.substringBeforeLast(" ")
        return if (!lastName.isNullOrEmpty()){
            lastName
        } else ""

    }

}