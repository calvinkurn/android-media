package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel

/**
 * @author by nisie on 8/30/17.
 */
class RevieweeBadgeSellerUiModel : Parcelable {
    var tooltip: String?
        private set
    var reputationScore: String?
        private set
    var score: Int
        private set
    var minBadgeScore: Int
        private set
    var reputationBadgeUrl: String?
        private set
    var reputationBadge: ReputationBadgeUiModel?
        private set
    var isFavorited: Int = -1

    constructor(
        tooltip: String?, reputationScore: String?, score: Int,
        minBadgeScore: Int, reputationBadgeUrl: String?,
        reputationBadge: ReputationBadgeUiModel?, isFavorited: Int
    ) {
        this.tooltip = tooltip
        this.reputationScore = reputationScore
        this.score = score
        this.minBadgeScore = minBadgeScore
        this.reputationBadgeUrl = reputationBadgeUrl
        this.reputationBadge = reputationBadge
        this.isFavorited = isFavorited
    }

    protected constructor(`in`: Parcel) {
        tooltip = `in`.readString()
        reputationScore = `in`.readString()
        score = `in`.readInt()
        minBadgeScore = `in`.readInt()
        reputationBadgeUrl = `in`.readString()
        reputationBadge = `in`.readParcelable(ReputationBadgeUiModel::class.java.classLoader)
        isFavorited = `in`.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(tooltip)
        dest.writeString(reputationScore)
        dest.writeInt(score)
        dest.writeInt(minBadgeScore)
        dest.writeString(reputationBadgeUrl)
        dest.writeParcelable(reputationBadge, flags)
        dest.writeInt(isFavorited)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<RevieweeBadgeSellerUiModel> =
            object : Parcelable.Creator<RevieweeBadgeSellerUiModel?> {
                override fun createFromParcel(`in`: Parcel): RevieweeBadgeSellerUiModel? {
                    return RevieweeBadgeSellerUiModel(`in`)
                }

                override fun newArray(size: Int): Array<RevieweeBadgeSellerUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}