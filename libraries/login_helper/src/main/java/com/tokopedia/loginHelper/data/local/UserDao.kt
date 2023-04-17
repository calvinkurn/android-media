package com.tokopedia.loginHelper.data.local

import androidx.room.*
import com.tokopedia.loginHelper.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}
