package com.tokopedia.browse.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory;

/**
 * @author by furqan on 05/09/18.
 */

public class DigitalBrowsePopularBrandsViewModel implements Parcelable, Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {

    private long id;
    private String name;
    private boolean isNew;
    private String logoUrl;
    private String url;

    public DigitalBrowsePopularBrandsViewModel() {
    }

    protected DigitalBrowsePopularBrandsViewModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
        isNew = in.readByte() != 0;
        logoUrl = in.readString();
        url = in.readString();
    }

    public static final Creator<DigitalBrowsePopularBrandsViewModel> CREATOR = new Creator<DigitalBrowsePopularBrandsViewModel>() {
        @Override
        public DigitalBrowsePopularBrandsViewModel createFromParcel(Parcel in) {
            return new DigitalBrowsePopularBrandsViewModel(in);
        }

        @Override
        public DigitalBrowsePopularBrandsViewModel[] newArray(int size) {
            return new DigitalBrowsePopularBrandsViewModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeString(logoUrl);
        dest.writeString(url);
    }

    @Override
    public int type(DigitalBrowseMarketplaceAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
