package com.tokopedia.createpost.domain.usecase

import android.text.TextUtils
import com.tokopedia.createpost.data.pojo.productsuggestion.affiliate.AffiliateProductItem
import com.tokopedia.createpost.data.pojo.productsuggestion.affiliate.AffiliateProductSuggestion
import com.tokopedia.createpost.data.raw.GQL_QUERY_AFFILIATE_PRODUCT_SUGGESTION
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 2019-09-17.
 */
class GetAffiliateProductSuggestionUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase)
    : UseCase<List<AffiliateProductItem>>() {

    override suspend fun executeOnBackground(): List<AffiliateProductItem> {
        val query = GQL_QUERY_AFFILIATE_PRODUCT_SUGGESTION
        val request = GraphqlRequest(query, AffiliateProductSuggestion::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)

        val graphqlResponse: GraphqlResponse = graphqlUseCase.executeOnBackground()

        val errors: MutableList<GraphqlError>? = graphqlResponse.getError(AffiliateProductSuggestion::class.java)
        if (!TextUtils.isEmpty(errors?.firstOrNull()?.message)) {
            throw MessageErrorException(errors?.first()?.message)
        }

        val response: AffiliateProductSuggestion = graphqlResponse.getData(AffiliateProductSuggestion::class.java)
        return response.affiliateParticularSections.explorePageSection.firstOrNull()?.items
                ?: arrayListOf()
    }
}