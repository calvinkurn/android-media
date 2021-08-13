package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/02/21.
 */

data class TokopointsDataModel(
        @SerializedName("tokopointsDrawerList") @Expose
        val data: TokopointsDrawerList = TokopointsDrawerList()
)

data class TokopointsDrawerList(
        @SerializedName("offFlag") @Expose
        val offFlag: Boolean = true,
        @SerializedName("drawerList") @Expose
        val drawerList: List<DrawerList> = arrayListOf()
)

data class DrawerList(
        @SerializedName("type") @Expose
        val type: String = "",
        @SerializedName("iconImageURL") @Expose
        val iconImageURL: String = "",
        @SerializedName("redirectURL") @Expose
        val redirectURL: String = "",
        @SerializedName("redirectAppLink") @Expose
        val redirectAppLink: String = "",
        @SerializedName("sectionContent") @Expose
        val sectionContent: List<SectionContent> = arrayListOf()
)

data class SectionContent(
        @SerializedName("type") @Expose
        val type: String = "",
        @SerializedName("textAttributes") @Expose
        val textAttributes: TextAttributes = TextAttributes(),
        @SerializedName("tagAttributes") @Expose
        val tagAttributes: TagAttributes = TagAttributes()
)

data class TextAttributes(
        @SerializedName("text") @Expose
        val text: String = "",
        @SerializedName("color") @Expose
        val color: String = "",
        @SerializedName("isBold") @Expose
        val isBold: Boolean = false
)

data class TagAttributes(
        @SerializedName("text") @Expose
        val text: String = "",
        @SerializedName("backgroundColor") @Expose
        val backgroundColor: String = ""
)