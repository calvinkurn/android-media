package com.rahullohra.fakeresponse.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RestResponse")
data class RestResponse(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val response: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val enabled: Boolean,
    val url: String,
    val httpMethod: String
)