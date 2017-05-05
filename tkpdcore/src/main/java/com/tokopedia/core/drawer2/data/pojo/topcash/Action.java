
package com.tokopedia.core.drawer2.data.pojo.topcash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author kulomady on 11/08/16
 */
public class Action {

    @SerializedName("redirect_url")
    @Expose
    private String mRedirectUrl;
    @SerializedName("text")
    @Expose
    private String mText;
    @SerializedName("type")
    @Expose
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
