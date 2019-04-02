package com.tokopedia.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ShopFeedDomain {

    private
    @Nullable
    Integer id;

    private
    @Nullable
    String name;

    private
    @Nullable
    String avatar;

    private
    @Nullable
    Boolean isOfficial;

    private
    @Nullable
    Boolean isGold;

    private
    @Nullable
    String url;

    private
    @Nullable
    String shopLink;

    private
    @Nullable
    String shareLinkDescription;

    private
    @Nullable
    String shareLinkURL;


    public ShopFeedDomain(@Nullable Integer id, @Nullable String name, @Nullable String avatar,
                          @Nullable Boolean isOfficial, @Nullable Boolean isGold, @Nullable String
                                  url,
                          @Nullable String shopLink, @Nullable String shareLinkDescription,
                          @Nullable String shareLinkURL) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOfficial = isOfficial;
        this.isGold = isGold;
        this.url = url;
        this.shopLink = shopLink;
        this.shareLinkDescription = shareLinkDescription;
        this.shareLinkURL = shareLinkURL;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public Boolean getOfficial() {
        return isOfficial;
    }

    public void setOfficial(@Nullable Boolean official) {
        isOfficial = official;
    }

    @Nullable
    public Boolean getGold() {
        return isGold;
    }

    public void setGold(@Nullable Boolean gold) {
        isGold = gold;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getShopLink() {
        return shopLink;
    }

    public void setShopLink(@Nullable String shopLink) {
        this.shopLink = shopLink;
    }

    @Nullable
    public String getShareLinkDescription() {
        return shareLinkDescription;
    }

    public void setShareLinkDescription(@Nullable String shareLinkDescription) {
        this.shareLinkDescription = shareLinkDescription;
    }

    @Nullable
    public String getShareLinkURL() {
        return shareLinkURL;
    }

    public void setShareLinkURL(@Nullable String shareLinkURL) {
        this.shareLinkURL = shareLinkURL;
    }
}
