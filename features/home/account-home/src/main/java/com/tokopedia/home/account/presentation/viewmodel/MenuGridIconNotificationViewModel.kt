package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel

/**
 * Created by fwidjaja on 15/07/20.
 */
class MenuGridIconNotificationViewModel : ParcelableViewModel<AccountTypeFactory> {
    private var title: String? = null
    private var linkText: String? = null
    private var applinkUrl: String? = null
    private var items: List<MenuGridIconNotificationItemViewModel>? = null
    private var titleTrack // for tracking
            : String? = null
    private var sectionTrack: String? = null
    override fun type(typeFactory: AccountTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getLinkText(): String? {
        return linkText
    }

    fun setLinkText(linkText: String?) {
        this.linkText = linkText
    }

    fun getApplinkUrl(): String? {
        return applinkUrl
    }

    fun setApplinkUrl(applinkUrl: String?) {
        this.applinkUrl = applinkUrl
    }

    fun getItems(): List<MenuGridIconNotificationItemViewModel>? {
        return items
    }

    fun setItems(items: List<MenuGridIconNotificationItemViewModel>?) {
        this.items = items
    }

    fun getTitleTrack(): String? {
        return titleTrack
    }

    fun setTitleTrack(titleTrack: String?) {
        this.titleTrack = titleTrack
    }

    fun getSectionTrack(): String? {
        return sectionTrack
    }

    fun setSectionTrack(sectionTrack: String?) {
        this.sectionTrack = sectionTrack
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(linkText)
        dest.writeString(applinkUrl)
        dest.writeTypedList(items)
        dest.writeString(titleTrack)
        dest.writeString(sectionTrack)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        linkText = `in`.readString()
        applinkUrl = `in`.readString()
        items = `in`.createTypedArrayList(MenuGridIconNotificationItemViewModel.CREATOR)
        titleTrack = `in`.readString()
        sectionTrack = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<MenuGridIconNotificationViewModel?> = object : Parcelable.Creator<MenuGridIconNotificationViewModel?> {
            override fun createFromParcel(source: Parcel): MenuGridIconNotificationViewModel? {
                return MenuGridIconNotificationViewModel(source)
            }

            override fun newArray(size: Int): Array<MenuGridIconNotificationViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}