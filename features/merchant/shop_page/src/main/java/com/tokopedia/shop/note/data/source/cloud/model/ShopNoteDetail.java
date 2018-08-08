package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetail {

    @SerializedName("notes")
    @Expose
    private Notes notes;
    @SerializedName("shop_id")
    @Expose
    private long shopId;

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }
}
