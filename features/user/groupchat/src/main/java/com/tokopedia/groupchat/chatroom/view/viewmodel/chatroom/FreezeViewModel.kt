package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 26/02/19
 */
@Parcelize
data class FreezeViewModel (
        @SerializedName("category")
        @Expose
        var freezeCategory: String = "",
        @SerializedName("title")
        @Expose
        var freezeTitle: String = "",
        @SerializedName("desc")
        @Expose
        var freezeDesc: String = "",
        @SerializedName("btn_title")
        @Expose
        var freezeButtonTitle: String = "",
        @SerializedName("btn_app_link")
        @Expose
        var freezeButtonUrl: String = ""
) : Parcelable