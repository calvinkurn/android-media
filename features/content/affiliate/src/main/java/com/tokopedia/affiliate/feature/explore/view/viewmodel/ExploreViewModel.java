package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreViewModel implements Visitable<ExploreTypeFactory>,Parcelable {

    private String id;
    private String imageUrl;
    private String title;
    private String commissionString;

    public ExploreViewModel(String id, String imageUrl, String title, String commissionString) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.commissionString = commissionString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommissionString() {
        return commissionString;
    }

    public void setCommissionString(String commissionString) {
        this.commissionString = commissionString;
    }

    @Override
    public int type(ExploreTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public ExploreViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.commissionString);
    }

    protected ExploreViewModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.commissionString = in.readString();
    }

    public static final Creator<ExploreViewModel> CREATOR = new Creator<ExploreViewModel>() {
        @Override
        public ExploreViewModel createFromParcel(Parcel source) {
            return new ExploreViewModel(source);
        }

        @Override
        public ExploreViewModel[] newArray(int size) {
            return new ExploreViewModel[size];
        }
    };
}
