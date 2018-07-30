package com.tokopedia.vote.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteItemDomainModel implements Parcelable {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    private static final String BAR_TYPE = "Bar";
    private static final String IMAGE_TYPE = "Image";

    private String optionId, option, url, type, percentage;
    private int selected;

    public VoteItemDomainModel(String optionId, String option, String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
        this.percentage = percentage;
        this.selected = selected;
        this.type = BAR_TYPE;
    }

    public VoteItemDomainModel(String optionId, String option, String url, String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
        this.url = url;
        this.percentage = percentage;
        this.selected = selected;
        this.type = IMAGE_TYPE;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPercentage() {
        return percentage;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOptionId() {
        return optionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionId);
        dest.writeString(this.option);
        dest.writeString(this.percentage);
        dest.writeInt(this.selected);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    protected VoteItemDomainModel(Parcel in) {
        this.optionId = in.readString();
        this.option = in.readString();
        this.percentage = in.readString();
        this.selected = in.readInt();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<VoteItemDomainModel> CREATOR = new Creator<VoteItemDomainModel>() {
        @Override
        public VoteItemDomainModel createFromParcel(Parcel source) {
            return new VoteItemDomainModel(source);
        }

        @Override
        public VoteItemDomainModel[] newArray(int size) {
            return new VoteItemDomainModel[size];
        }
    };

    public int getPercentageInteger() {
        try {
            return (int) Double.parseDouble(percentage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
