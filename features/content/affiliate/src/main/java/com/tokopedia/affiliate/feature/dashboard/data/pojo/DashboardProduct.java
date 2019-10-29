package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardProduct {

    @SerializedName("pagination")
    @Expose
    private DashboardPagingPojo pagination;
    @SerializedName("products")
    @Expose
    private List<DashboardItemPojo> affiliatedProducts;
    @SerializedName("subtitles")
    @Expose
    private List<DashboardSubtitlePojo> subtitles;

    public DashboardPagingPojo getPagination() {
        return pagination;
    }

    public void setPagination(DashboardPagingPojo pagination) {
        this.pagination = pagination;
    }

    public List<DashboardItemPojo> getAffiliatedProducts() {
        return affiliatedProducts;
    }

    public List<DashboardSubtitlePojo> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<DashboardSubtitlePojo> subtitles) {
        this.subtitles = subtitles;
    }

    public void setAffiliatedProducts(List<DashboardItemPojo> affiliatedProducts) {
        this.affiliatedProducts = affiliatedProducts;
    }
}
