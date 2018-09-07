package com.tokopedia.groupchat.vote.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.vote.view.adapter.typefactory.VoteTypeFactory;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteViewModel implements Parcelable, Visitable<VoteTypeFactory> {

    public static final int DEFAULT = 0;
    public static final int UNSELECTED = 1;
    public static final int SELECTED = 2;

    public static final String BAR_TYPE = "Bar";
    public static final String IMAGE_TYPE = "Image";

    private String optionId, option, url, type, percentage;
    private int selected;

    public VoteViewModel(String optionId, String option, String percentage, int selected) {
        this.optionId = optionId;
        this.option = option;
        this.percentage = percentage;
        this.selected = selected;
        this.type = BAR_TYPE;
    }

    public VoteViewModel(String optionId, String option, String url, String percentage, int selected) {
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
    public int type(VoteTypeFactory typeFactory) {
        return typeFactory.type(this);
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

    protected VoteViewModel(Parcel in) {
        this.optionId = in.readString();
        this.option = in.readString();
        this.percentage = in.readString();
        this.selected = in.readInt();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<VoteViewModel> CREATOR = new Creator<VoteViewModel>() {
        @Override
        public VoteViewModel createFromParcel(Parcel source) {
            return new VoteViewModel(source);
        }

        @Override
        public VoteViewModel[] newArray(int size) {
            return new VoteViewModel[size];
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
