package com.tokopedia.tokopedianow.category.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryComponentName.Companion.TABS_HORIZONTAL_SCROLL

data class GetCategoryLayoutResponse(
    @SerializedName("categoryGetDetailModular")
    val response: CategoryGetDetailModular = CategoryGetDetailModular()
) {

    data class CategoryGetDetailModular(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("basicInfo")
        val basicInfo: BasicInfo = BasicInfo(),
        @SerializedName("components")
        val components: List<Component> = emptyList()
    ) {
        fun getCategoryNameList(): List<String> {
            return components.find { it.type == TABS_HORIZONTAL_SCROLL }
                ?.getTabCategoryNameList()
                ?: emptyList()
        }
    }

    data class Header(
        @SerializedName("code")
        val code: Int = 0,
        @SerializedName("message")
        val message: String = ""
    )

    data class BasicInfo(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("tree")
        val tree: Int? = null,
        @SerializedName("parent")
        val parent: Int? = null,
        @SerializedName("rootId")
        val rootId: Int? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("redirectionURL")
        val redirectionURL: String? = null,
        @SerializedName("appRedirectionURL")
        val appRedirectionURL: String? = null,
        @SerializedName("applinks")
        val applinks: String = "",
        @SerializedName("redirectTo")
        val redirectTo: String? = null,
        @SerializedName("iconImageURL")
        val iconImageURL: String? = null,
        @SerializedName("hidden")
        val hidden: Int? = null,
        @SerializedName("view")
        val view: Int? = null,
        @SerializedName("intermediary")
        val intermediary: Int? = null,
        @SerializedName("isAdult")
        val isAdult: Int? = null,
        @SerializedName("isBanned")
        val isBanned: Int? = null,
        @SerializedName("appRedirection")
        val appRedirection: String? = null,
        @SerializedName("bannedMsgHeader")
        val bannedMsgHeader: String? = null,
        @SerializedName("bannedMsg")
        val bannedMsg: String? = null,
        @SerializedName("titleTag")
        val titleTag: String? = null,
        @SerializedName("description")
        val description: String? = null,
        @SerializedName("metaDescription")
        val metaDescription: String? = null,
        @SerializedName("useDiscoPage")
        val useDiscoPage: Boolean? = null,
        @SerializedName("discoIdentifier")
        val discoIdentifier: String? = null,
        @SerializedName("relatedCategory")
        val relatedCategory: List<RelatedCategory> = emptyList(),
        @SerializedName("longDescription")
        val longDescription: List<String> = emptyList()
    )

    data class RelatedCategory(
        @SerializedName("id")
        val id: Int? = null
    )

    data class Component(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("targetID")
        val targetID: Int? = null,
        @SerializedName("sticky")
        val sticky: Boolean? = null,
        @SerializedName("properties")
        val properties: Properties? = Properties(),
        @SerializedName("data")
        val data: List<ComponentData> = emptyList()
    ) {
        fun getTabCategoryNameList(): List<String> {
            return data.mapNotNull { it.categoryName }
        }
    }

    data class ComponentData(
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("text")
        val text: String? = null,
        @SerializedName("applink")
        val applink: String? = null,
        @SerializedName("applinks")
        val type: String? = null,
        @SerializedName("categoryName")
        val categoryName: String? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("isAdult")
        val isAdult: Int? = null,
        @SerializedName("targetComponentId")
        val targetComponentId: String? = null,
    )

    data class Properties(
        @SerializedName("background")
        val background: String? = null,
        @SerializedName("dynamic")
        val dynamic: Boolean? = null,
        @SerializedName("categoryDetail")
        val categoryDetail: Boolean? = null,
        @SerializedName("backgroundColor")
        val backgroundColor: String? = null,
        @SerializedName("backgroundTransparentImageURL")
        val backgroundTransparentImageURL: String? = null,
        @SerializedName("backgroundImageURL")
        val backgroundImageURL: String? = null
    )
}
