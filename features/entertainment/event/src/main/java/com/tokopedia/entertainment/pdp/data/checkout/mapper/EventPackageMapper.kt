package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutAdditionalData
import com.tokopedia.entertainment.pdp.data.pdp.ItemMap
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import java.util.regex.Matcher
import java.util.regex.Pattern

object EventPackageMapper {

    fun getPackage(productDetailData: ProductDetailData, packageID: String):PackageV3 {
        var packageV3 = PackageV3()
        for(i in 0..productDetailData.packages.size-1){
            if(packageID.equals(productDetailData.packages[i].id)){
                packageV3 = productDetailData.packages[i]
            }
        }
        return packageV3
    }

    fun getPackageItem(packageV3: PackageV3, itemId:String):PackageItem {
        var packageItem = PackageItem()
        for (i in 0..packageV3.packageItems.size - 1){
            if (itemId.equals(packageV3.packageItems.get(i).id)){
                packageItem = packageV3.packageItems.get(i)
            }
        }

        return packageItem
    }


    fun getItemMap(metadata: MetaDataResponse): ItemMapResponse{
        var itemMapResponse = ItemMapResponse()
        if (!metadata.itemMap.isNullOrEmpty()){
            for (itemMap in metadata.itemMap){
                itemMapResponse = itemMap
            }
        }

        return itemMapResponse
    }

    fun getDigit(str: String): Int{
        if(str.isNotBlank()){
            val pattern = Pattern.compile("[^0-9]")
            val matcher: Matcher = pattern.matcher(str)
            return matcher.replaceAll("").toInt()
        }
        else return -1
    }

    fun getAdditionalList(itemMaps: List<ItemMapResponse>):MutableList<EventCheckoutAdditionalData>{
        val additionalList = mutableListOf<EventCheckoutAdditionalData>()
        if(!itemMaps.isNullOrEmpty()){
            itemMaps.map {
                for(i in 1..it.quantity) {
                    additionalList.add(EventCheckoutAdditionalData(idPackage = it.packageId,idItem = it.id, titleItem = it.name+" "+i, additionalType = AdditionalType.ITEM_UNFILL))
                }
            }
        }
        return additionalList
    }

    fun getAdditionalPackage(pdp:ProductDetailData, packageID: String):EventCheckoutAdditionalData{
        return if (!getPackage(pdp, packageID).formsPackages.isNullOrEmpty()){
             EventCheckoutAdditionalData(idPackage = packageID, additionalType = AdditionalType.PACKAGE_UNFILL)
        } else EventCheckoutAdditionalData()
    }
}