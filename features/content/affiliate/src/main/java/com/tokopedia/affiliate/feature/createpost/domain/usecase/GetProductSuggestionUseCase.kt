package com.tokopedia.affiliate.feature.createpost.domain.usecase

import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion.ShopProductSuggestionResponse
import com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion.TagItem
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Named

/**
 * @author by milhamj on 2019-08-29.
 */
class GetProductSuggestionUseCase(
        @Named(QUERY_PRODUCT_SUGGESTION) private val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase)
    : UseCase<List<TagItem>>() {

    companion object {
        const val QUERY_PRODUCT_SUGGESTION = "query_af_shop_product_suggestion"
    }

    override suspend fun executeOnBackground(): List<TagItem> {

        val request = GraphqlRequest(query, ShopProductSuggestionResponse::class.java)

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