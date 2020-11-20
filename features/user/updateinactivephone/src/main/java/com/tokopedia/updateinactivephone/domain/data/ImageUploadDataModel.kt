package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageUploadDataModel(
        @Expose @SerializedName("status")
        var status: String = "",
        @Expose @SerializedName("error_messages")
        var errors: MutableList<String> = mutableListOf(),
        @Expose @SerializedName("data")
        var data: PictureObjectDataModel = PictureObjectDataModel(),
        var source: String = ""
) {
    data class PictureObjectDataModel(
            @Expose @SerializedName("picture_obj")
            var pictureObject: String = ""
    )
}

data class ImageUploadParamDataModel(
        @Expose @SerializedName("email")
        var email: String = "",
        @Expose @SerializedName("oldMsisdn")
        var phoneNumber: String = "",
        @Expose @SerializedName("index")
        var index: Int = 0
)
