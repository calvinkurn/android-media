package com.tokopedia.favorite.data.mapper

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.favorite.R
import com.tokopedia.favorite.domain.model.*
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * @author Kulomady on 1/19/17.
 */
class FavoriteShopMapper(
        context: Context,
        private val gson: Gson
) : Func1<Response<String?>?, FavoriteShop> {

    companion object {
        const val IS_SHOP_FAVORITE = "1"
    }

    private val defaultErrorMessage: String = context.getString(R.string.msg_network_error)
    private val emptyErrorMessage: String = context.getString(R.string.msg_empty_wishlist)
    private val successMessage: String = "success get get favorite shop"

    override fun call(response: Response<String?>?): FavoriteShop {
        return if (response != null &&
                response.isSuccessful &&
                response.body() != null) {
            validateResponse(response.body())
        } else invalidResponse(defaultErrorMessage)
    }

    private fun validateResponse(responseBody: String?): FavoriteShop {
        val favoriteShopsResponse = gson.fromJson(responseBody, TopAdsData::class.java)
        return if (favoriteShopsResponse != null) {
            if (favoriteShopsResponse.messageError != null
                    && favoriteShopsResponse.messageError.size > 0) {
                val errorMessage = favoriteShopsResponse.messageError[0]
                invalidResponse(errorMessage)
            } else if (favoriteShopsResponse.data != null) {
                mappingValidResponse(favoriteShopsResponse.data)
            } else {
                invalidResponse(defaultErrorMessage)
            }
        } else {
            invalidResponse(defaultErrorMessage)
        }
    }

    private fun mappingValidResponse(shopItemDataResponse: ShopItemData): FavoriteShop {
        return if (shopItemDataResponse.list != null &&
                shopItemDataResponse.list.size > 0) {
            val favoriteShop = FavoriteShop()
            favoriteShop.setDataIsValid(true)
            favoriteShop.message = successMessage
            favoriteShop.data = mappingDataShopItem(shopItemDataResponse.list)
            favoriteShop.pagingModel = shopItemDataResponse.pagingHandlerModel
            favoriteShop
        } else {
            invalidResponse(emptyErrorMessage)
        }
    }

    private fun mappingDataShopItem(listShopItemResponse: List<ShopItem>): List<FavoriteShopItem> {
        val favoriteShopItems = ArrayList<FavoriteShopItem>()
        for (shopItem in listShopItemResponse) {
            val favoriteShopItem = FavoriteShopItem()
            favoriteShopItem.adKey = shopItem.adKey
            favoriteShopItem.adR = shopItem.adR
            favoriteShopItem.coverUri = shopItem.coverUri
            favoriteShopItem.iconUri = shopItem.iconUri
            favoriteShopItem.id = shopItem.id
            favoriteShopItem.setIsFav(IS_SHOP_FAVORITE == shopItem.isFav)
            favoriteShopItem.location = shopItem.location
            favoriteShopItem.shopClickUrl = shopItem.shopClickUrl
            favoriteShopItem.name = shopItem.name
            favoriteShopItem.badgeUrl = getShopBadgeUrl(shopItem.shopBadge)
            favoriteShopItems.add(favoriteShopItem)
        }
        return favoriteShopItems
    }

    private fun getShopBadgeUrl(shopBadge: List<Badge?>?): String {
        val hasBadgeUrl = shopBadge != null
                && shopBadge.isNotEmpty()
                && shopBadge[0] != null
                && shopBadge[0]?.imageUrl != null
                && (shopBadge[0]?.imageUrl?.isNotEmpty())?: false

        return if (hasBadgeUrl) {
            (shopBadge?.get(0)?.imageUrl) ?: ""
        } else ""
    }

    private fun invalidResponse(defaultErrorMessage: String): FavoriteShop {
        val favoriteShop = FavoriteShop()
        favoriteShop.setDataIsValid(false)
        favoriteShop.message = defaultErrorMessage
        return favoriteShop
    }

}
