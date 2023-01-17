package com.tokopedia.favorite.data.mapper

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.favorite.R
import com.tokopedia.favorite.domain.model.TopAdsHome
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.domain.model.TopAdsShopItem
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * @author Kulomady on 1/19/17.
 */
class TopAdsShopMapper(
        context: Context,
        private val gson: Gson
) : Func1<Response<String?>?, TopAdsShop> {

    private val defaultErrorMessage: String = context.getString(R.string.msg_network_error)
    private val emptyErrorMessage: String = context.getString(R.string.msg_empty_wishlist)

    override fun call(response: Response<String?>?): TopAdsShop {
        return if (response != null
                && response.isSuccessful
                && response.body() != null) {
            validateResponse(response.body())
        } else invalidResponse(defaultErrorMessage)
    }

    private fun validateResponse(responseBody: String?): TopAdsShop {
        val topAdsResponse = gson.fromJson(responseBody, TopAdsHome::class.java)
        return if (topAdsResponse != null) {
            if (topAdsResponse.data != null) {
                mappingValidResponse(topAdsResponse.data)
            } else {
                invalidResponse(defaultErrorMessage)
            }
        } else {
            invalidResponse(defaultErrorMessage)
        }
    }

    private fun mappingValidResponse(topAdsDataResponse: List<TopAdsHome.Data>?): TopAdsShop {
        return if (topAdsDataResponse != null && topAdsDataResponse.isNotEmpty()) {
            val favoriteShop = TopAdsShop()
            favoriteShop.isDataValid = true
            favoriteShop.topAdsShopItemList = mappingDataShopItem(topAdsDataResponse)
            favoriteShop
        } else {
            invalidResponse(emptyErrorMessage)
        }
    }

    private fun mappingDataShopItem(listShopItemResponse: List<TopAdsHome.Data>): List<TopAdsShopItem> {
        val topAdsShopItems = ArrayList<TopAdsShopItem>()
        for (dataResponse in listShopItemResponse) {
            val topAdsShopItem = TopAdsShopItem()
            topAdsShopItem.adRefKey = dataResponse.adRefKey
            topAdsShopItem.redirect = dataResponse.redirect
            topAdsShopItem.id = dataResponse.id
            topAdsShopItem.shopClickUrl = dataResponse.adClickUrl
            mappingShopResponse(topAdsShopItem, dataResponse.headline?.shop)
            topAdsShopItem.shopImageCover = dataResponse.headline?.shop?.imageShop?.cover
            topAdsShopItem.shopImageCoverEcs = dataResponse.headline?.shop?.imageShop?.coverEcs
            topAdsShopItem.shopImageUrl = dataResponse.headline?.image?.fullUrl
            topAdsShopItem.shopImageEcs = dataResponse.headline?.shop?.imageShop?.sEcs
            topAdsShopItem.imageUrl = dataResponse.headline?.shop?.product?.firstOrNull()?.imageProduct?.imageUrl
            topAdsShopItem.fullEcs = dataResponse.headline?.image?.fullEcs
            topAdsShopItem.shopIsOfficial = dataResponse.headline?.shop?.shopIsOfficial?: false
            topAdsShopItem.isPMPro = dataResponse.headline?.shop?.isPMPro?: false
            topAdsShopItem.isPowerMerchant = dataResponse.headline?.shop?.isPowerMerchant?: false
            topAdsShopItem.imageShop = dataResponse.headline?.shop?.imageShop
            topAdsShopItem.layout = dataResponse.headline?.layout
            topAdsShopItem.applink = dataResponse.applinks
            topAdsShopItem.isFollowed = dataResponse.headline?.shop?.is_followed ?: false
            topAdsShopItems.add(topAdsShopItem)
        }
        return topAdsShopItems
    }

    private fun mappingShopResponse(topAdsShopItem: TopAdsShopItem, shopResponse: TopAdsHome.Shop?) {
        topAdsShopItem.shopId = shopResponse?.id
        topAdsShopItem.shopLocation = shopResponse?.location
        topAdsShopItem.shopName = shopResponse?.name
    }

    private fun invalidResponse(defaultErrorMessage: String): TopAdsShop {
        val topAdsShop = TopAdsShop()
        topAdsShop.isDataValid = false
        topAdsShop.message = defaultErrorMessage
        return topAdsShop
    }

}
