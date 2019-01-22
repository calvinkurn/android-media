package com.tokopedia.digital.widget.view.model;

/**
 * Created by Rizky on 13/11/18.
 */
public class Recommendation {

    private String iconUrl;
    private String title;
    private String clientNumber;
    private String applink;
    private String webLink;
    private int categoryId;
    private String categoryName;
    private int productId;
    private String productName;
    private String type;
    private int position;

    public Recommendation(String iconUrl, String title, String clientNumber, String applink,
                          String webLink, int categoryId, String categoryName,
                          int productId, String productName, String type, int position) {
        this.iconUrl = iconUrl;
        this.title = title;
        this.clientNumber = clientNumber;
        this.applink = applink;
        this.webLink = webLink;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.position = position;
    }

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
