package com.tokopedia.sellerapp.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

/**
 * Created by m.normansyah on 05/01/2016.
 */
public class ShopDataModel {
    @SerializedName("message_error")
    @Expose
    String[] messageError;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ShopModel shopModel;
    @SerializedName("config")
    @Expose
    private String config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ShopModel getShopModel() {
        return shopModel;
    }

    public void setShopModel(ShopModel shopModel) {
        this.shopModel = shopModel;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public String[] getMessageError() {
        return messageError;
    }

    public void setMessageError(String[] messageError) {
        this.messageError = messageError;
    }
}
