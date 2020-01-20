package com.tokopedia.createpost.domain.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.createpost.data.pojo.productsuggestion.shop.ShopProductItem
import com.tokopedia.createpost.data.pojo.productsuggestion.shop.ShopProductSuggestionResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by milhamj on 2019-08-29.
 */
class GetShopProductSuggestionUseCase @Inject constructor(
        @Named(QUERY_SHOP_PRODUCT_SUGGESTION) private val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase)
    : UseCase<List<ShopProductItem>>() {

    var params: HashMap<String, Any> = hashMapOf()

    companion object {
        const val QUERY_SHOP_PRODUCT_SUGGESTION = "query_af_shop_product_suggestion"
        private const val PARAM_SHOP_ID = "shopID"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(PARAM_SHOP_ID, shopId)
        }
    }

    override suspend fun executeOnBackground(): List<ShopProductItem> {

        val request = GraphqlRequest(query, ShopProductSuggestionResponse::class.java, params)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)

        val graphqlResponse: GraphqlResponse = graphqlUseCase.executeOnBackground()

        val errors: MutableList<GraphqlError>? = graphqlResponse.getError(ShopProductSuggestionResponse::class.java)
        if (!TextUtils.isEmpty(errors?.firstOrNull()?.message)) {
            throw MessageErrorException(errors?.first()?.message)
        }

        val response: ShopProductSuggestionResponse = graphqlResponse.getData(ShopProductSuggestionResponse::class.java)
        if (!TextUtils.isEmpty(response.feedContentTagItems.error)) {
            throw MessageErrorException(response.feedContentTagItems.error)
        } else {
            return response.feedContentTagItems.tagItems
        }
    }
}