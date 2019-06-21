package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemViewModel implements Visitable<DashboardItemTypeFactory>,Parcelable {

    private String id;
    private String imageUrl;
    private String title;
    private String value;
    private String itemClicked;
    private String itemSold;
    private String productCommission;
    private boolean isActive;

    public DashboardItemViewModel(String id, String imageUrl, String title, String value,
                                  String itemClicked, String itemSold,
                                  String productCommission, boolean isActive) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.value = value;
        this.itemClicked = itemClicked;
        this.itemSold = itemSold;
        this.productCommission = productCommission;
        this.isActive = isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getItemClicked() {
        return itemClicked;
    }

    public void setItemClicked(String itemClicked) {
        this.itemClicked = itemClicked;
    }

    public String getItemSold() {
        return itemSold;
    }

    public void setItemSold(String itemSold) {
        this.itemSold = itemSold;
    }

    public String getProductCommission() {
        return productCommission;
    }

    public void setProductCommission(String productCommission) {
        this.productCommission = productCommission;
    }

    @Override
    public int type(DashboardItemTypeFactory typeFactory) {
        return typeFactory.type(this);
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
        dest.writeString(this.value);
        dest.writeString(this.itemClicked);
        dest.writeString(this.itemSold);
        dest.writeString(this.productCommission);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
    }

    protected DashboardItemViewModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.value = in.readString();
        this.itemClicked = in.readString();
        this.itemSold = in.readString();
        this.productCommission = in.readString();
        this.isActive = in.readByte() != 0;
    }

    public static final Creator<DashboardItemViewModel> CREATOR = new Creator<DashboardItemViewModel>() {
        @Override
        public DashboardItemViewModel createFromParcel(Parcel source) {
            return new DashboardItemViewModel(source);
        }

        @Override
        public DashboardItemViewModel[] newArray(int size) {
            return new DashboardItemViewModel[size];
        }
    };
}
