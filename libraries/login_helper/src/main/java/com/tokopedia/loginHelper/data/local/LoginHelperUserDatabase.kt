package com.tokopedia.loginHelper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tokopedia.loginHelper.domain.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class LoginHelperUserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "login_helper_users_dao"
    }
}
