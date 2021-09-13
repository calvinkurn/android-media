package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by nisie on 8/30/17.
 */
class ReputationBadgeUiModel : Parcelable {
    var level: Int
        private set
    var set: Int
        private set

    constructor(level: Int, set: Int) {
        this.level = level
        this.set = set
    }

    protected constructor(`in`: Parcel) {
        level = `in`.readInt()
        set = `in`.readInt()
    }

    public override fun describeContents(): Int {
        return 0
    }

    public override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(level)
        dest.writeInt(set)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ReputationBadgeUiModel> =
            object : Parcelable.Creator<ReputationBadgeUiModel?> {
                public override fun createFromParcel(`in`: Parcel): ReputationBadgeUiModel? {
                    return ReputationBadgeUiModel(`in`)
                }

                public override fun newArray(size: Int): Array<ReputationBadgeUiModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}