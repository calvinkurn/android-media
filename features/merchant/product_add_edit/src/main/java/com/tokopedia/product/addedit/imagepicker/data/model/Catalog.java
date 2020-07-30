
package com.tokopedia.product.addedit.imagepicker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Catalog {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("department_id")
    @Expose
    private String departmentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("update_time")
    @Expose
    private String updateTime;
    @SerializedName("catalog_image")
    @Expose
    private List<CatalogImage> catalogImage = null;
    @SerializedName("market_price")
    @Expose
    private List<MarketPrice> marketPrice = null;
    @SerializedName("expert_review")
    @Expose
    private List<Object> expertReview = null;
    @SerializedName("specification")
    @Expose
    private List<Specification> specification = null;
    @SerializedName("long_description")
    @Expose
    private List<Object> longDescription = null;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<CatalogImage> getCatalogImage() {
        return catalogImage;
    }

    public void setCatalogImage(List<CatalogImage> catalogImage) {
        this.catalogImage = catalogImage;
    }

    public List<MarketPrice> getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(List<MarketPrice> marketPrice) {
        this.marketPrice = marketPrice;
    }

    public List<Object> getExpertReview() {
        return expertReview;
    }

    public void setExpertReview(List<Object> expertReview) {
        this.expertReview = expertReview;
    }

    public List<Specification> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Specification> specification) {
        this.specification = specification;
    }

    public List<Object> getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(List<Object> longDescription) {
        this.longDescription = longDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
