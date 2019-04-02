package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoShopModel implements StepperModel {
    TopAdsDetailShopViewModel topAdsDetailShopViewModel;

    public TopAdsDetailShopViewModel getTopAdsDetailShopViewModel() {
        return topAdsDetailShopViewModel;
    }

    public void setDetailShopScheduleViewModel(TopAdsDetailShopViewModel topAdsDetailShopViewModel){
        if(this.topAdsDetailShopViewModel == null) {
            this.topAdsDetailShopViewModel = topAdsDetailShopViewModel;
        }else if(topAdsDetailShopViewModel != null){
            this.topAdsDetailShopViewModel.setScheduled(topAdsDetailShopViewModel.isScheduled());
            this.topAdsDetailShopViewModel.setStartDate(topAdsDetailShopViewModel.getStartDate());
            this.topAdsDetailShopViewModel.setStartTime(topAdsDetailShopViewModel.getStartTime());
        }
    }

    public void setDetailShopCostViewModel(TopAdsDetailShopViewModel topAdsDetailShopViewModel) {
        if(this.topAdsDetailShopViewModel == null) {
            this.topAdsDetailShopViewModel = topAdsDetailShopViewModel;
        }else if(topAdsDetailShopViewModel != null){
            this.topAdsDetailShopViewModel.setPriceBid(topAdsDetailShopViewModel.getPriceBid());
            this.topAdsDetailShopViewModel.setPriceDaily(topAdsDetailShopViewModel.getPriceDaily());
            this.topAdsDetailShopViewModel.setBudget(topAdsDetailShopViewModel.isBudget());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.topAdsDetailShopViewModel, flags);
    }

    public TopAdsCreatePromoShopModel() {
    }

    protected TopAdsCreatePromoShopModel(Parcel in) {
        this.topAdsDetailShopViewModel = in.readParcelable(TopAdsDetailShopViewModel.class.getClassLoader());
    }

    public static final Creator<TopAdsCreatePromoShopModel> CREATOR = new Creator<TopAdsCreatePromoShopModel>() {
        @Override
        public TopAdsCreatePromoShopModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoShopModel(source);
        }

        @Override
        public TopAdsCreatePromoShopModel[] newArray(int size) {
            return new TopAdsCreatePromoShopModel[size];
        }
    };
}
