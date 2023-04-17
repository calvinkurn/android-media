package com.tokopedia.loginHelper.domain.repository

import com.tokopedia.loginHelper.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserLocalRepository {

    fun getUsers(): Flow<List<User>>

    suspend fun insertUser(user: User)
}
