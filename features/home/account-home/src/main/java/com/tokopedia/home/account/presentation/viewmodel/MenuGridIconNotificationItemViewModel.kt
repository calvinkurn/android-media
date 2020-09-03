package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

/**
 * Created by fwidjaja on 15/07/20.
 */

class MenuGridIconNotificationItemViewModel() : Parcelable {
    @DrawableRes
    private var resourceId = 0
    private var description: String = ""
    private var applink: String = ""
    private var count: Int = 0
    private var titleTrack : String = ""
    private var sectionTrack: String = ""

    constructor(parcel: Parcel) : this() {
        resourceId = parcel.readInt()
        description = parcel.readString().toString()
        applink = parcel.readString().toString()
        count = parcel.readInt()
        titleTrack = parcel.readString().toString()
        sectionTrack = parcel.readString().toString()
    }

    constructor(resourceId: Int, description: String, applink: String, count: Int,
                titleTrack: String, sectionTrack: String) : this() {
        this.resourceId = resourceId
        this.description = description
        this.applink = applink
        this.count = count
        this.titleTrack = titleTrack
        this.sectionTrack = sectionTrack
    }

    fun getResourceId(): Int {
        return resourceId
    }

    fun setResourceId(resourceId: Int) {
        this.resourceId = resourceId
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getApplink(): String {
        return applink
    }

    fun setApplink(applink: String) {
        this.applink = applink
    }

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getTitleTrack(): String {
        return titleTrack
    }

    fun setTitleTrack(titleTrack: String) {
        this.titleTrack = titleTrack
    }

    fun getSectionTrack(): String {
        return sectionTrack
    }

    fun setSectionTrack(sectionTrack: String) {
        this.sectionTrack = sectionTrack
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resourceId)
        parcel.writeString(description)
        parcel.writeString(applink)
        parcel.writeInt(count)
        parcel.writeString(titleTrack)
        parcel.writeString(sectionTrack)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuGridIconNotificationItemViewModel> {
        override fun createFromParcel(parcel: Parcel): MenuGridIconNotificationItemViewModel {
            return MenuGridIconNotificationItemViewModel(parcel)
        }

        override fun newArray(size: Int): Array<MenuGridIconNotificationItemViewModel?> {
            return arrayOfNulls(size)
        }
    }
}