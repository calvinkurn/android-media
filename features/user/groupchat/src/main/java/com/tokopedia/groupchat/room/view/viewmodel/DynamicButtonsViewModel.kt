package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 22/02/19.
 */

class DynamicButtonsViewModel constructor(var floatingButton: Button = Button(),
                                          var listDynamicButton: ArrayList<Button> = ArrayList())
    : Visitable<Any>, Parcelable {

    constructor(`in`: Parcel) : this() {
        this.floatingButton = `in`.readParcelable(Button::class.java.classLoader)
        this.listDynamicButton = `in`.createTypedArrayList(Button.CREATOR)
    }

    class Button(var buttonId : String = "",
                 var imageUrl: String = "",
                 var linkUrl: String = "",
                 var contentType: String = "",
                 var contentText: String = "",
                 var contentButtonText: String = "",
                 var contentLinkUrl: String = "",
                 var contentImageUrl: String = "",
                 var hasNotification: Boolean = false,
                 var tooltip: String = "") : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readByte() != 0.toByte(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(buttonId)
            parcel.writeString(imageUrl)
            parcel.writeString(linkUrl)
            parcel.writeString(contentType)
            parcel.writeString(contentText)
            parcel.writeString(contentButtonText)
            parcel.writeString(contentLinkUrl)
            parcel.writeString(contentImageUrl)
            parcel.writeByte(if (hasNotification) 1 else 0)
            parcel.writeString(tooltip)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Button> {
            override fun createFromParcel(parcel: Parcel): Button {
                return Button(parcel)
            }

            override fun newArray(size: Int): Array<Button?> {
                return arrayOfNulls(size)
            }
        }


    }

    companion object {

        const val TYPE = "dynamic_button"
        const val TYPE_REDIRECT_EXTERNAL = "external"
        const val TYPE_OVERLAY_CTA = "overlay_cta"
        const val TYPE_OVERLAY_WEBVIEW = "overlay_webview"

        @JvmField
        val CREATOR: Parcelable.Creator<DynamicButtonsViewModel> = object : Parcelable.Creator<DynamicButtonsViewModel> {
            override fun createFromParcel(parcel: Parcel): DynamicButtonsViewModel {
                return DynamicButtonsViewModel(parcel)
            }

            override fun newArray(size: Int): Array<DynamicButtonsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun type(typeFactory: Any): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(this.floatingButton, flags)
        dest.writeTypedList(listDynamicButton)
    }

    override fun describeContents(): Int {
        return 0
    }

}
