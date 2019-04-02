package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Vishal Gupta 7th May, 2018
 */
public class RechargeCategoryDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("is_new")
    @Expose
    private boolean isNew;
    @SerializedName("instant_checkout")
    @Expose
    private boolean instantCheckout;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("microsite_url")
    @Expose
    private String micrositeUrl;
    @SerializedName("default_operator_id")
    @Expose
    private String defaultOperatorId;
    @SerializedName("operator_style")
    @Expose
    private String operatorStyle;
    @SerializedName("operator_label")
    @Expose
    private String operatorLabel;
    @SerializedName("additional_feature")
    @Expose
    private AdditionalFeature additionalFeature;
    @SerializedName("client_number")
    @Expose
    private ClientNumber clientNumber;
    @SerializedName("operator")
    @Expose
    private List<OperatorBannerEntity> operators;
    @SerializedName("banners")
    @Expose
    private List<OperatorBannerEntity> banners;
    @SerializedName("other_banners")
    @Expose
    private List<OperatorBannerEntity> otherBanners;
    @SerializedName("guides")
    @Expose
    private List<GuideEntity> guides;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public boolean getInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(Boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMicrositeUrl() {
        return micrositeUrl;
    }

    public void setMicrositeUrl(String micrositeUrl) {
        this.micrositeUrl = micrositeUrl;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public void setOperatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
    }

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public void setOperatorLabel(String operatorLabel) {
        this.operatorLabel = operatorLabel;
    }

    public AdditionalFeature getAdditionalFeature() {
        return additionalFeature;
    }

    public ClientNumber getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(ClientNumber clientNumber) {
        this.clientNumber = clientNumber;
    }

    public List<OperatorBannerEntity> getOperators() {
        return operators;
    }

    public void setOperators(List<OperatorBannerEntity> operators) {
        this.operators = operators;
    }

    public List<OperatorBannerEntity> getBanners() {
        return banners;
    }

    public void setBanners(List<OperatorBannerEntity> banners) {
        this.banners = banners;
    }

    public List<OperatorBannerEntity> getOtherBanners() {
        return otherBanners;
    }

    public void setOtherBanners(List<OperatorBannerEntity> otherBanners) {
        this.otherBanners = otherBanners;
    }

    public List<GuideEntity> getGuides() {
        return guides;
    }

    public void setGuides(List<GuideEntity> guides) {
        this.guides = guides;
    }
}
