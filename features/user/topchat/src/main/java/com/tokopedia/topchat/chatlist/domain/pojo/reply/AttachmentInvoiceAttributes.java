package com.tokopedia.topchat.chatlist.domain.pojo.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachmentInvoiceAttributes {
    @SerializedName("code")
    @Expose
    String code;
    @SerializedName("create_time")
    @Expose
    String createdTime;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("href_url")
    @Expose
    String url;
    @SerializedName("id")
    @Expose
    Long id;
    @SerializedName("image_url")
    @Expose
    String imageUrl;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("status_id")
    @Expose
    int statusId;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("total_amount")
    @Expose
    String amount;

    public AttachmentInvoiceAttributes() {
    }

    public AttachmentInvoiceAttributes(String code, String createdTime, String description, String url, Long id, String imageUrl, String status, int statusId, String title, String amount) {
        this.code = code;
        this.createdTime = createdTime;
        this.description = description;
        this.url = url;
        this.id = id;
        this.imageUrl = imageUrl;
        this.status = status;
        this.statusId = statusId;
        this.title = title;
        this.amount = amount;
    }

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
