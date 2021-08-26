package com.tokopedia.home_account.linkaccount.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers

/**
 * Created by Yoris on 26/08/21.
 */

class GetUserProfile (private val repository: GraphqlRepository)
    : CoroutineUseCase<RequestParams, ProfilePojo>(Dispatchers.IO) {

    override suspend fun execute(params: RequestParams): ProfilePojo {
        return request(repository, params.parameters)
    }

    override fun graphqlQuery(): String = query

    companion object {
        val query = """
            query profile() {
              profile() {
                phone
              }
            }
        """.trimIndent()
    }
}