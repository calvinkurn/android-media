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

    class Button(var buttonType: String = "",
                 var imageUrl: String = "",
                 var linkUrl: String = "") : Parcelable {



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
