package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel

/**
 * @author by nisie on 8/28/17.
 */
class InboxReputationDetailPassModel : Parcelable {
    var reputationDataUiModel: ReputationDataUiModel?
    var reputationId: String?
    var revieweeName: String?
    var revieweeImage: String?
    var deadlineText: String?
    var invoice: String?
    var createTime: String?
    var role: Int

    constructor(
        reputationId: String?, revieweeName: String?,
        revieweeImage: String?, deadlineText: String?,
        invoice: String?, createTime: String?,
        reputationDataUiModel: ReputationDataUiModel?,
        role: Int
    ) {
        this.reputationId = reputationId
        this.revieweeName = revieweeName
        this.revieweeImage = revieweeImage
        this.deadlineText = deadlineText
        this.invoice = invoice
        this.createTime = createTime
        this.reputationDataUiModel = reputationDataUiModel
        this.role = role
    }

    protected constructor(`in`: Parcel) {
        reputationDataUiModel =
            `in`.readParcelable(ReputationDataUiModel::class.java.getClassLoader())
        reputationId = `in`.readString()
        revieweeName = `in`.readString()
        revieweeImage = `in`.readString()
        deadlineText = `in`.readString()
        invoice = `in`.readString()
        createTime = `in`.readString()
        role = `in`.readInt()
    }

    public override fun describeContents(): Int {
        return 0
    }

    public override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(reputationDataUiModel, flags)
        dest.writeString(reputationId)
        dest.writeString(revieweeName)
        dest.writeString(revieweeImage)
        dest.writeString(deadlineText)
        dest.writeString(invoice)
        dest.writeString(createTime)
        dest.writeInt(role)
    }

    companion object {
        val CREATOR: Parcelable.Creator<InboxReputationDetailPassModel> =
            object : Parcelable.Creator<InboxReputationDetailPassModel?> {
                public override fun createFromParcel(`in`: Parcel): InboxReputationDetailPassModel? {
                    return InboxReputationDetailPassModel(`in`)
                }

                public override fun newArray(size: Int): Array<InboxReputationDetailPassModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}