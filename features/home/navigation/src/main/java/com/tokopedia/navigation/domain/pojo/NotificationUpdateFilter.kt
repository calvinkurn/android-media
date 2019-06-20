package com.tokopedia.navigation.domain.pojo

/**
 * @author : Steven 11/04/19
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationUpdateFilter(
        @Expose
        @SerializedName("notifcenter_filter")
        var pojo: NotifCenterFilterPojo = NotifCenterFilterPojo()
)

data class NotifCenterFilterPojo(
        @Expose
        @SerializedName("notifcenter_typeList")
        var typeList: NotifFilterTypeList = NotifFilterTypeList(),

        @Expose
        @SerializedName("notifcenter_tagList")
        var tagList: NotifFilterTagList = NotifFilterTagList()
)

data class NotifFilterTypeList(
        @Expose
        @SerializedName("list")
        var list: List<NotifFilterTypeItem> = arrayListOf()
)

data class NotifFilterTagList(
        @Expose
        @SerializedName("list")
        var list: List<NotifFilterTagItem> = arrayListOf()
)

data class NotifFilterTypeItem(
        @Expose
        @SerializedName("id")
        var id: String = "",
        @Expose
        @SerializedName("name")
        var name: String = ""
)

data class NotifFilterTagItem(
        @Expose
        @SerializedName("tag_id")
        var tagId: String = "",
        @Expose
        @SerializedName("tag_name")
        var tagName: String = "",
        @Expose
        @SerializedName("tag_key")
        var tagKey: String = ""
)