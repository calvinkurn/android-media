package com.tokopedia.fakeresponse.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.data.models.ResponseListData
import java.util.*

@Entity(tableName = "RestRecord")
data class RestRecord(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val response: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val enabled: Boolean,
    val url: String,
    var httpMethod: String,
    val customTag: String,
    val isResponseSuccess: Boolean
)

fun RestRecord.toResponseListData(): ResponseListData? {
    id?.let {
        return ResponseListData(
            id = it,
            title = url,
            isChecked = enabled,
            responseType = ResponseItemType.REST,
            customName = customTag,
            updatedAt = updatedAt,
            isResponseSuccess = isResponseSuccess
        )
    }
    return null
}

fun RestRecord.toRestRecord(): RestRecord {
    val date = Date()
    val currentTime = date.time
    return RestRecord(
        response = response,
        createdAt = currentTime,
        updatedAt = currentTime,
        enabled = true,
        url = url,
        httpMethod = httpMethod,
        customTag = customTag,
        isResponseSuccess = isResponseSuccess
    )
}