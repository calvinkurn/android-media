package com.tokopedia.feedplus.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetContentWidgetRecommendationResponse(
    @SerializedName("contentWidgetRecommendation")
    val contentWidgetRecommendation: Entity = Entity()
) {

    data class Entity(
        @SerializedName("data")
        val data: List<Data> = emptyList()
    )

    data class Data(
        @SuppressLint("Invalid Data Type")
        @SerializedName("contentID")
        val contentID: ContentIdentifier = ContentIdentifier(),

        @SerializedName("__typename")
        val typename: String = "",

        @SerializedName("appLink")
        val appLink: String = "",

        @SerializedName("viewsFmt")
        val viewsFmt: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("author")
        val author: Author = Author(),

        @SerializedName("media")
        val media: Media = Media()
    )

    data class ContentIdentifier(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("origin")
        val origin: String = ""
    )

    data class Author(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("thumbnailURL")
        val thumbnailUrl: String = "",

        @SerializedName("appLink")
        val appLink: String = ""
    )

    data class Media(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("link")
        val link: String = "",

        @SerializedName("coverURL")
        val coverUrl: String = ""
    )

//    sealed interface Data {
//
//        data class WidgetAuthor(
//            @SerializedName("appLink")
//            val appLink: String = "",
//
//            @SerializedName("viewsFmt")
//            val viewsFmt: String = "",
//
//            @SerializedName("author")
//            val author: Author = Author()
//        ) : Data
//
//        data class WidgetBanner(
//            @SerializedName("title")
//            val title: String = "",
//
//            @SerializedName("appLink")
//            val appLink: String = "",
//        ) : Data
//
//        class Deserializer : JsonDeserializer<Data> {
//            override fun deserialize(
//                json: JsonElement?,
//                typeOfT: Type?,
//                context: JsonDeserializationContext
//            ): Data {
//                if (json?.isJsonObject != true) error("Json is not a json object")
//                val jsonObject = json.asJsonObject
//
//                val typename = jsonObject.getAsJsonPrimitive("__typename")
//                if (typename?.isString != true) error("Typename is null or not a string")
//
//                return when (val typeString = typename.asString) {
//                    "ContentWidgetAuthor" -> {
//                        context.deserialize<WidgetAuthor>(jsonObject, WidgetAuthor::class.java)
//                    }
//                    "ContentWidgetBanner" -> {
//                        context.deserialize<WidgetBanner>(jsonObject, WidgetBanner::class.java)
//                    }
//                    else -> {
//                        error("Type name $typeString is not supported")
//                    }
//                }
//            }
//        }
//    }
}

// internal fun GsonBuilder.registerGetContentWidgetRecommendationTypeAdapter(): GsonBuilder {
//    return registerTypeAdapter(
//        GetContentWidgetRecommendation::class.java,
//        GetContentWidgetRecommendation.Data.Deserializer()
//    )
// }
