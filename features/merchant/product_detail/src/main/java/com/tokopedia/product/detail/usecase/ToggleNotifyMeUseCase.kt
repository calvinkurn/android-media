package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.upcoming.TeaserNotifyMe
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ToggleNotifyMeUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                private val graphqlRepository: GraphqlRepository) : UseCase<TeaserNotifyMe>() {

    var requestParams: RequestParams = RequestParams.EMPTY

    fun createParams(campaignId: Long, productId: Long, action: String, source: String) {
        val requestParams = RequestParams()
        requestParams.putLong(ProductDetailCommonConstant.PARAM_TEASER_CAMPAIGN_ID, campaignId)
        requestParams.putLong(ProductDetailCommonConstant.PARAM_TEASER_PRODUCT_ID, productId)
        requestParams.putString(ProductDetailCommonConstant.PARAM_TEASER_ACTION, action)
        requestParams.putString(ProductDetailCommonConstant.PARAM_TEASER_SOURCE, source)

        this.requestParams = requestParams
    }

    override suspend fun executeOnBackground(): TeaserNotifyMe {
        val request = GraphqlRequest(rawQueries[RawQueryKeyConstant.MUTATION_NOTIFY_ME],
                TeaserNotifyMe::class.java, requestParams.parameters)

        val gqlResponse = graphqlRepository.getReseponse(listOf(request))
        val result = gqlResponse.getData<TeaserNotifyMe>(TeaserNotifyMe::class.java)
        val errorResult = gqlResponse.getError(TeaserNotifyMe::class.java)

        if (result == null) {
            throw RuntimeException()
        } else if (errorResult != null && errorResult.isNotEmpty() && errorResult.first().message.isNotEmpty()) {
            throw MessageErrorException(errorResult.first().message)
        }

        return result
    }
}