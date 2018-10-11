
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("affiliateTrackID")
    @Expose
    private String affiliateTrackId;
    @SerializedName("captionEng")
    @Expose
    private String captionEng;
    @SerializedName("captionInd")
    @Expose
    private String captionInd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAffiliateTrackId() {
        return affiliateTrackId;
    }

    public void setAffiliateTrackId(String affiliateTrackId) {
        this.affiliateTrackId = affiliateTrackId;
    }

    public String getCaptionEng() {
        return captionEng;
    }

    public void setCaptionEng(String captionEng) {
        this.captionEng = captionEng;
    }

    public String getCaptionInd() {
        return captionInd;
    }

    public void setCaptionInd(String captionInd) {
        this.captionInd = captionInd;
    }
}
