package com.tokopedia.core.gcm.model;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class ApplinkNotificationPass {
    private String title;
    private String description;
    private String ticker;
    private String applink;
    private String iconUrl;
    private String imageUrl;
    private Intent intent;
    private int notificationId;
    private List<String> contents;

    public ApplinkNotificationPass() {
    }

    private ApplinkNotificationPass(String title,
                                    String description,
                                    String ticker,
                                    String applink,
                                    Intent intent,
                                    String iconUrl,
                                    String imageUrl,
                                    int notificationId,
                                    List<String> contents) {
        this.title = title;
        this.description = description;
        this.ticker = ticker;
        this.applink = applink;
        this.intent = intent;
        this.contents = contents;
        this.iconUrl = iconUrl;
        this.notificationId = notificationId;
        this.imageUrl = imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public static class ApplinkNotificationPassBuilder {
        private String nestedTitle;
        private String nestedDescription;
        private String nestedTicker;
        private String nestedApplink;
        private String nestedIconUrl;
        private String nestedImageUrl;
        private Intent nestedIntent;
        private List<String> nestedContents;
        private int nestedNotificationId;

        private ApplinkNotificationPassBuilder() {
        }

        public static ApplinkNotificationPassBuilder builder() {
            return new ApplinkNotificationPassBuilder();
        }

        public ApplinkNotificationPassBuilder title(final String title) {
            this.nestedTitle = title;
            return this;
        }

        public ApplinkNotificationPassBuilder description(final String description) {
            this.nestedDescription = description;
            return this;
        }

        public ApplinkNotificationPassBuilder ticker(final String ticker) {
            this.nestedTicker = ticker;
            return this;
        }

        public ApplinkNotificationPassBuilder applink(final String applink) {
            this.nestedApplink = applink;
            return this;
        }

        public ApplinkNotificationPassBuilder image(final String image) {
            this.nestedImageUrl = image;
            return this;
        }

        public ApplinkNotificationPassBuilder icon(final String icon) {
            this.nestedIconUrl = icon;
            return this;
        }

        public ApplinkNotificationPassBuilder intent(final Intent intent) {
            this.nestedIntent = intent;
            return this;
        }

        public ApplinkNotificationPassBuilder contents(final List<String> contents) {
            this.nestedContents = contents;
            return this;
        }

        public ApplinkNotificationPassBuilder id(final int notificationId) {
            this.nestedNotificationId = notificationId;
            return this;
        }

        public ApplinkNotificationPass build() {
            return new ApplinkNotificationPass(
                    nestedTitle,
                    nestedDescription,
                    nestedTicker,
                    nestedApplink,
                    nestedIntent,
                    nestedIconUrl,
                    nestedImageUrl,
                    nestedNotificationId,
                    nestedContents
            );
        }
    }
}
