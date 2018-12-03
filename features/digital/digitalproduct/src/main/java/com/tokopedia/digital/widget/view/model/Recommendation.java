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
    private String categoryId;

    public Recommendation(String iconUrl, String title, String clientNumber, String applink,
                          String webLink, String categoryId) {
        this.iconUrl = iconUrl;
        this.title = title;
        this.clientNumber = clientNumber;
        this.applink = applink;
        this.webLink = webLink;
        this.categoryId = categoryId;
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

    public String getCategoryId() {
        return categoryId;
    }
}
