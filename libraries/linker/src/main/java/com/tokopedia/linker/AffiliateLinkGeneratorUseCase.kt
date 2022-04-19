package com.tokopedia.linker

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.linker.model.AffiliateGenerateLinkInput
import com.tokopedia.linker.model.GenerateAffiliateLink
import com.tokopedia.network.exception.MessageErrorException

class AffiliateLinkGeneratorUseCase constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<String>(graphqlRepository) {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, GenerateAffiliateLink.Response::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())

        val response = gqlResponse.getData<GenerateAffiliateLink.Response>(GenerateAffiliateLink.Response::class.java)
        if (!TextUtils.isEmpty(response.generateAffiliateLink.data?.get(0)?.uRL?.shortURL)) {
            return response.generateAffiliateLink.data?.get(0)?.uRL?.shortURL.toString()
        } else {
            throw MessageErrorException("Error in affiliate link generation")
        }
    }

    companion object {

        private const val INPUT = "input"

        const val QUERY = """mutation generateAffiliateLink(${'$'}input: GenerateLinkRequest!) {
                generateAffiliateLink(input: ${'$'}input) {
                Data {
                        Status 
                        Type
                        Error
                        Identifier
                        IdentifierType
                        LinkID
                    URL {
                        ShortURL
                        RegularURL
                        }
                    }
                }
            }
            """

        fun createParam(affiliateGenerateLinkInput: AffiliateGenerateLinkInput): HashMap<String, Any> {
            return hashMapOf(
                INPUT to affiliateGenerateLinkInput
            )
        }
    }
}
