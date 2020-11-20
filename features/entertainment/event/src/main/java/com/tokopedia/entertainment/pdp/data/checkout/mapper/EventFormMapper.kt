package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.EMAIL_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FAMILY_NAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FIRST_NAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FULLNAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.MOBILE_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.PHONE_TYPE
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackageItem
import com.tokopedia.user.session.UserSessionInterface

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

    fun setListBottomSheetString(mapData: LinkedHashMap<String, String>): List<String> {
        val listString = mutableListOf<String>()
        mapData.map {
            listString.add(it.value)
        }
        return listString
    }

    fun searchHashMap(keyword: String, mapData: LinkedHashMap<String, String>): LinkedHashMap<String, String> {
        return mapData.filter {
            it.value.contains(keyword, true)
        } as LinkedHashMap<String, String>
    }

    fun mapFormToString(list: List<Form>): List<String> {
        return list.map {
            if (it.elementType.equals(ELEMENT_LIST))
                it.valueList else it.value
        }
    }

    fun getFamilyName(name: String): String {
        val splittedName = name.split(" ")
        return if (!splittedName.isNullOrEmpty()) {
            splittedName.get(splittedName.size - 1)
        } else ""
    }

    fun getFirstName(name: String): String {
        val lastName = name.substringBeforeLast(" ")
        return if (!lastName.isNullOrEmpty()) {
            lastName
        } else ""
    }


    fun initialListForm(list: List<Form>, userSession: UserSessionInterface, nullableData: String): List<Form> {
        for (i in 0..list.size - 1) {
            list.get(i).apply {
                if (this.value.isNullOrEmpty()) {
                    this.value = when (this.name) {
                        FULLNAME_TYPE -> userSession.name
                        FAMILY_NAME_TYPE -> getFamilyName(userSession.name)
                        FIRST_NAME_TYPE -> getFirstName(userSession.name)
                        EMAIL_TYPE -> userSession.email
                        PHONE_TYPE, MOBILE_TYPE -> userSession.phoneNumber
                        else -> nullableData
                    }
                } else if((this.valuePosition.isNullOrEmpty() || this.valuePosition.equals("-1")) && this.elementType.equals(ELEMENT_LIST)) this.valueList = nullableData
            }
        }

        return list
    }

    fun isEmptyForms(list: List<Form>, emptyString: String): Boolean {
        var status = false
        loop@ for (i in 0..list.size - 1) {
            if ((list.get(i).value.isNullOrEmpty() || list.get(i).value.equals(emptyString) && list.get(i).required==1) ) {
                status = true
                break@loop
            }
        }
        return status
    }
}