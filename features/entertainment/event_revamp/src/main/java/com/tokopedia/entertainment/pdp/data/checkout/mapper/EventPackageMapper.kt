package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.ItemMap
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import java.util.regex.Matcher
import java.util.regex.Pattern

object EventPackageMapper {

    fun getPackage(scheduleId: String, groupID: String, packageID: String,
                   productDetailData: ProductDetailData): Package {

        var packet: Package = Package()
        for (i in 0..productDetailData.schedules.size-1) {
            val groups = productDetailData.schedules[i].groups

            for (j in 0..groups.size-1) {

                if (groups[j].id.equals(groupID)) {
                    val packages = groups[j].packages

                    for (k in 0..packages.size-1) {
                        if(packages[k].id.equals(packageID)){
                            packet = packages[k]
                        }
                    }
                }
            }
        }

        return packet
    }


    fun getPackage(productDetailData: ProductDetailData, packageID: String):PackageV3{
        var packageV3 = PackageV3()
        for(i in 0..productDetailData.packages.size-1){
            if(packageID.equals(productDetailData.packages[i].id)){
                packageV3 = productDetailData.packages[i]
            }
        }
        return packageV3
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

    fun getPackageItem(productDetailData: ProductDetailData, packageID: String):PackageItem{
        var packageItem = PackageItem()
        for(packageV3 in productDetailData.packages){
            if(packageID.equals(packageV3.id)){
                for (packageItems in packageV3.packageItems){
                    packageItem = packageItems
                }
            }
        }
        return packageItem
    }


    fun getSchedule(scheduleId: String, productDetailData: ProductDetailData): Schedule {

        var schedule = Schedule()
        for (i in 0..productDetailData.schedules.size-1) {
            val scheduleTemp = productDetailData.schedules[i].schedule
            if(scheduleTemp.id.equals(scheduleId)){
                schedule =  productDetailData.schedules[i].schedule
            }
        }

        return schedule
    }

    fun getDigit(str: String): Int{
        if(str.isNotBlank()){
            val pattern = Pattern.compile("[^0-9]")
            val matcher: Matcher = pattern.matcher(str)
            return matcher.replaceAll("").toInt()
        }
        else return -1
    }


}