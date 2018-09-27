package com.tokopedia.flashsale.management.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory

class CampaignViewModel() : Visitable<CampaignAdapterTypeFactory>, Parcelable {

    var name: String = ""
    var campaign_period: String = ""
    var status: String = ""
    var campaign_type: String = ""
    var cover: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        campaign_period = parcel.readString()
        status = parcel.readString()
        campaign_type = parcel.readString()
        cover = parcel.readString()
    }

    override fun type(typeFactory: CampaignAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(campaign_period)
        parcel.writeString(status)
        parcel.writeString(campaign_type)
        parcel.writeString(cover)
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