package com.tokopedia.shop.home.domain

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_PAGE_HOME_LAYOUT
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

class GetShopPageHomeLayoutUseCase @Inject constructor(
        @Named(GQL_GET_SHOP_PAGE_HOME_LAYOUT)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopLayoutWidget>() {

    companion object {
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_STATUS = "status"
        private const val KEY_LAYOUT_ID = "layoutId"

        @JvmStatic
        fun createParams(
                shopId: String = "",
                status: String = "",
                layoutId: String = ""
        ) = mapOf<String, Any>(
                KEY_SHOP_ID to shopId,
                KEY_STATUS to status,
                KEY_LAYOUT_ID to layoutId
        )
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopLayoutWidget {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())

        val gqlRequest = GraphqlRequest(gqlQuery, ShopLayoutWidget.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopLayoutWidget.Response>(ShopLayoutWidget.Response::class.java)
                    .shopLayoutWidget
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}