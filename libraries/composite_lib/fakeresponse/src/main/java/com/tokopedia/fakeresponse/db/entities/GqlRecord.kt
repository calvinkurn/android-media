package com.tokopedia.fakeresponse.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.data.models.ResponseListData
import java.util.*

@Entity(tableName = "GqlRecord")
data class GqlRecord(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val response: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val enabled: Boolean,
    val gqlOperationName: String,
    val customTag: String,
    val isResponseSuccess: Boolean,
    val isDelayResponse: Boolean
)



fun GqlRecord.toResponseListData(): ResponseListData? {
    id?.let {
        return ResponseListData(
            id = it,
            title = gqlOperationName,
            isChecked = enabled,
            responseType = ResponseItemType.GQL,
            customName = customTag,
            updatedAt = updatedAt,
            isResponseSuccess = isResponseSuccess
        )
    }
    return null
}

fun GqlRecord.toGqlRecord(): GqlRecord {
    val date = Date()
    val currentTime = date.time
    return GqlRecord(
        response = response,
        createdAt = currentTime,
        updatedAt = currentTime,
        enabled = true,
        gqlOperationName = gqlOperationName,
        customTag = customTag,
        isResponseSuccess = true,
        isDelayResponse = true
    )
}
