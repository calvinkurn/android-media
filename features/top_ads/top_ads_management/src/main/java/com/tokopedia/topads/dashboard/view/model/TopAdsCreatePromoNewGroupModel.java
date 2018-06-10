package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoNewGroupModel extends TopAdsProductListStepperModel {
    String groupName;
    TopAdsDetailGroupViewModel detailAd;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public TopAdsDetailGroupViewModel getDetailAd() {
        return detailAd;
    }

    public void setDetailAd(TopAdsDetailGroupViewModel detailAd) {
        this.detailAd = detailAd;
    }

    public void setDetailGroupScheduleViewModel(TopAdsDetailGroupViewModel detailGroupViewModel){
        if(detailAd == null) {
            detailAd = detailGroupViewModel;
        }else if(detailGroupViewModel != null){
            detailAd.setScheduled(detailGroupViewModel.isScheduled());
            detailAd.setStartDate(detailGroupViewModel.getStartDate());
            detailAd.setStartTime(detailGroupViewModel.getStartTime());
            detailAd.setEndDate(detailGroupViewModel.getEndDate());
            detailAd.setEndTime(detailGroupViewModel.getEndTime());
        }
    }

    public void setDetailGroupCostViewModel(TopAdsDetailGroupViewModel detailGroupViewModel) {
        if(detailAd == null) {
            detailAd = detailGroupViewModel;
        }else if(detailGroupViewModel != null){
            detailAd.setPriceBid(detailGroupViewModel.getPriceBid());
            detailAd.setPriceDaily(detailGroupViewModel.getPriceDaily());
            detailAd.setBudget(detailGroupViewModel.isBudget());
        }
    }

    public TopAdsCreatePromoNewGroupModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.groupName);
        dest.writeParcelable(this.detailAd, flags);
    }

    protected TopAdsCreatePromoNewGroupModel(Parcel in) {
        super(in);
        this.groupName = in.readString();
        this.detailAd = in.readParcelable(TopAdsDetailGroupViewModel.class.getClassLoader());
    }

    public static final Creator<TopAdsCreatePromoNewGroupModel> CREATOR = new Creator<TopAdsCreatePromoNewGroupModel>() {
        @Override
        public TopAdsCreatePromoNewGroupModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoNewGroupModel(source);
        }

        @Override
        public TopAdsCreatePromoNewGroupModel[] newArray(int size) {
            return new TopAdsCreatePromoNewGroupModel[size];
        }
    };
}
