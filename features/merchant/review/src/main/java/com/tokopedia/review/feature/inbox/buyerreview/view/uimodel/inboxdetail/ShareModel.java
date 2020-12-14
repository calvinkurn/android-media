package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

/**
 * @author by nisie on 9/28/17.
 */

public class ShareModel {
    private String title;
    private String content;
    private String link;
    private String image;

    public ShareModel(String title, String content, String link, String image) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }
}
