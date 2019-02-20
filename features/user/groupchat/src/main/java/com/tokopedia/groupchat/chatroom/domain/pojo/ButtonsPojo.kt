package com.tokopedia.groupchat.chatroom.domain.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by StevenFredian .
 */

class ButtonsPojo() : Visitable<Any>, Parcelable {


    @SerializedName("floating_button")
    @Expose
    var floatingButton: Button? = null

    @SerializedName("dynamic_buttons")
    @Expose
    var listDynamicButton: List<Button>?= null

    constructor(`in`: Parcel) : this() {
        this.floatingButton = `in`.readParcelable(Button::class.java.classLoader)
        this.listDynamicButton = `in`.createTypedArrayList(Button.CREATOR)
    }

    class Button() : Parcelable {

        @SerializedName("button_type")
        @Expose
        var buttonType: String = ""
        @SerializedName("image_url")
        @Expose
        var imageUrl: String = ""
        @SerializedName("link_url")
        @Expose
        var linkUrl: String = ""

        constructor(parcel: Parcel) : this() {
            buttonType = parcel.readString()
            imageUrl = parcel.readString()
            linkUrl = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(buttonType)
            parcel.writeString(imageUrl)
            parcel.writeString(linkUrl)
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

        @JvmField
        val CREATOR: Parcelable.Creator<ButtonsPojo> = object : Parcelable.Creator<ButtonsPojo> {
            override fun createFromParcel(parcel: Parcel): ButtonsPojo {
                return ButtonsPojo(parcel)
            }

            override fun newArray(size: Int): Array<ButtonsPojo?> {
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

