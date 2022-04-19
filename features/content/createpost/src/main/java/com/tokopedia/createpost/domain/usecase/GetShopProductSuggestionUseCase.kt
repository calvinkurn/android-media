package com.tokopedia.createpost.domain.usecase

import android.text.TextUtils
import com.tokopedia.createpost.data.pojo.productsuggestion.shop.ShopProductItem
import com.tokopedia.createpost.data.pojo.productsuggestion.shop.ShopProductSuggestionResponse
import com.tokopedia.createpost.data.raw.GQL_SHOP_PRODUCT_SUGGESTION
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 2019-08-29.
 */
class GetShopProductSuggestionUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase)
    : UseCase<List<ShopProductItem>>() {

    var params: HashMap<String, Any> = hashMapOf()

    companion object {
        private const val PARAM_SHOP_ID = "shopID"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(PARAM_SHOP_ID, shopId)
        }
    }

    override suspend fun executeOnBackground(): List<ShopProductItem> {
        val query = GQL_SHOP_PRODUCT_SUGGESTION
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