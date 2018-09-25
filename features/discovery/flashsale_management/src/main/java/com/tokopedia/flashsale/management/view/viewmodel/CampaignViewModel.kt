package com.tokopedia.flashsale.management.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flashsale.management.data.SellerInfo
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory

class CampaignViewModel() : Visitable<CampaignAdapterTypeFactory>, Parcelable {

    var campaign_id: Int = 0
    var name: String = ""
    var campaign_period: String = ""
    var submission_start_date: String = ""
    var submission_end_date: String = ""
    var status: String = ""
    var campaign_type: String = ""
    var cover: String = ""
    var is_joined: Boolean = false
    var dashboard_url: String = ""
    var product_number: Int = 0
    var seller_number: Int = 0
    var seller_info: SellerInfo = SellerInfo()

    constructor(parcel: Parcel) : this() {
        campaign_id = parcel.readInt()
        name = parcel.readString()
        campaign_period = parcel.readString()
        submission_start_date = parcel.readString()
        submission_end_date = parcel.readString()
        status = parcel.readString()
        campaign_type = parcel.readString()
        cover = parcel.readString()
        is_joined = parcel.readByte() != 0.toByte()
        dashboard_url = parcel.readString()
        product_number = parcel.readInt()
        seller_number = parcel.readInt()
    }

    override fun type(typeFactory: CampaignAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(campaign_id)
        parcel.writeString(name)
        parcel.writeString(campaign_period)
        parcel.writeString(submission_start_date)
        parcel.writeString(submission_end_date)
        parcel.writeString(status)
        parcel.writeString(campaign_type)
        parcel.writeString(cover)
        parcel.writeByte(if (is_joined) 1 else 0)
        parcel.writeString(dashboard_url)
        parcel.writeInt(product_number)
        parcel.writeInt(seller_number)
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