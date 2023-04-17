package com.tokopedia.loginHelper.data.repository

import com.tokopedia.loginHelper.data.local.UserDao
import com.tokopedia.loginHelper.domain.model.User
import com.tokopedia.loginHelper.domain.repository.UserLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserLocalRepository {

    override fun getUsers(): Flow<List<User>> {
        return dao.getUsers()
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }
}
