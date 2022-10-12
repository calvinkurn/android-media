package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SectionContent(
    @SerializedName("backgroundImgURLMobile")
    var backgroundImgURLMobile: String? = "",
    @SerializedName("layoutType")
    @Expose
    var layoutType: String = "",
    @SerializedName("sectionTitle")
    @Expose
    var sectionTitle: String? = null,
    @SerializedName("sectionSubTitle")
    @Expose
    var sectionSubTitle: String? = null,
    @SerializedName("countdownAttr")
    @Expose
    var countdownAttr: CountdownAttr? = null,
    @SerializedName("cta")
    @Expose
    var cta: Cta? = null,
    @SerializedName("layoutCategoryAttr")
    @Expose
    var layoutCategoryAttr: LayoutCategoryAttr? = null,
    @SerializedName("layoutCouponAttr")
    @Expose
    var layoutCouponAttr: LayoutCouponAttr? = null,
    @SerializedName("layoutBannerAttr")
    @Expose
    var layoutBannerAttr: LayoutBannerAttr? = null,
    @SerializedName("layoutCatalogAttr")
    @Expose
    var layoutCatalogAttr: LayoutCatalogAttr? = null,
    @SerializedName("layoutTickerAttr")
    @Expose
    var layoutTickerAttr: LayoutTickerAttr? = null,
    @SerializedName("layoutTopAdsAttr")
    @Expose
    var layoutTopAdsAttr: LayoutTopAdsAttr? = null,
    @SerializedName("layoutMerchantCouponAttr")
    @Expose
    var layoutMerchantCouponAttr: LayoutMerchantCouponAttr? = null,
    @SerializedName("layoutQuestWidgetAttr")
    @Expose
    var layoutQuestWidgetAttr: LayoutQuestWidgetAttr? = null,
)
