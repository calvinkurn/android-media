package com.tokopedia.feedplus.view.viewmodel.feeddetail;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailHeaderViewModel implements Visitable<FeedPlusDetailTypeFactory> {

    private Integer shopId;
    private String shopName;
    private String shopAvatar;
    private boolean isGoldMerchant;
    private String time;
    private boolean isOfficialStore;
    private String shopUrl;
    private String shareLinkURL;
    private String shareLinkDescription;
    private String actionText;
    private String activityId;

    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public FeedDetailHeaderViewModel(Integer shopId,
                                     String shopName,
                                     String shopAvatar,
                                     boolean isGoldMerchant,
                                     String time,
                                     boolean isOfficialStore,
                                     String shopUrl,
                                     String shareLinkURL,
                                     String shareLinkDescription,
                                     String actionText,
                                     String activityId) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAvatar = shopAvatar;
        this.isGoldMerchant = isGoldMerchant;
        this.time = time;
        this.isOfficialStore = isOfficialStore;
        this.shopUrl = shopUrl;
        this.shareLinkURL = shareLinkURL;
        this.shareLinkDescription = shareLinkDescription;
        this.actionText = actionText;
        this.activityId = activityId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean officialStore) {
        isOfficialStore = officialStore;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShareLinkURL() {
        return shareLinkURL;
    }

    public void setShareLinkURL(String shareLinkURL) {
        this.shareLinkURL = shareLinkURL;
    }

    public String getShareLinkDescription() {
        return shareLinkDescription;
    }

    public void setShareLinkDescription(String shareLinkDescription) {
        this.shareLinkDescription = shareLinkDescription;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
