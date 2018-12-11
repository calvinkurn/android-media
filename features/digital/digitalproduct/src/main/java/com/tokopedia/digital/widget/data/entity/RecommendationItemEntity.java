package com.tokopedia.digital.widget.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 14/11/18.
 */
public class RecommendationItemEntity {

    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("clientNumber")
    @Expose
    private String clientNumber;

    @SerializedName("appLink")
    @Expose
    private String applink;

    @SerializedName("webLink")
    @Expose
    private String webLink;

    @SerializedName("categoryId")
    @Expose
    private int categoryId;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("productId")
    @Expose
    private int productId;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("position")
    @Expose
    private int position;

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getApplink() {
        return applink;
    }

    public String getWebLink() {
        return webLink;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

}
