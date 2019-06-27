package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by nisie on 22/02/19.
 */

class DynamicButtonsViewModel constructor(var floatingButton: DynamicButton = DynamicButton(),
                                          var listDynamicButton: ArrayList<DynamicButton> = ArrayList(),
                                          var interactiveButton: InteractiveButton = InteractiveButton())
    : Visitable<Any>, Parcelable {

    constructor(`in`: Parcel) : this() {
        this.floatingButton = `in`.readParcelable(DynamicButton::class.java.classLoader)
        this.listDynamicButton = `in`.createTypedArrayList(DynamicButton.CREATOR)
        this.interactiveButton = `in`.readParcelable(InteractiveButton::class.java.classLoader)
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
        dest.writeParcelable(this.interactiveButton, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

}
