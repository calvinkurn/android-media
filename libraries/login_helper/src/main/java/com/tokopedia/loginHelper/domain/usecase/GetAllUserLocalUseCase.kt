package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.domain.model.User
import com.tokopedia.loginHelper.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUserLocalUseCase @Inject constructor(
    private val repository: UserLocalRepository
) {

    operator fun invoke(): Flow<List<User>> {
        return repository.getUsers().map { userList ->
            userList.sortedBy { it.email.lowercase() }
        }
    }
}
