package com.tokopedia.sellerhomedrawer.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Action(): Parcelable {
    
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Action> = object : Parcelable.Creator<Action> {
            override fun createFromParcel(parcel: Parcel): Action {
                return Action(parcel)
            }

            override fun newArray(size: Int): Array<Action?> {
                return arrayOfNulls(size)
            }
        }
    }

    protected constructor(parcel: Parcel) : this() {
        mRedirectUrl = parcel.readString()
        mText = parcel.readString()
        mAppLinks = parcel.readString()
        mVisibility = parcel.readString()
    }

    @SerializedName("redirect_url")
    private var mRedirectUrl: String? = null
    @SerializedName("text")
    private var mText: String? = null
    @SerializedName("applinks")
    private var mAppLinks: String? = null
    @SerializedName("visibility")
    private var mVisibility: String? = null
    
    fun getRedirectUrl(): String {
        return mRedirectUrl?: ""
    }

    fun setRedirectUrl(redirect_url: String) {
        mRedirectUrl = redirect_url
    }

    fun getmText(): String? {
        return mText
    }

    fun setmText(mText: String) {
        this.mText = mText
    }

    fun getmAppLinks(): String? {
        return mAppLinks
    }

    fun setmAppLinks(mAppLinks: String) {
        this.mAppLinks = mAppLinks
    }

    fun getmVisibility(): String? {
        return mVisibility
    }

    fun setmVisibility(mVisibility: String) {
        this.mVisibility = mVisibility
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(mRedirectUrl)
        parcel.writeString(mText)
        parcel.writeString(mAppLinks)
        parcel.writeString(mVisibility)
    }
}