package com.tokopedia.topads.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel

/**
 * Author errysuprayogi on 01,November,2019
 */
open class CreateManualAdsStepperModel() : StepperModel {

    var groupId: String = ""
    var groupName: String = ""
    var selectedProductIds = mutableListOf<Int>()
    var selectedKeywords = mutableListOf<String>()
    var selectedSuggestBid = mutableListOf<Int>()
    var suggestedBidPerClick =0
    var maxBid =0
    var minBid =0
    var dailyBudget =0

    constructor(parcel: Parcel) : this() {
        groupId = parcel.readString()
        groupName = parcel.readString()
        suggestedBidPerClick = parcel.readInt()
        maxBid = parcel.readInt()
        minBid = parcel.readInt()
        dailyBudget = parcel.readInt()
        selectedProductIds = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        selectedKeywords = arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
        selectedSuggestBid = arrayListOf<Int>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupId)
        parcel.writeString(groupName)
        parcel.writeList(selectedProductIds)
        parcel.writeList(selectedKeywords)
        parcel.writeList(selectedSuggestBid)
        parcel.writeInt(suggestedBidPerClick)
        parcel.writeInt(maxBid)
        parcel.writeInt(minBid)
        parcel.writeInt(dailyBudget)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CreateManualAdsStepperModel> = object : Parcelable.Creator<CreateManualAdsStepperModel> {
            override fun createFromParcel(source: Parcel): CreateManualAdsStepperModel = CreateManualAdsStepperModel(source)
            override fun newArray(size: Int): Array<CreateManualAdsStepperModel?> = arrayOfNulls(size)
        }
    }


}