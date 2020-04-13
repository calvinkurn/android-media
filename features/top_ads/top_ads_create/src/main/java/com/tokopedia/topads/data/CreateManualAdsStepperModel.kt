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
    var manualKeywords = mutableListOf<String>()
    var selectedSuggestBid = mutableListOf<Int>()
    var minSuggestBidKeyword =0 // for keywords
    var suggestedBidPerClick = 0 // for Default
    var finalBidPerClick = -1 // Edited Bid by User
    var maxBid = 0
    var minBid = 0
    var dailyBudget = 0
    var adIds = mutableListOf<Int>()
    var adIdsPromo = mutableListOf<Int>()
    var adIdsNonPromo = mutableListOf<Int>()
    var selectedPromo = mutableListOf<Int>()
    var selectedNonPromo = mutableListOf<Int>()

    constructor(parcel: Parcel) : this() {
        groupId = parcel.readString()
        groupName = parcel.readString()
        suggestedBidPerClick = parcel.readInt()
        maxBid = parcel.readInt()
        minBid = parcel.readInt()
        finalBidPerClick = parcel.readInt()
        minSuggestBidKeyword = parcel.readInt()
        dailyBudget = parcel.readInt()
        selectedProductIds = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        adIds = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        adIdsPromo = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        adIdsNonPromo = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        selectedKeywords = arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
        manualKeywords = arrayListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
        selectedSuggestBid = arrayListOf<Int>().apply {
            parcel.readList(this, String::class.java.classLoader)
        }
        selectedPromo = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
        selectedNonPromo = arrayListOf<Int>().apply {
            parcel.readList(this, Int::class.java.classLoader)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupId)
        parcel.writeString(groupName)
        parcel.writeList(selectedProductIds)
        parcel.writeList(selectedKeywords)
        parcel.writeList(manualKeywords)
        parcel.writeList(selectedSuggestBid)
        parcel.writeInt(suggestedBidPerClick)
        parcel.writeInt(maxBid)
        parcel.writeInt(minBid)
        parcel.writeInt(dailyBudget)
        parcel.writeList(adIds)
        parcel.writeList(adIdsPromo)
        parcel.writeList(adIdsNonPromo)
        parcel.writeInt(finalBidPerClick)
        parcel.writeInt(minSuggestBidKeyword)
        parcel.writeList(selectedPromo)
        parcel.writeList(selectedNonPromo)
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