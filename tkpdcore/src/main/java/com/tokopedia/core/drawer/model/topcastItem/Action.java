
package com.tokopedia.core.drawer.model.topcastItem;

import com.google.gson.annotations.SerializedName;

/**
 * @author kulomady on 11/08/16
 */
public class Action {

    @SerializedName("redirect_url")
    private String mRedirectUrl;
    @SerializedName("text")
    private String mText;
    @SerializedName("type")
    private String mType;

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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}
