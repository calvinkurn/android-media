package com.tokopedia.opportunity.domain.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hadi-putra on 27/03/18.
 */

public class OpportunityProduct {
    @SerializedName("detail_id")
    @Expose
    private int detailId;
    @SerializedName("detail_product_desc")
    @Expose
    private String productDescription;
    @SerializedName("detail_product_url")
    @Expose
    private String productUrl;
    @SerializedName("detail_product_name")
    @Expose
    private String productName;
    @SerializedName("detail_product_picture")
    @Expose
    private String productPicture;
    @SerializedName("detail_product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("detail_product_weight")
    @Expose
    private String productWeight;
    @SerializedName("detail_product_total_weight")
    @Expose
    private String productTotalWeight;
    @SerializedName("detail_product_notes")
    @Expose
    private String productNotes;
    @SerializedName("detail_product_price")
    @Expose
    private String productPrice;
    @SerializedName("detail_product_pictures")
    @Expose
    private List<String> productPictures = null;

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductTotalWeight() {
        return productTotalWeight;
    }

    public void setProductTotalWeight(String productTotalWeight) {
        this.productTotalWeight = productTotalWeight;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<String> getProductPictures() {
        return productPictures;
    }

    public void setProductPictures(List<String> productPictures) {
        this.productPictures = productPictures;
    }
}
