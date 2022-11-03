package com.tokopedia.categorylevels.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryGetDetailModularData(
    @SerializedName("categoryGetDetailModular")
    var categoryGetDetailModular: CategoryGetDetailModular
) {
    @Keep
    data class CategoryGetDetailModular(
        @SerializedName("header")
        var header: Header,
        @SerializedName("basicInfo")
        var basicInfo: BasicInfo,
        @SerializedName("components")
        var components: List<Component>
    ) {
        @Keep
        data class Header(
            @SerializedName("code")
            var code: Int,
            @SerializedName("message")
            var message: String
        )

        @Keep
        data class BasicInfo(
            @SerializedName("id")
            var id: Int,
            @SerializedName("name")
            var name: String,
            @SerializedName("tree")
            var tree: Int,
            @SerializedName("parent")
            var parent: Int,
            @SerializedName("rootId")
            var rootId: Int,
            @SerializedName("url")
            var url: String,
            @SerializedName("redirectionURL")
            var redirectionURL: String,
            @SerializedName("appRedirectionURL")
            var appRedirectionURL: String? = null,
            @SerializedName("applinks")
            var applinks: String,
            @SerializedName("redirectTo")
            var redirectTo: String,
            @SerializedName("iconImageURL")
            var iconImageURL: String,
            @SerializedName("hidden")
            var hidden: Int,
            @SerializedName("view")
            var view: Int,
            @SerializedName("intermediary")
            var intermediary: Int,
            @SerializedName("isAdult")
            var isAdult: Int,
            @SerializedName("isBanned")
            var isBanned: Int,
            @SerializedName("appRedirection")
            var appRedirection: String,
            @SerializedName("bannedMsgHeader")
            var bannedMsgHeader: String,
            @SerializedName("bannedMsg")
            var bannedMsg: String,
            @SerializedName("titleTag")
            var titleTag: String,
            @SerializedName("description")
            var description: String,
            @SerializedName("metaDescription")
            var metaDescription: String,
            @SerializedName("useDiscoPage")
            var useDiscoPage: Boolean
        )

        @Keep
        data class Component(
            @SerializedName("id")
            var id: Int,
            @SerializedName("name")
            var name: String,
            @SerializedName("type")
            var type: String,
            @SerializedName("targetID")
            var targetId: Int,
            @SerializedName("sticky")
            var sticky: Boolean,
            @SerializedName("properties")
            var properties: Properties,
            @SerializedName("data")
            var `data`: List<Data>
        ) {
            @Keep
            data class Data(
                @SerializedName("id")
                var id: Int ? = 0,
                @SerializedName("name")
                var name: String? = null,
                @SerializedName("url")
                var url: String? = null,
                @SerializedName("thumbnailImage")
                var thumbnailImage: String? = null,
                @SerializedName("isAdult")
                var isAdult: Int = 0,
                @SerializedName("applinks")
                var applinks: String? = null,
                @SerializedName("text")
                var text: String? = null,
                @SerializedName("categoryName")
                var categoryName: String? = null,
                @SerializedName("targetComponentId")
                var targetComponentId: String? = null,
            )
            @Keep
            data class Properties(
                @SerializedName("background")
                val background: String? = null,
                @SerializedName("dynamic")
                var dynamic: Boolean = false,
                @SerializedName("categoryDetail")
                val categoryDetail: Boolean = false,
                @SerializedName("backgroundImageURL")
                val backgroundImageURL: String? = null
            )
        }
    }
}
