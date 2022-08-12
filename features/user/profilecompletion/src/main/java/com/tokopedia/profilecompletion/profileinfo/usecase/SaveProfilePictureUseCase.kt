package com.tokopedia.profilecompletion.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profilecompletion.data.SaveProfilePictureResponse

class SaveProfilePictureUseCase(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<Map<String, Any>, SaveProfilePictureResponse>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): SaveProfilePictureResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation save_profile_picture(${'$'}uploadID: String!) {
          saveProfilePicture(uploadID:${'$'}uploadID) {
            status
            errorMessage
            data {
                imageURL
                isSuccess
            }
          }
        }
    """.trimIndent()

    companion object {
        const val PARAM_UPLOAD_ID = "uploadID"
    }
}