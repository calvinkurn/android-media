package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel;

/**
 * @author by nisie on 8/28/17.
 */

public class InboxReputationDetailPassModel implements Parcelable{

    ReputationDataUiModel reputationDataUiModel;
    String reputationId;
    String revieweeName;
    String revieweeImage;
    String deadlineText;
    String invoice;
    String createTime;
    int role;

    public InboxReputationDetailPassModel(String reputationId, String revieweeName,
                                          String revieweeImage, String deadlineText,
                                          String invoice, String createTime,
                                          ReputationDataUiModel reputationDataUiModel,
                                          int role) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.revieweeImage = revieweeImage;
        this.deadlineText = deadlineText;
        this.invoice = invoice;
        this.createTime = createTime;
        this.reputationDataUiModel = reputationDataUiModel;
        this.role = role;
    }

    protected InboxReputationDetailPassModel(Parcel in) {
        reputationDataUiModel = in.readParcelable(ReputationDataUiModel.class.getClassLoader());
        reputationId = in.readString();
        revieweeName = in.readString();
        revieweeImage = in.readString();
        deadlineText = in.readString();
        invoice = in.readString();
        createTime = in.readString();
        role = in.readInt();
    }

    public static final Creator<InboxReputationDetailPassModel> CREATOR = new Creator<InboxReputationDetailPassModel>() {
        @Override
        public InboxReputationDetailPassModel createFromParcel(Parcel in) {
            return new InboxReputationDetailPassModel(in);
        }

        @Override
        public InboxReputationDetailPassModel[] newArray(int size) {
            return new InboxReputationDetailPassModel[size];
        }
    };

    public String getReputationId() {
        return reputationId;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public String getRevieweeImage() {
        return revieweeImage;
    }

    public String getDeadlineText() {
        return deadlineText;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ReputationDataUiModel getReputationDataUiModel() {
        return reputationDataUiModel;
    }

    public int getRole() {
        return role;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(reputationDataUiModel, flags);
        dest.writeString(reputationId);
        dest.writeString(revieweeName);
        dest.writeString(revieweeImage);
        dest.writeString(deadlineText);
        dest.writeString(invoice);
        dest.writeString(createTime);
        dest.writeInt(role);
    }
}
