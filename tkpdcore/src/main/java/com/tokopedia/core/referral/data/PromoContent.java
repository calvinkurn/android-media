package com.tokopedia.core.referral.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 08/11/17.
 */

public class PromoContent {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("friend_count")
    @Expose
    private String friendCount;

    @SerializedName("url")
    @Expose
    private String shareUrl;

    @SerializedName("promo_benefit")
    @Expose
    private PromoBenefit promoBenefit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public PromoBenefit getPromoBenefit() {
        return promoBenefit;
    }

    public void setPromoBenefit(PromoBenefit promoBenefit) {
        this.promoBenefit = promoBenefit;
    }
}
