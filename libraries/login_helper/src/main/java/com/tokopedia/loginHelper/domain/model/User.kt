package com.tokopedia.loginHelper.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val email: String,
    val password: String,
    @PrimaryKey val id: Int? = null
)
