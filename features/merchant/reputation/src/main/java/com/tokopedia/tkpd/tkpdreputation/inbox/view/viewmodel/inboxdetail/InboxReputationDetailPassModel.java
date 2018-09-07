package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;

/**
 * @author by nisie on 8/28/17.
 */

public class InboxReputationDetailPassModel implements Parcelable{

    ReputationDataViewModel reputationDataViewModel;
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
                                          ReputationDataViewModel reputationDataViewModel,
                                          int role) {
        this.reputationId = reputationId;
        this.revieweeName = revieweeName;
        this.revieweeImage = revieweeImage;
        this.deadlineText = deadlineText;
        this.invoice = invoice;
        this.createTime = createTime;
        this.reputationDataViewModel = reputationDataViewModel;
        this.role = role;
    }

    protected InboxReputationDetailPassModel(Parcel in) {
        reputationDataViewModel = in.readParcelable(ReputationDataViewModel.class.getClassLoader());
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

    public ReputationDataViewModel getReputationDataViewModel() {
        return reputationDataViewModel;
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
        dest.writeParcelable(reputationDataViewModel, flags);
        dest.writeString(reputationId);
        dest.writeString(revieweeName);
        dest.writeString(revieweeImage);
        dest.writeString(deadlineText);
        dest.writeString(invoice);
        dest.writeString(createTime);
        dest.writeInt(role);
    }
}
