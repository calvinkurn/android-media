
package com.tokopedia.topads.keyword.data.model.cloud.bulkkeyword;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataBulkKeyword {

    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("keywords")
    @Expose
    private List<Keyword> keyword = null;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Keyword> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<Keyword> keyword) {
        this.keyword = keyword;
    }

}
