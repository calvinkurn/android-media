
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class TokenAsset {

    @SerializedName("backgroundImgURL")
    private String mBackgroundImgURL;
    @SerializedName("glowImgURL")
    private String mGlowImgURL;
    @SerializedName("glowShadowImgURL")
    private String mGlowShadowImgURL;
    @SerializedName("imageURL")
    private String mImageURL;
    @SerializedName("imageV2URLs")
    private List<String> mImageV2URLs;
    @SerializedName("seamlessImgURL")
    private String mSeamlessImgURL;

    public String getBackgroundImgURL() {
        return mBackgroundImgURL;
    }

    public void setBackgroundImgURL(String backgroundImgURL) {
        mBackgroundImgURL = backgroundImgURL;
    }

    public String getGlowImgURL() {
        return mGlowImgURL;
    }

    public void setGlowImgURL(String glowImgURL) {
        mGlowImgURL = glowImgURL;
    }

    public String getGlowShadowImgURL() {
        return mGlowShadowImgURL;
    }

    public void setGlowShadowImgURL(String glowShadowImgURL) {
        mGlowShadowImgURL = glowShadowImgURL;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public List<String> getImageV2URLs() {
        return mImageV2URLs;
    }

    public void setImageV2URLs(List<String> imageV2URLs) {
        mImageV2URLs = imageV2URLs;
    }

    public String getSeamlessImgURL() {
        return mSeamlessImgURL;
    }

    public void setSeamlessImgURL(String seamlessImgURL) {
        mSeamlessImgURL = seamlessImgURL;
    }

    public String getVersion() {
        return "1";
    }
}
