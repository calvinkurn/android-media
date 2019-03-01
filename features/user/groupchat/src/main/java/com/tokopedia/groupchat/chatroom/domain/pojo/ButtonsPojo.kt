package com.tokopedia.groupchat.chatroom.domain.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by StevenFredian .
 */

class ButtonsPojo() {

    @SerializedName("floating_button")
    @Expose
    var floatingButton: Button = Button()

    @SerializedName("dynamic_buttons")
    @Expose
    var listDynamicButton: List<Button> = arrayListOf()

    class Button() {

        @SerializedName("button_id")
        @Expose
        var buttonId: String = ""
        @SerializedName("button_type")
        @Expose
        var buttonType: String = ""
        @SerializedName("image_url")
        @Expose
        var imageUrl: String = ""
        @SerializedName("link_url")
        @Expose
        var linkUrl: String = ""
        @SerializedName("content_type")
        @Expose
        var contentType: String = ""
        @SerializedName("content_image_url")
        @Expose
        var contentImageUrl: String = ""
        @SerializedName("content_button_title")
        @Expose
        var contentButtonText: String = ""
        @SerializedName("content_text")
        @Expose
        var contentText: String = ""
        @SerializedName("content_link_url")
        @Expose
        var contentLinkUrl: String = ""
        @SerializedName("tooltip")
        @Expose
        var tooltip: String = ""
        @SerializedName("red_dot")
        @Expose
        var redDot: Boolean = false

    }

}

