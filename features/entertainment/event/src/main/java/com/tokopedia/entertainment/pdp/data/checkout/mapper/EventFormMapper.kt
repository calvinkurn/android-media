package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackage
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventPackageMapper.getPackageItem
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_EMAIL
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_FAMILY_NAME
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_FIRST_NAME
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_MOBILE
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_NAME
import com.tokopedia.entertainment.pdp.fragment.EventCheckoutFragment.Companion.PASSENGER_PHONE
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_edittext_item.view.*

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

    fun getFamilyName(name: String): String {
        val splittedName = name.split(" ")
        return if (!splittedName.isNullOrEmpty()) {
            splittedName.get(splittedName.size - 1)
        } else ""
    }

    fun getFirstName(name: String): String{
        val lastName = name.substringBeforeLast(" ")
        return if (!lastName.isNullOrEmpty()){
            lastName
        } else ""

    }


    fun initialListForm(list: List<Form>, userSession:UserSessionInterface, nullableData:String):List<Form>{
        for (i in 0..list.size-1) {
            list.get(i).apply {
                if (this.value.isNullOrEmpty()){
                    this.value = when(this.name){
                        PASSENGER_NAME -> userSession.name
                        PASSENGER_FAMILY_NAME -> getFamilyName(userSession.name)
                        PASSENGER_FIRST_NAME -> getFirstName(userSession.name)
                        PASSENGER_EMAIL -> userSession.email
                        PASSENGER_PHONE, PASSENGER_MOBILE ->  userSession.phoneNumber
                        else -> nullableData
                    }
                }
            }
        }

        return list
    }

    fun isEmptyForms(list: List<Form>, emptyString: String):Boolean{
        var status = false
        loop@for (i in 0..list.size-1){
            if(list.get(i).value.isNullOrEmpty() || list.get(i).value.equals(emptyString)) {
                status = true
                break@loop
            }
        }
        return status
    }
}