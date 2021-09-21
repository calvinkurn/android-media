package com.tokopedia.play.broadcaster.util

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created by jegul on 11/05/21
 */
class PlayBroadcasterResponseBuilder {

    private val gson = Gson()

    fun buildRecommendedChannelTagsResponse(tags: List<String>): GetRecommendedChannelTagsResponse {
        return GetRecommendedChannelTagsResponse(
                recommendedTags = GetRecommendedChannelTagsResponse.GetRecommendedTags(
                        tags = tags
                )
        )
    }

    fun buildGetProductsInEtalaseResponse(idNameList: List<Pair<String, String>>): GetProductsByEtalaseResponse.GetProductListData {
        return GetProductsByEtalaseResponse.GetProductListData(
                data = idNameList.map { (id, name) ->
                    GetProductsByEtalaseResponse.Data(
                            id = id,
                            name = name
                    )
                }
        )
    }

    fun buildGetSelfEtalaseUseCaseResponse(idNameList: List<Pair<String, String>>): List<ShopEtalaseModel> {
        return idNameList.map { (id, name) ->
            ShopEtalaseModel(
                    id = id,
                    name = name,
                    type = 1
            )
        }
    }

    fun buildGetProductsInEtalaseUseCaseResponse(idNameList: List<Pair<String, String>>): GetProductsByEtalaseResponse.GetProductListData {
        return GetProductsByEtalaseResponse.GetProductListData(
                data = idNameList.map { (id, name) ->
                    GetProductsByEtalaseResponse.Data(
                            id = id,
                            name = name
                    )
                }
        )
    }
}