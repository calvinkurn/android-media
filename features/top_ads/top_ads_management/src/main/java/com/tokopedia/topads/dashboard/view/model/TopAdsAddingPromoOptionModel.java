package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;

import com.tokopedia.base.list.seller.common.util.ItemIdType;
import com.tokopedia.topads.common.constant.TopAdsAddingOption;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionModel implements ItemIdType, Parcelable {
    public static final int TYPE = 225;

    @TopAdsAddingOption
    private int optionId;
    private String titleOption;
    private String subtitleOption;
    private @IntegerRes int resIcon;

    public TopAdsAddingPromoOptionModel(int optionId, String titleOption, String subtitleOption, @IntegerRes int resIcon) {
        this.optionId = optionId;
        this.titleOption = titleOption;
        this.subtitleOption = subtitleOption;
        this.resIcon = resIcon;
    }

    public TopAdsAddingPromoOptionModel() {
    }

    protected TopAdsAddingPromoOptionModel(Parcel in) {
        optionId = in.readInt();
        titleOption = in.readString();
        subtitleOption = in.readString();
        resIcon = in.readInt();
    }

    public static final Creator<TopAdsAddingPromoOptionModel> CREATOR = new Creator<TopAdsAddingPromoOptionModel>() {
        @Override
        public TopAdsAddingPromoOptionModel createFromParcel(Parcel in) {
            return new TopAdsAddingPromoOptionModel(in);
        }

        @Override
        public TopAdsAddingPromoOptionModel[] newArray(int size) {
            return new TopAdsAddingPromoOptionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(optionId);
        parcel.writeString(titleOption);
        parcel.writeString(subtitleOption);
        parcel.writeInt(resIcon);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public String getItemId() {
        return String.valueOf(optionId);
    }

    @TopAdsAddingOption
    public int getOptionId() {
        return optionId;
    }

    public String getTitleOption() {
        return titleOption;
    }

    public String getSubtitleOption() {
        return subtitleOption;
    }

    public int getResIcon() {
        return resIcon;
    }
}
