package com.tokopedia.topads.keyword.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.keyword.constant.KeywordTypeDef

open class TopAdsKeywordNewStepperModel() : StepperModel {
    companion object CREATOR : Parcelable.Creator<TopAdsKeywordNewStepperModel> {
        override fun createFromParcel(parcel: Parcel): TopAdsKeywordNewStepperModel {
            return TopAdsKeywordNewStepperModel(parcel)
        }

        override fun newArray(size: Int): Array<TopAdsKeywordNewStepperModel?> {
            return arrayOfNulls(size)
        }
    }

    var isPositive: Boolean = false
    var groupId: String? = null
    var choosenId: String? = null
    var groupName: String? = null
    @KeywordTypeDef
    var keywordType: Int = 0
    var serverCount: Int = 0
    var maxWords: Int = 0
    var localWords: MutableList<String> = mutableListOf()

    constructor(parcel: Parcel) : this() {
        this.isPositive = parcel.readByte().toInt() != 0
        this.groupId = parcel.readString()
        this.groupName = parcel.readString()
        this.keywordType = parcel.readInt()
        this.serverCount = parcel.readInt()
        this.maxWords = parcel.readInt()
        this.localWords = parcel.createStringArrayList()
    }

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.let {
            it.writeByte(if (this.isPositive) 1.toByte() else 0.toByte())
            it.writeString(this.groupId)
            it.writeString(this.groupName)
            it.writeInt(this.keywordType)
            it.writeInt(this.serverCount)
            it.writeInt(this.maxWords)
            it.writeStringList(this.localWords)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

}