
package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopNote {

    @SerializedName("shop_note_id")
    @Expose
    private long shopNoteId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("position")
    @Expose
    private long position;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    public long getShopNoteId() {
        return shopNoteId;
    }

    public void setShopNoteId(long shopNoteId) {
        this.shopNoteId = shopNoteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}

