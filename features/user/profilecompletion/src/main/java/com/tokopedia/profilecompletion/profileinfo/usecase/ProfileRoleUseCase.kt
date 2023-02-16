package com.tokopedia.profilecompletion.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.profileinfo.data.ProfileRoleResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProfileRoleUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, ProfileRoleResponse>(dispatcher) {

    override suspend fun execute(params: Unit): ProfileRoleResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String =
        """
       	query userProfileRole() {
	        userProfileRole {
		    isAllowedChangeDob
		    isAllowedChangeName
		    isAllowedChangeGender
		    chancesChangeName
		    chancesChangeGender
            changeNameMessageInfoTitle
            changeNameMessageInfo
            changeDobMessageInfo
            changeDobMessageInfoTitle
	        }
        }""".trimIndent()
}
