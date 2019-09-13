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

    public int getTotalItem() {
        return getProductList().size() + getAdsModel().getData().size();
    }
}
