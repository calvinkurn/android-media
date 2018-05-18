package com.tokopedia.topchat.chatroom.domain.pojo.invoicesent;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 16/05/18.
 */
public class InvoiceLinkAttributePojo {

    @SerializedName("code")
    private String code;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("description")
    private String description;
    @SerializedName("href_url")
    private String hrefUrl;
    @SerializedName("id")
    private long id;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("status")
    private String status;
    @SerializedName("status_id")
    private int statusId;
    @SerializedName("title")
    private String title;
    @SerializedName("total_amount")
    private String totalAmount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public void setHrefUrl(String hrefUrl) {
        this.hrefUrl = hrefUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
