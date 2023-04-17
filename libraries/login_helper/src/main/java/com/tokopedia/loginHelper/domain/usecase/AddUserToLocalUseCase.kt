package com.tokopedia.loginHelper.domain.usecase

import com.tokopedia.loginHelper.domain.model.User
import com.tokopedia.loginHelper.domain.repository.UserLocalRepository
import javax.inject.Inject

class AddUserToLocalUseCase @Inject constructor(
    private val repository: UserLocalRepository
) {
    suspend operator fun invoke(user: User) {
        repository.insertUser(user)
    }
}
