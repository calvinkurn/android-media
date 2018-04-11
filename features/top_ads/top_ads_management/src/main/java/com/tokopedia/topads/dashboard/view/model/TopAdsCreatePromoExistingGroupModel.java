package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoExistingGroupModel extends TopAdsProductListStepperModel {
    String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public static Creator<TopAdsCreatePromoExistingGroupModel> getCREATOR() {
        return CREATOR;
    }

    public TopAdsCreatePromoExistingGroupModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.groupId);
    }

    protected TopAdsCreatePromoExistingGroupModel(Parcel in) {
        super(in);
        this.groupId = in.readString();
    }

    public static final Creator<TopAdsCreatePromoExistingGroupModel> CREATOR = new Creator<TopAdsCreatePromoExistingGroupModel>() {
        @Override
        public TopAdsCreatePromoExistingGroupModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoExistingGroupModel(source);
        }

        @Override
        public TopAdsCreatePromoExistingGroupModel[] newArray(int size) {
            return new TopAdsCreatePromoExistingGroupModel[size];
        }
    };
}
