package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreViewModel implements Visitable<ExploreTypeFactory>,Parcelable {

    private String adId;
    private String imageUrl;
    private String title;
    private String commissionString;
    private String productId;
    private String productLink;

    public ExploreViewModel(String adId, String imageUrl, String title, String commissionString, String productId, String productLink) {
        this.adId = adId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.commissionString = commissionString;
        this.productId = productId;
        this.productLink = productLink;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    @Override
    public int type(ExploreTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adId);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.commissionString);
        dest.writeString(this.productId);
        dest.writeString(this.productLink);
    }

    protected ExploreViewModel(Parcel in) {
        this.adId = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.commissionString = in.readString();
        this.productId = in.readString();
        this.productLink = in.readString();
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
