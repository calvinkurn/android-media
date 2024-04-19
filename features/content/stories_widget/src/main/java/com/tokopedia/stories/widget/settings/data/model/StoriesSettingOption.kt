package com.tokopedia.stories.widget.settings.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 3/25/24
 */
data class StoriesSettingOption(
    @SerializedName("contentCreatorStoryGetAuthorOptions")
    val data: Response = Response()
) {
    data class Response(
        @SerializedName("options")
        val options: List<Options> = emptyList(),
        @SerializedName("config")
        val config: Config = Config(),
    )

    data class Options(
        @SerializedName("copy")
        val copy: String = "",
        @SerializedName("optionType")
        val optionType: String = "",
        @SerializedName("isDisabled")
        val isDisabled: Boolean = false,
    )

    data class Config(
        @SerializedName("copy")
        val copy: String = "",
        @SerializedName("weblink")
        val webLink: String = "",
        @SerializedName("applink")
        val appLink: String = "",
    )
}
