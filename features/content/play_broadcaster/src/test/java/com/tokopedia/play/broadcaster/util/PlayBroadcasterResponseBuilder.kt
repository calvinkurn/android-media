package com.tokopedia.play.broadcaster.util

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse

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
}