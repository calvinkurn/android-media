package com.tokopedia.home.model;

import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.var.ProductItem;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by m.normansyah on 27/10/2015.
 * this class model, very very outer model
 */
@Parcel
public class ProductFeedModel {
    HorizontalProductList dataHistory;
    List<ProductItem> dataProduct;
    HorizontalProductList dataTopAds;

    public ProductFeedModel(){}

    public HorizontalProductList getDataHistory() {
        return dataHistory;
    }

    public void setDataHistory(List<ProductItem> dataHistory) {
        this.dataHistory = new HorizontalProductList(dataHistory);
    }

    public List<ProductItem> getDataProduct() {
        return dataProduct;
    }

    public void setDataProduct(List<ProductItem> dataProduct) {
        this.dataProduct = dataProduct;
    }

    public HorizontalProductList getDataTopAds() {
        return dataTopAds;
    }

    public void setDataTopAds(List<ProductItem> dataTopAds) {
        this.dataTopAds = new HorizontalProductList(dataTopAds);
    }
}
