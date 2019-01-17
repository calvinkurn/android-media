package com.tokopedia.flashsale.management.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory

class CampaignViewModel() : Visitable<CampaignAdapterTypeFactory>, Parcelable {

    var id: Long = -1
    var name: String = ""
    var campaignPeriod: String = ""
    var status: String = ""
    var campaignType: String? = ""
    var cover: String = ""
    var campaignUrl: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        campaignPeriod = parcel.readString()
        status = parcel.readString()
        campaignType = parcel.readString()
        cover = parcel.readString()
        campaignUrl = parcel.readString()
    }

    override fun type(typeFactory: CampaignAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(campaignPeriod)
        parcel.writeString(status)
        parcel.writeString(campaignType)
        parcel.writeString(cover)
        parcel.writeString(campaignUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CampaignViewModel> {
        override fun createFromParcel(parcel: Parcel): CampaignViewModel {
            return CampaignViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CampaignViewModel?> {
            return arrayOfNulls(size)
        }
    }
}