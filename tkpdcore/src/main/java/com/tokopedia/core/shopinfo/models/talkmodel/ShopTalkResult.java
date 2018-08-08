
package com.tokopedia.core.shopinfo.models.talkmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.PagingHandler;

import java.util.ArrayList;

public class ShopTalkResult {

    @SerializedName("list")
    @Expose
    private java.util.List<ShopTalk> list = new ArrayList<ShopTalk>();
    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;

    /**
     * @return The list
     */
    public java.util.List<ShopTalk> getList() {
        return list;
    }

    /**
     * @param list The list
     */
    public void setList(java.util.List<ShopTalk> list) {
        this.list = list;
    }

    /**
     * @return The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     * @param paging The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

}
