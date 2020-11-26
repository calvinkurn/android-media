package com.tokopedia.user_identification_common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.domain.pojo.RegisterIdentificationPojo
import javax.inject.Inject

class RegisterKycUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<RegisterIdentificationPojo>() {

    var params: HashMap<String, Any> = HashMap()

    override suspend fun executeOnBackground(): RegisterIdentificationPojo {
        val rawQuery = rawQueries[KYCConstant.QUERY_REGISTER_KYC]
        val gqlRequest = GraphqlRequest(rawQuery,
                RegisterIdentificationPojo::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(RegisterIdentificationPojo::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            return gqlResponse.getData(RegisterIdentificationPojo::class.java)
        }
    }

    companion object {
        private const val PROJECT_ID = "projectID"

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