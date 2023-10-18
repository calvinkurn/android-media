package com.tokopedia.favorite.data.mapper

import android.content.Context
import com.tokopedia.favorite.R
import com.tokopedia.favorite.domain.model.*
import com.tokopedia.network.data.model.response.GraphqlResponse
import retrofit2.Response
import rx.functions.Func1
import java.util.*

/**
 * Created by naveengoyal on 5/7/18.
 */
class FavoritShopGraphQlMapper(
        context: Context
) : Func1<Response<GraphqlResponse<FavoritShopResponseData>>, FavoriteShop> {

    private val defaultErrorMessage: String = context.getString(R.string.msg_network_error)
    private val emptyErrorMessage: String = context.getString(R.string.msg_empty_wishlist)
    private val successMessage: String = "success get get favorite shop"

    private fun validateResponse(responseBody: FavoritShopResponseData?): FavoriteShop {
        return if (responseBody != null && responseBody.data != null) {
            mappingValidResponse(responseBody.data!!)
        } else {
            invalidResponse(defaultErrorMessage)
        }
    }

    private fun mappingValidResponse(shopItemDataResponse: FavShopItemData): FavoriteShop {
        return if (shopItemDataResponse.list != null
                && shopItemDataResponse.list!!.isNotEmpty()) {
            val favoriteShop = FavoriteShop()
            favoriteShop.isDataValid = true
            favoriteShop.message = successMessage

            val list = shopItemDataResponse.list
            favoriteShop.data = if (list == null) null else mappingDataShopItem(list)

            favoriteShop.pagingModel = shopItemDataResponse.pagingHandlerModel
            favoriteShop
        } else {
            invalidResponse(emptyErrorMessage)
        }
    }

    private fun mappingDataShopItem(listShopItemResponse: List<FavShopsItem>): List<FavoriteShopItem> {
        val favoriteShopItems = ArrayList<FavoriteShopItem>()
        for (shopItem in listShopItemResponse) {
            val favoriteShopItem = FavoriteShopItem()
            favoriteShopItem.iconUri = shopItem.image
            favoriteShopItem.id = shopItem.id
            favoriteShopItem.location = shopItem.location
            favoriteShopItem.name = shopItem.name
            favoriteShopItem.badgeUrl = getShopBadgeUrl(shopItem.badge)
            favoriteShopItems.add(favoriteShopItem)
        }
        return favoriteShopItems
    }

    private fun getShopBadgeUrl(shopBadge: List<Badge?>?): String? {
        return if (shopBadge != null
                && shopBadge.isNotEmpty()
                && shopBadge[0] != null
                && shopBadge[0]!!.imageUrl != null
                && shopBadge[0]!!.imageUrl!!.isNotEmpty()) {
            shopBadge[0]!!.imageUrl
        } else ""
    }

    private fun invalidResponse(defaultErrorMessage: String): FavoriteShop {
        val favoriteShop = FavoriteShop()
        favoriteShop.isDataValid = false
        favoriteShop.message = defaultErrorMessage
        return favoriteShop
    }

    override fun call(response: Response<GraphqlResponse<FavoritShopResponseData>>?): FavoriteShop {
        return if (response != null
                && response.isSuccessful
                && response.body() != null) {
            validateResponse(response.body()!!.data)
        } else invalidResponse(defaultErrorMessage)
    }

}
