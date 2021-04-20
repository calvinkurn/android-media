package com.tokopedia.kyc_centralized.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import javax.inject.Inject

class GetUserProjectInfoUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<KycUserProjectInfoPojo>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): KycUserProjectInfoPojo {
        val rawQuery = rawQueries[KYCConstant.QUERY_GET_KYC_PROJECT_INFO]
        val gqlRequest = GraphqlRequest(rawQuery,
                KycUserProjectInfoPojo::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(KycUserProjectInfoPojo::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(KycUserProjectInfoPojo::class.java)
        }
    }

    companion object {
        private const val PROJECT_ID = "projectId"

        fun createParam(projectId: Int): HashMap<String, Any> {
            var id = projectId
            if(id < 0) {
                id = KYCConstant.KYC_PROJECT_ID
            }
            return hashMapOf(
                    PROJECT_ID to id
            )
        }
    }
}