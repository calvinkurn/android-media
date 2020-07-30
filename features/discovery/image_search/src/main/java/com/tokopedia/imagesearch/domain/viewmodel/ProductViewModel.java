package com.tokopedia.imagesearch.domain.viewmodel;

import com.tokopedia.filter.common.data.DynamicFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModel {
    private List<ProductItem> productList = new ArrayList<>();
    private String query;
    private String token;
    private String totalDataText;
    private int totalData;
    private CategoryFilterModel categoryFilterModel;
    private DynamicFilterModel dynamicFilterModel;

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public String getTotalDataText() {
        return totalDataText;
    }

    public void setTotalDataText(String totalDataText) {
        this.totalDataText = totalDataText;
    }

    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CategoryFilterModel getCategoryFilterModel() {
        return categoryFilterModel;
    }

    public void setCategoryFilterModel(CategoryFilterModel categoryFilterModel) {
        this.categoryFilterModel = categoryFilterModel;
    }

    public DynamicFilterModel getDynamicFilterModel() {
        return dynamicFilterModel;
    }

    public void setDynamicFilterModel(DynamicFilterModel dynamicFilterModel) {
        this.dynamicFilterModel = dynamicFilterModel;
    }
}
