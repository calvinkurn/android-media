package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SectionContent(
    @SerializedName("backgroundImgURLMobile")
    var backgroundImgURLMobile: String = "",
    @SerializedName("layoutType")
    @Expose
    var layoutType: String = "",
    @SerializedName("sectionTitle")
    @Expose
    var sectionTitle: String = "",
    @SerializedName("sectionSubTitle")
    @Expose
    var sectionSubTitle: String = "",
    @SerializedName("countdownAttr")
    @Expose
    var countdownAttr: CountdownAttr = CountdownAttr(),
    @SerializedName("cta")
    @Expose
    var cta: Cta = Cta(),
    @SerializedName("layoutCategoryAttr")
    @Expose
    var layoutCategoryAttr: LayoutCategoryAttr = LayoutCategoryAttr(),
    @SerializedName("layoutCouponAttr")
    @Expose
    var layoutCouponAttr: LayoutCouponAttr = LayoutCouponAttr(),
    @SerializedName("layoutBannerAttr")
    @Expose
    var layoutBannerAttr: LayoutBannerAttr = LayoutBannerAttr(),
    @SerializedName("layoutCatalogAttr")
    @Expose
    var layoutCatalogAttr: LayoutCatalogAttr = LayoutCatalogAttr(),
    @SerializedName("layoutTickerAttr")
    @Expose
    var layoutTickerAttr: LayoutTickerAttr = LayoutTickerAttr(),
    @SerializedName("layoutTopAdsAttr")
    @Expose
    var layoutTopAdsAttr: LayoutTopAdsAttr = LayoutTopAdsAttr(),
    @SerializedName("layoutMerchantCouponAttr")
    @Expose
    var layoutMerchantCouponAttr: LayoutMerchantCouponAttr = LayoutMerchantCouponAttr(),
    @SerializedName("layoutQuestWidgetAttr")
    @Expose
    var layoutQuestWidgetAttr: LayoutQuestWidgetAttr = LayoutQuestWidgetAttr(),
)
