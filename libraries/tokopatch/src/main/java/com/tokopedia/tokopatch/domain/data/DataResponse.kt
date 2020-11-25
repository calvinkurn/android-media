package com.tokopedia.tokopatch.domain.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DataResponse(
        @SerializedName("code")
        val code: Int = 0,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("result")
        val result: List<Result> = listOf()
) {
    @Entity(tableName = "result")
    data class Result(
            @PrimaryKey var uid: Int,
            @SerializedName("description")
            @ColumnInfo(name = "description")
            val description: String = "",
            @SerializedName("version")
            @ColumnInfo(name = "version")
            val versionName: String = "",
            @SerializedName("version_code")
            @ColumnInfo(name = "version_code")
            val versionCode: String?,
            @SerializedName("signature")
            @ColumnInfo(name = "signature")
            val signature: String = "",
            @SerializedName("data")
            @ColumnInfo(name = "data")
            val `data`: String = ""
    )
}