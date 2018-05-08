package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by naveengoyal on 1/8/18.
 */

public class GroupViewModel implements Parcelable {

    private Integer id;
    private Integer productId;
    private Integer productScheduleId;
    private String providerGroupId;
    private String name;
    private String description;
    private String tnc;
    private String providerMetaData;
    private Integer status;
    private String createdAt;
    private String updatedAt;

    private List<PackageViewModel> packages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductScheduleId() {
        return productScheduleId;
    }

    public void setProductScheduleId(Integer productScheduleId) {
        this.productScheduleId = productScheduleId;
    }

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getProviderMetaData() {
        return providerMetaData;
    }

    public void setProviderMetaData(String providerMetaData) {
        this.providerMetaData = providerMetaData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PackageViewModel> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageViewModel> packages) {
        this.packages = packages;
    }

    public GroupViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.productId);
        dest.writeValue(this.productScheduleId);
        dest.writeString(this.providerGroupId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.tnc);
        dest.writeString(this.providerMetaData);
        dest.writeValue(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeTypedList(this.packages);
    }

    protected GroupViewModel(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productScheduleId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.providerGroupId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.tnc = in.readString();
        this.providerMetaData = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.packages = in.createTypedArrayList(PackageViewModel.CREATOR);
    }

    public static final Creator<GroupViewModel> CREATOR = new Creator<GroupViewModel>() {
        @Override
        public GroupViewModel createFromParcel(Parcel source) {
            return new GroupViewModel(source);
        }

        @Override
        public GroupViewModel[] newArray(int size) {
            return new GroupViewModel[size];
        }
    };
}
