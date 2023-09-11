package com.tokopedia.creation.common.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum

/**
 * Created By : Muhammad Furqan on 08/09/23
 */
data class ContentCreationResponse(
    @SerializedName("data")
    val data: ContentCreationDataEntity = ContentCreationDataEntity(),
    @SerializedName("error")
    val error: String = ""
) {
    class Response(
        @SerializedName("feedXHeader")
        val response: ContentCreationResponse = ContentCreationResponse()
    )
}

data class ContentCreationDataEntity(
    @SerializedName("creation")
    val creation: ContentCreationConfigEntity = ContentCreationConfigEntity()
)

data class ContentCreationConfigEntity(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("statistic")
    val statistic: ContentCreationStatisticEntity = ContentCreationStatisticEntity(),
    @SerializedName("authors")
    val authors: List<ContentCreationAuthorEntity> = emptyList()
)

data class ContentCreationStatisticEntity(
    @SerializedName("applink")
    val applink: String = ""
)

data class ContentCreationAuthorEntity(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("hasUsername")
    val hasUsername: Boolean = false,
    @SerializedName("hasAcceptTnC")
    val hasAcceptTnC: Boolean = false,
    @SerializedName("items")
    val items: List<ContentCreationAuthorItemEntity> = emptyList()
) {
    val typeEnum: ContentCreationAuthorEnum = ContentCreationAuthorEnum.getTypeByValue(this.type)

}

data class ContentCreationAuthorItemEntity(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("weblink")
    val weblink: String = "",
    @SerializedName("media")
    val media: ContentCreationMediaEntity = ContentCreationMediaEntity(),
    @SerializedName("descriptionCTA")
    val descriptionCTA: ContentCreationDescriptionEntity = ContentCreationDescriptionEntity()
) {

    val typeEnum: ContentCreationTypeEnum? = ContentCreationTypeEnum.getTypeByValue(this.type)

    data class ContentCreationMediaEntity(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("coverURL")
        val coverUrl: String = "",
        @SerializedName("mediaURL")
        val mediaUrl: String = ""
    )

    data class ContentCreationDescriptionEntity(
        @SerializedName("applink")
        val applink: String = ""
    )
}
