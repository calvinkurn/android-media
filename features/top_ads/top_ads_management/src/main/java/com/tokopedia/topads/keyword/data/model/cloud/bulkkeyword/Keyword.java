
package com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keyword {

    @SerializedName("keyword_id")
    @Expose
    private String keywordId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("price_bid")
    @Expose
    private double priceBid;
    @SerializedName("keyword_type_id")
    @Expose
    private String keywordTypeId;

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public double getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(double priceBid) {
        this.priceBid = priceBid;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }
}
