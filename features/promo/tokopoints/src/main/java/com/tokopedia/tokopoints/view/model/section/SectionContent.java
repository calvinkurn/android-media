
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionContent {

    @SerializedName("layoutType")
    @Expose
    private String layoutType;
    @SerializedName("sectionTitle")
    @Expose
    private String sectionTitle;
    @SerializedName("sectionSubTitle")
    @Expose
    private String sectionSubTitle;
    @SerializedName("countdownAttr")
    @Expose
    private CountdownAttr countdownAttr;
    @SerializedName("cta")
    @Expose
    private Cta cta;
    @SerializedName("layoutCategoryAttr")
    @Expose
    private LayoutCategoryAttr layoutCategoryAttr;
    @SerializedName("layoutCouponAttr")
    @Expose
    private LayoutCouponAttr layoutCouponAttr;
    @SerializedName("layoutBannerAttr")
    @Expose
    private LayoutBannerAttr layoutBannerAttr;
    @SerializedName("layoutCatalogAttr")
    @Expose
    private LayoutCatalogAttr layoutCatalogAttr;
    @SerializedName("layoutTickerAttr")
    @Expose
    private LayoutTickerAttr layoutTickerAttr;

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionSubTitle() {
        return sectionSubTitle;
    }

    public void setSectionSubTitle(String sectionSubTitle) {
        this.sectionSubTitle = sectionSubTitle;
    }

    public CountdownAttr getCountdownAttr() {
        return countdownAttr;
    }

    public void setCountdownAttr(CountdownAttr countdownAttr) {
        this.countdownAttr = countdownAttr;
    }

    public Cta getCta() {
        return cta;
    }

    public void setCta(Cta cta) {
        this.cta = cta;
    }

    public LayoutCategoryAttr getLayoutCategoryAttr() {
        return layoutCategoryAttr;
    }

    public void setLayoutCategoryAttr(LayoutCategoryAttr layoutCategoryAttr) {
        this.layoutCategoryAttr = layoutCategoryAttr;
    }

    public LayoutCouponAttr getLayoutCouponAttr() {
        return layoutCouponAttr;
    }

    public void setLayoutCouponAttr(LayoutCouponAttr layoutCouponAttr) {
        this.layoutCouponAttr = layoutCouponAttr;
    }

    public LayoutBannerAttr getLayoutBannerAttr() {
        return layoutBannerAttr;
    }

    public void setLayoutBannerAttr(LayoutBannerAttr layoutBannerAttr) {
        this.layoutBannerAttr = layoutBannerAttr;
    }

    public LayoutCatalogAttr getLayoutCatalogAttr() {
        return layoutCatalogAttr;
    }

    public void setLayoutCatalogAttr(LayoutCatalogAttr layoutCatalogAttr) {
        this.layoutCatalogAttr = layoutCatalogAttr;
    }

    public LayoutTickerAttr getLayoutTickerAttr() {
        return layoutTickerAttr;
    }

    public void setLayoutTickerAttr(LayoutTickerAttr layoutTickerAttr) {
        this.layoutTickerAttr = layoutTickerAttr;
    }

}
