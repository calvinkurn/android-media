package com.tokopedia.kyc_centralized.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.data.model.KycUserProjectInfoPojo
import javax.inject.Inject

class GetProjectInfoUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Map<String, Int>, KycUserProjectInfoPojo>(dispatchers.io) {

    override suspend fun execute(params: Map<String, Int>): KycUserProjectInfoPojo {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query get_project_info(${'$'}projectId: Int!) {
            kycProjectInfo(projectID: ${'$'}projectId) {
                Status
                StatusName
                Message
                IsAllowToRegister
                Reason
                TypeList {
                  TypeID
                  Status
                  StatusName
                  IsAllowToUpload
                }
                IsSelfie
            }
        }
    """.trimIndent()
}
