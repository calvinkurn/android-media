package com.tokopedia.core.gcm.model;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

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
    private String imageUrl;
    private String bannerUrl;
    private Intent intent;
    private int notificationId;
    private List<String> contents;
    private String info;
    private String category;
    private String group;
    private boolean multiSender;
    private TaskStackBuilder taskStackBuilder;

    public ApplinkNotificationPass() {
    }

    public ApplinkNotificationPass(String title,
                                   String description,
                                   String ticker,
                                   String applink,
                                   String imageUrl,
                                   String bannerUrl,
                                   Intent intent,
                                   int notificationId,
                                   List<String> contents,
                                   String info,
                                   String category,
                                   String group,
                                   boolean multiSender,
                                   TaskStackBuilder taskStackBuilder) {
        this.title = title;
        this.description = description;
        this.ticker = ticker;
        this.applink = applink;
        this.imageUrl = imageUrl;
        this.intent = intent;
        this.notificationId = notificationId;
        this.contents = contents;
        this.info = info;
        this.category = category;
        this.group = group;
        this.multiSender = multiSender;
        this.taskStackBuilder = taskStackBuilder;
        this.bannerUrl = bannerUrl;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isMultiSender() {
        return multiSender;
    }

    public void setMultiSender(boolean multiSender) {
        this.multiSender = multiSender;
    }

    public TaskStackBuilder getTaskStackBuilder() {
        return taskStackBuilder;
    }

    public void setTaskStackBuilder(TaskStackBuilder taskStackBuilder) {
        this.taskStackBuilder = taskStackBuilder;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public static class ApplinkNotificationPassBuilder {
        private String nestedTitle;
        private String nestedDescription;
        private String nestedTicker;
        private String nestedApplink;
        private String nestedImageUrl;
        private String nestedBannerUrl;
        private Intent nestedIntent;
        private List<String> nestedContents;
        private int nestedNotificationId;
        private String nestedInfo;
        private String nestedCategory;
        private String nestedGroup;
        private boolean nestedMultipleSender;
        private TaskStackBuilder nestedTaskStackBuilder;

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

        public ApplinkNotificationPassBuilder banner(final String banner) {
            this.nestedBannerUrl = banner;
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

        public ApplinkNotificationPassBuilder info(final String info) {
            this.nestedInfo = info;
            return this;
        }

        public ApplinkNotificationPassBuilder group(final String group) {
            this.nestedGroup = group;
            return this;
        }

        public ApplinkNotificationPassBuilder category(final String category) {
            this.nestedCategory = category;
            return this;
        }

        public ApplinkNotificationPassBuilder multipleSender(final boolean multipleSender) {
            this.nestedMultipleSender = multipleSender;
            return this;
        }

        public ApplinkNotificationPassBuilder taskStackBuilder(final TaskStackBuilder taskStackBuilder) {
            this.nestedTaskStackBuilder = taskStackBuilder;
            return this;
        }

        public ApplinkNotificationPass build() {
            return new ApplinkNotificationPass(
                    nestedTitle,
                    nestedDescription,
                    nestedTicker,
                    nestedApplink,
                    nestedImageUrl,
                    nestedBannerUrl,
                    nestedIntent,
                    nestedNotificationId,
                    nestedContents,
                    nestedInfo,
                    nestedCategory,
                    nestedGroup,
                    nestedMultipleSender,
                    nestedTaskStackBuilder
            );
        }
    }
}
