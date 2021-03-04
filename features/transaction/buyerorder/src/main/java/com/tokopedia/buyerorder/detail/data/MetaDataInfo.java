package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaDataInfo {

    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("entity_address")
    @Expose
    private EntityAddress entityAddress;
    @SerializedName("entity_packages")
    @Expose
    private List<EntityPackage> entityPackages;
    @SerializedName("entity_brand_name")
    @Expose
    private String entityBrandName;
    @SerializedName("entity_category_id")
    @Expose
    private int entityCategoryId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("entity_image")
    @Expose
    private String entityImage;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("entity_product_id")
    @Expose
    private int entityProductId;
    @SerializedName("entity_product_name")
    @Expose
    private String entityProductName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("entity_provider_id")
    @Expose
    private int entityProviderId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("total_ticket_count")
    @Expose
    private int totalTicketCount;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("total_ticket_price")
    @Expose
    private int totalTicketPrice;

    @SerializedName("total_price")
    @Expose
    private int totalPrice;

    @SerializedName("entity_passengers")
    @Expose
    private List<EntityPessenger> entityPessengers;

    @SerializedName("passenger_forms")
    @Expose
    private List<PassengerForm> passengerForms;

    @SerializedName("is_hiburan")
    @Expose
    private int isHiburan;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("location_name")
    @Expose
    private String locationName;

    @SerializedName("location_desc")
    @Expose
    private String locationDesc;

    @SerializedName("seo_url")
    @Expose
    private String seoUrl;

    @SerializedName("insurance_type")
    @Expose
    private String insuranceType;

    @SerializedName("product_quantity_fmt")
    @Expose
    private String productQuantity;

    @SerializedName("insurance_length_fmt")
    @Expose
    private String insuranceLength;

    @SerializedName("premium_price_fmt")
    @Expose
    private String premiumPrice;

    @SerializedName("product_image_url")
    @Expose
    private String prouductImage;

    @SerializedName("product_price_fmt")
    @Expose
    private String productPrice;

    @SerializedName("product_app_url")
    @Expose
    private String productAppUrl;

    @SerializedName("custom_link_app_url")
    @Expose
    private String customLinkAppUrl;

    @SerializedName("custom_link_label")
    @Expose
    private String customLinkLabel;

    @SerializedName("custom_link_type")
    @Expose
    private String customLinkType;


    public String getEndDate() {
        return endDate;
    }

    public String getEndTime(){
        return endTime;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getInsuranceLength() {
        return insuranceLength;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public String getPremiumPrice() {
        return premiumPrice;
    }

    public String getName() {
        return name;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public String getProuductImage() {
        return prouductImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public EntityAddress getEntityAddress() {
        return entityAddress;
    }

    public void setEntityAddress(EntityAddress entityAddress) {
        this.entityAddress = entityAddress;
    }

    public List<EntityPackage> getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(List<EntityPackage> entityPackages) {
        this.entityPackages = entityPackages;
    }

    public String getEntityBrandName() {
        return entityBrandName;
    }

    public void setEntityBrandName(String entityBrandName) {
        this.entityBrandName = entityBrandName;
    }

    public int getEntityCategoryId() {
        return entityCategoryId;
    }

    public void setEntityCategoryId(int entityCategoryId) {
        this.entityCategoryId = entityCategoryId;
    }

    public String getEntityImage() {
        return entityImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setEntityImage(String entityImage) {
        this.entityImage = entityImage;
    }

    public int getEntityProductId() {
        return entityProductId;
    }

    public void setEntityProductId(int entityProductId) {
        this.entityProductId = entityProductId;
    }

    public String getEntityProductName() {
        return entityProductName;
    }

    public void setEntityProductName(String entityProductName) {
        this.entityProductName = entityProductName;
    }

    public int getEntityProviderId() {
        return entityProviderId;
    }

    public void setEntityProviderId(int entityProviderId) {
        this.entityProviderId = entityProviderId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getTotalTicketCount() {
        return totalTicketCount;
    }

    public int getQuantity(){return quantity;}

    public void setTotalTicketCount(int totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public int getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalTicketPrice(int totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public EntityAddress getEntityaddress() {
        return entityAddress;
    }

    public void setEntity_address(EntityAddress entityaddress) {
        this.entityAddress = entityaddress;
    }

    public List<EntityPessenger> getEntityPessengers() {
        return entityPessengers;
    }

    public List<PassengerForm> getPassengerForms() {
        return passengerForms;
    }

    public void setEntityPessengers(List<EntityPessenger> entityPessengers) {
        this.entityPessengers = entityPessengers;
    }

    public int getIsHiburan() {
        return isHiburan;
    }

    public void setIsHiburan(int isHiburan) {
        this.isHiburan = isHiburan;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public String getEmail(){return email;}

    public String getProductAppUrl(){return productAppUrl;}

    public String getCustomLinkAppUrl(){ return customLinkAppUrl;}

    public String getCustomLinkLabel(){ return customLinkLabel;}

    public String getCustomLinkType(){ return customLinkType;}
}
