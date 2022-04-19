package com.tokopedia.home_account.linkaccount.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 26/08/21.
 */

class GetUserProfile @Inject constructor(@ApplicationContext private val repository: GraphqlRepository)
    : CoroutineUseCase<Unit, ProfilePojo>(Dispatchers.IO) {

    override suspend fun execute(params: Unit): ProfilePojo {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
            query profile() {
              profile() {
                phone
              }
            }
        """.trimIndent()
    }