package com.rahullohra.fakeresponse.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rahullohra.fakeresponse.ResponseItemType
import com.rahullohra.fakeresponse.ResponseListData

@Entity(tableName = "RestRecord")
data class RestRecord(
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        val response: String? = null,
        val createdAt: Long,
        val updatedAt: Long,
        val enabled: Boolean,
        val url: String,
        var httpMethod: String,
        val customTag: String?=null
)

fun RestRecord.toResponseListData(): ResponseListData? {
    id?.let {
        return ResponseListData(id = it, title = url, isChecked = enabled, responseType = ResponseItemType.REST, customName = customTag)
    }
    return null
}