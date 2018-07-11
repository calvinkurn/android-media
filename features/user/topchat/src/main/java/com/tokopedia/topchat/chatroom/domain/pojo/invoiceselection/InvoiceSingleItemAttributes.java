package com.tokopedia.topchat.chatroom.domain.pojo.invoiceselection;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 16/05/18.
 */
public class InvoiceSingleItemAttributes {

    @SerializedName("code")
    private String code;
    @SerializedName("create_time")
    private String createdTime;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;
    @SerializedName("id")
    private Long id;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("status")
    private String status;
    @SerializedName("status_id")
    private int statusId;
    @SerializedName("title")
    private String title;
    @SerializedName("total_amount")
    private  String amount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
