package com.tokopedia.core.drawer2.data.pojo.topcash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashData {

    @SerializedName("action")
    @Expose
    private Action mAction;
    @SerializedName("balance")
    @Expose
    private String mBalance;
    @SerializedName("redirect_url")
    @Expose
    private String mRedirectUrl;
    @SerializedName("text")
    @Expose
    private String mText;
    @SerializedName("wallet_id")
    @Expose
    private Long mWalletId;
    @SerializedName("link")
    @Expose
    private int link;


    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String redirect_url) {
        mRedirectUrl = redirect_url;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Long getWalletId() {
        return mWalletId;
    }

    public void setWalletId(Long wallet_id) {
        mWalletId = wallet_id;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }
}
