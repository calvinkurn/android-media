package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileRelatedQuery(
    @SerializedName("feedPostRelated")
    @Expose
    val feedPostRelated: FeedPostRelated = FeedPostRelated()
)

class FeedPostRelated {

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

    class Meta {
        @SerializedName("totalItems")
        @Expose
        var totalItems: Int? = null
    }

    data class Datum(
        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("content")
        @Expose
        var content: Content = Content()) {
        data class Content(
            @SerializedName("header")
            @Expose
            var header: Header = Header(),
            @SerializedName("body")
            @Expose
            var body: Body = Body(),
            @SerializedName("tracking")
            @Expose
            var tracking: Tracking = Tracking()) {
            data class Body(
                @SerializedName("media")
                @Expose
                var media: List<Medium>? = null,
                @SerializedName("caption")
                @Expose
                var caption: Caption? = null) {

                data class Medium(

                    @SerializedName("id")
                    @Expose
                    var id: String = "",
                    @SerializedName("type")
                    @Expose
                    var type: String = "",
                    @SerializedName("applink")
                    @Expose
                    var applink: String = "",
                    @SerializedName("thumbnail")
                    @Expose
                    var thumbnail: String = "",
                    @SerializedName("thumbnailLarge")
                    @Expose
                    var thumbnailLarge: String = ""

                )

                data class Caption(
                    @SerializedName("text")
                    @Expose
                    var text: String = "",
                    @SerializedName("applink")
                    @Expose
                    var applink: String = "")
            }

            data class Tracking(
                @SerializedName("authorID")
                @Expose
                var authorID: String = "")

            data class Header(
                @SerializedName("avatar")
                @Expose
                var avatar: String = "",
                @SerializedName("avatarTitle")
                @Expose
                var avatarTitle: String = "",
                @SerializedName("avatarBadge")
                @Expose
                var avatarBadge: String = "")

        }
    }
}








