
package com.tokopedia.core.drawer.model.topcastItem;


import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("action")
    private Action mAction;
    @SerializedName("balance")
    private String mBalance;
    @SerializedName("redirect_url")
    private String mRedirectUrl;
    @SerializedName("text")
    private String mText;
    @SerializedName("wallet_id")
    private Long mWalletId;


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

}
