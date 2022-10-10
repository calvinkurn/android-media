package com.tokopedia.entertainment.pdp.data.pdp.mapper

import android.content.ClipData
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.pdp.*
import com.tokopedia.kotlin.extensions.view.toIntSafely

object EventVerifyMapper {
    fun getInitialVerify(pdpData: ProductDetailData): VerifyRequest {
        return VerifyRequest(
                book = true,
                checkout = false,
                cartdata = CartData(
                        metadata = MetaData(
                                productIds = listOf(pdpData.id),
                                providerIds = listOf(pdpData.providerId),
                                productNames = listOf(pdpData.displayName),
                                categoryName = "event"
                        )
                )
        )
    }

    fun getItemMap(packageItem: PackageItem, pdpData: ProductDetailData, quantiy : Int,
                   totalPrice:Int, selectedDate: String, packageId:String, packageName:String):ItemMap{
        val locationDesc = pdpData.outlets.firstOrNull()?.district ?: ""
        val locationName= pdpData.outlets.firstOrNull()?.name ?: ""
        packageItem.apply {
            return ItemMap(
                    id = id,
                    packageId = packageId,
                    name = name,
                    productId = productId,
                    productName = pdpData.displayName,
                    providerId = pdpData.providerId,
                    categoryId = pdpData.categoryId,
                    startTime = pdpData.saleStartTime,
                    endTime = pdpData.saleEndDate,
                    price = salesPrice.toIntSafely().toLong(),
                    quantity = quantiy,
                    totalPrice = totalPrice.toLong(),
                    locationName = locationName,
                    locationDesc = locationDesc,
                    packageName = packageName,
                    productAppUrl = pdpData.appUrl,
                    webAppUrl = pdpData.webUrl,
                    productImage = pdpData.imageApp,
                    scheduleTimestamp = selectedDate
            )
        }
    }

    fun getTotalPrice(hashMap: HashMap<String, ItemMap>): Long {
        var totalPrice = 0L
        for (item in hashMap){
            totalPrice += item.value.totalPrice
        }

        return totalPrice
    }

    fun getListItemMap(hashItemMap: HashMap<String, ItemMap>): List<ItemMap>{
        val list = mutableListOf<ItemMap>()
        for (item in hashItemMap){
            list.add(item.value)
        }
        return list
    }

    fun getItemIds(hashItemMap: HashMap<String, ItemMap>): List<String>{
        val list = mutableListOf<String>()
        for (item in hashItemMap){
            list.add(item.value.id)
        }
        return list
    }

    fun getTotalQuantity(hashItemMap: HashMap<String, ItemMap>): Int{
        var quantity = 0
        for (item in hashItemMap){
            quantity += item.value.quantity
        }
        return quantity
    }


}