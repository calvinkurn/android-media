package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/30/17.
 */
class RevieweeBadgeCustomerUiModel : Parcelable {
    var positive: Int
        private set
    var neutral: Int
        private set
    var negative: Int
        private set
    var positivePercentage: String?
        private set
    var noReputation: Int
        private set

    constructor(
        positive: Int, neutral: Int, negative: Int,
        positivePercentage: String?, noReputation: Int
    ) {
        this.positive = positive
        this.neutral = neutral
        this.negative = negative
        this.positivePercentage = positivePercentage
        this.noReputation = noReputation
    }

    protected constructor(`in`: Parcel) {
        positive = `in`.readInt()
        neutral = `in`.readInt()
        negative = `in`.readInt()
        positivePercentage = `in`.readString()
        noReputation = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(positive)
        dest.writeInt(neutral)
        dest.writeInt(negative)
        dest.writeString(positivePercentage)
        dest.writeInt(noReputation)
    }

    companion object {
        val CREATOR: Parcelable.Creator<RevieweeBadgeCustomerUiModel> =
            object : Parcelable.Creator<RevieweeBadgeCustomerUiModel?> {
                override fun createFromParcel(`in`: Parcel): RevieweeBadgeCustomerUiModel? {
                    return RevieweeBadgeCustomerUiModel(`in`)
                }

                override fun newArray(size: Int): Array<RevieweeBadgeCustomerUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}