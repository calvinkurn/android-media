package com.tokopedia.core.network.entity.discovery;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.router.discovery.BrowseProductRouter;

import java.util.HashMap;
import java.util.Map;

public class BrowseProductActivityModel implements Parcelable {
    protected String parentDepartement = "0";
    protected String departmentId = "0";
    protected int fragmentId = BrowseProductRouter.VALUES_INVALID_FRAGMENT_ID;
    protected String adSrc = TopAdsApi.SRC_BROWSE_PRODUCT;
    HotListBannerModel hotListBannerModel;
    public String alias;
    public String q = "";
    public String ob; //set to default sort
    public String obCatalog; //set to default sort
    public boolean isSearchDeeplink = false;
    public String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
    public int activeTab;
    public String unique_id;
    public Map<String, String> filterOptions;
    public Data categoryHeader;
    public String totalDataCategory ="";

    public HotListBannerModel getHotListBannerModel() {
        return hotListBannerModel;
    }

    public void setHotListBannerModel(HotListBannerModel hotListBannerModel) {
        this.hotListBannerModel = hotListBannerModel;
        if(hotListBannerModel!=null) {
            fragmentId = BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID;
            adSrc = TopAdsApi.SRC_HOTLIST;
        }
    }

    public void removeBannerModel() {
        this.hotListBannerModel = null;
        fragmentId = BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID;
    }

    public String getParentDepartement() {
        return parentDepartement;
    }

    public void setParentDepartement(String parentDepartement) {
        this.parentDepartement = parentDepartement;
    }

    public boolean isSearchDeeplink() {
        return isSearchDeeplink;
    }

    public void setSearchDeeplink(boolean searchDeeplink) {
        isSearchDeeplink = searchDeeplink;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public int getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(int tab) {
        this.activeTab = tab;
    }

    public String getAdSrc() {
        return adSrc;
    }

    public void setAdSrc(String adSrc) {
        this.adSrc = adSrc;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getOb() {
        return ob;
    }

    public void setOb(String ob) {
        this.ob = ob;
    }

    public String getObCatalog() {
        return obCatalog;
    }

    public void setObCatalog(String obPosition) {
        this.obCatalog = obPosition;
    }

    public Map<String, String> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Map<String, String> filterOptions) {
        this.filterOptions = filterOptions;
    }

    public Data getCategoryHeader() {
        return categoryHeader;
    }

    public void setCategoryHeader(Data categoryHeader) {
        this.categoryHeader = categoryHeader;
    }

    public String getTotalDataCategory() {
        return totalDataCategory;
    }

    public void setTotalDataCategory(String totalDataCategory) {
        this.totalDataCategory = totalDataCategory;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.parentDepartement);
        dest.writeString(this.departmentId);
        dest.writeInt(this.fragmentId);
        dest.writeString(this.adSrc);
        dest.writeParcelable((Parcelable) this.hotListBannerModel, flags);
        dest.writeString(this.alias);
        dest.writeString(this.q);
        dest.writeString(this.ob);
        dest.writeString(this.obCatalog);
        dest.writeByte(this.isSearchDeeplink ? (byte) 1 : (byte) 0);
        dest.writeString(this.source);
        dest.writeInt(this.activeTab);
        dest.writeString(this.unique_id);
        if (filterOptions != null) {
            dest.writeInt(this.filterOptions.size());
            for (Map.Entry<String, String> entry : this.filterOptions.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }
        dest.writeParcelable(categoryHeader,flags);
        dest.writeString(this.totalDataCategory);
    }

    public BrowseProductActivityModel() {
    }

    protected BrowseProductActivityModel(Parcel in) {
        this.parentDepartement = in.readString();
        this.departmentId = in.readString();
        this.fragmentId = in.readInt();
        this.adSrc = in.readString();
        this.hotListBannerModel = in.readParcelable(HotListBannerModel.class.getClassLoader());
        this.alias = in.readString();
        this.q = in.readString();
        this.ob = in.readString();
        this.obCatalog = in.readString();
        this.isSearchDeeplink = in.readByte() != 0;
        this.source = in.readString();
        this.activeTab = in.readInt();
        this.unique_id = in.readString();
        int filterOptionsSize = in.readInt();
        this.filterOptions = new HashMap<>(filterOptionsSize);
        for (int i = 0; i < filterOptionsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.filterOptions.put(key, value);
        }
        this.categoryHeader = in.readParcelable(Data.class.getClassLoader());
        this.totalDataCategory = in.readString();
    }

    public static final Parcelable.Creator<BrowseProductActivityModel> CREATOR
            = new Parcelable.Creator<BrowseProductActivityModel>() {
        @Override
        public BrowseProductActivityModel createFromParcel(Parcel source) {
            return new BrowseProductActivityModel(source);
        }

        @Override
        public BrowseProductActivityModel[] newArray(int size) {
            return new BrowseProductActivityModel[size];
        }
    };
}
