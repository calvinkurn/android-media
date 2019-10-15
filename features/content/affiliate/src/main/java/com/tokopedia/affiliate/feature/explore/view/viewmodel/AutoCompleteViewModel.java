package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompleteViewModel implements Parcelable {

    private String keyword;
    private String text;
    private String formatted;

    public AutoCompleteViewModel(String keyword, String text, String formatted) {
        this.keyword = keyword;
        this.text = text;
        this.formatted = formatted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.formatted);
    }

    protected AutoCompleteViewModel(Parcel in) {
        this.text = in.readString();
        this.formatted = in.readString();
    }

    public static final Parcelable.Creator<AutoCompleteViewModel> CREATOR = new Parcelable.Creator<AutoCompleteViewModel>() {
        @Override
        public AutoCompleteViewModel createFromParcel(Parcel source) {
            return new AutoCompleteViewModel(source);
        }

        @Override
        public AutoCompleteViewModel[] newArray(int size) {
            return new AutoCompleteViewModel[size];
        }
    };
}
