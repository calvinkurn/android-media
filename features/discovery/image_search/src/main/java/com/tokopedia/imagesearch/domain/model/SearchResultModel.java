package com.tokopedia.imagesearch.domain.model;

import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;

import java.util.List;

/**
 * Created by hangnadi on 10/3/17.
 */

public class SearchResultModel {

    private List<ProductModel> productList;
    private CategoryFilterModel categoryFilterModel;
    private int totalData;
    private String query;
    private String shareUrl;
    private String additionalParams;

    public void setProductList(List<ProductModel> productList) {
        this.productList = productList;
    }

    public List<ProductModel> getProductList() {
        return productList;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
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

    public CategoryFilterModel getCategoryFilterModel() {
        return categoryFilterModel;
    }

    public void setCategoryFilterModel(CategoryFilterModel categoryFilterModel) {
        this.categoryFilterModel = categoryFilterModel;
    }
}
