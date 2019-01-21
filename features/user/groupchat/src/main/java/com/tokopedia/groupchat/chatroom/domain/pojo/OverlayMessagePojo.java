package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 18/12/18.
 */
public class OverlayMessagePojo {
    
    @SerializedName("type_message")
    @Expose
    private String typeMessage;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("assets")
    @Expose
    private OverlayMessageAssetPojo assets;
    @SerializedName("closeable")
    @Expose
    private boolean closeable;

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OverlayMessageAssetPojo getAssets() {
        return assets;
    }

    public void setAssets(OverlayMessageAssetPojo assets) {
        this.assets = assets;
    }

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

}
