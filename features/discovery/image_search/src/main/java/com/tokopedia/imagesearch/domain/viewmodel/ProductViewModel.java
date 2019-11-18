package com.tokopedia.imagesearch.domain.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModel implements Parcelable {
    private List<ProductItem> productList = new ArrayList<>();
    private boolean hasCatalog;
    private String query;
    private String shareUrl;
    private String additionalParams;
    private int totalData;
    private int totalItem;
    private TopAdsModel adsModel;
    private CpmModel cpmModel;
    private SearchParameter searchParameter;
    private CategoryFilterModel categoryFilterModel;

    public TopAdsModel getAdsModel() {
        return adsModel;
    }

    public ProductViewModel() {
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    public SearchParameter getSearchParameter() {
        return searchParameter;
    }

    public CategoryFilterModel getCategoryFilterModel() {
        return categoryFilterModel;
    }

    public void setCategoryFilterModel(CategoryFilterModel categoryFilterModel) {
        this.categoryFilterModel = categoryFilterModel;
    }

    public int getTotalItem() {
        return getProductList().size() + getAdsModel().getData().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productList);
        dest.writeByte(this.hasCatalog ? (byte) 1 : (byte) 0);
        dest.writeString(this.query);
        dest.writeString(this.shareUrl);
        dest.writeString(this.additionalParams);
        dest.writeInt(this.totalData);
        dest.writeInt(this.totalItem);
        dest.writeParcelable(this.adsModel, flags);
        dest.writeParcelable(this.cpmModel, flags);
        dest.writeParcelable(this.searchParameter, flags);
    }

    protected ProductViewModel(Parcel in) {
        this.productList = in.createTypedArrayList(ProductItem.CREATOR);
        this.hasCatalog = in.readByte() != 0;
        this.query = in.readString();
        this.shareUrl = in.readString();
        this.additionalParams = in.readString();
        this.totalData = in.readInt();
        this.totalItem = in.readInt();
        this.adsModel = in.readParcelable(TopAdsModel.class.getClassLoader());
        this.cpmModel = in.readParcelable(CpmModel.class.getClassLoader());
        this.searchParameter = in.readParcelable(SearchParameter.class.getClassLoader());
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel source) {
            return new ProductViewModel(source);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };
}
