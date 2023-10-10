package com.tokopedia.universal_sharing.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.universal_sharing.model.ImagePolicy
import com.tokopedia.universal_sharing.model.ImagePolicyResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ImagePolicyUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : CoroutineUseCase<String, ImagePolicyResponse>(Dispatchers.IO) {
    override fun graphqlQuery(): String {
        return """
              query imagenerator_policy(${'$'}sourceID:String!) {
                  imagenerator_policy(sourceID: ${'$'}sourceID) {
                    args {
                        key
                        type
                        required
                    }
                  }
              }
        """.trimIndent()
    }

    override suspend fun execute(params: String): ImagePolicyResponse {
        return graphqlRepository.request(graphqlQuery(), mapOf("sourceID" to params))
    }
}
