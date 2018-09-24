package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardPojo {

    @SerializedName("pagination")
    @Expose
    private DashboardPagingPojo pagination;
    @SerializedName("affiliatedProducts")
    @Expose
    private List<DashboardItemPojo> affiliatedProducts;
    @SerializedName("header")
    @Expose
    private DashboardHeaderPojo header;

    public DashboardPagingPojo getPagination() {
        return pagination;
    }

    public void setPagination(DashboardPagingPojo pagination) {
        this.pagination = pagination;
    }

    public List<DashboardItemPojo> getAffiliatedProducts() {
        return affiliatedProducts;
    }

    public void setAffiliatedProducts(List<DashboardItemPojo> affiliatedProducts) {
        this.affiliatedProducts = affiliatedProducts;
    }

    public DashboardHeaderPojo getHeader() {
        return header;
    }

    public void setHeader(DashboardHeaderPojo header) {
        this.header = header;
    }
}
