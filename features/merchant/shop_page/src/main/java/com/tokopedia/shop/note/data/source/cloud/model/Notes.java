package com.tokopedia.shop.note.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class Notes {
    @SerializedName("shop_note_id")
    @Expose
    private long shopNoteId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("position")
    @Expose
    private long position;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
