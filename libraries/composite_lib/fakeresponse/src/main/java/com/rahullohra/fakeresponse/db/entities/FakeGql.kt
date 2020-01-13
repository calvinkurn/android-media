package com.rahullohra.fakeresponse.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FakeGql")
data class FakeGql(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val response: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val enabled: Boolean,
    val gqlOperationName: String,
    val javaQueryName: String?,
    val customTag:String?
)