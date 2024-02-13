package com.tokopedia.profilecompletion.settingprofile.profileinfo.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.settingprofile.profileinfo.data.ProfileRoleResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProfileRoleUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, ProfileRoleResponse>(dispatchers.io) {

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
