package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointPromosEntity {
    @Expose
    @SerializedName("catalog")
    CatalogEntity catalogs;

    @Expose
    @SerializedName("coupons")
    PromoCouponEntity coupons;

    public CatalogEntity getCatalog() {
        return catalogs;
    }

    public void setCatalogs(CatalogEntity catalogs) {
        this.catalogs = catalogs;
    }

    public PromoCouponEntity getCoupon() {
        return coupons;
    }

    public void setCoupons(PromoCouponEntity coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "TokoPointPromosEntity{" +
                "catalogs=" + catalogs +
                ", coupons=" + coupons +
                '}';
    }
}
