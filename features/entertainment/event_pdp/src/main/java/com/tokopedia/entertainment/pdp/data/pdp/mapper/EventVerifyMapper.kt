package com.tokopedia.entertainment.pdp.data.pdp.mapper

import android.content.ClipData
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.pdp.*

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

    fun getItemMap(packageItem: PackageItem, pdpData: ProductDetailData, quantiy : Int, totalPrice:Int, selectedDate: String):ItemMap{
        packageItem.apply {
            return ItemMap(
                    id = id,
                    name = name,
                    productId = productId,
                    productName = pdpData.displayName,
                    providerId = pdpData.providerId,
                    categoryId = pdpData.categoryId,
                    startTime = pdpData.saleStartTime,
                    endTime = pdpData.saleEndDate,
                    price = salesPrice.toInt(),
                    quantity = quantiy,
                    totalPrice = totalPrice,
                    locationName = pdpData.location,
                    productAppUrl = pdpData.appUrl,
                    webAppUrl = pdpData.webUrl,
                    productImage = pdpData.imageApp,
                    scheduleTimestamp = selectedDate
            )
        }
    }

    fun getTotalPrice(hashMap: HashMap<String, ItemMap>): Int{
        var totalPrice = 0
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