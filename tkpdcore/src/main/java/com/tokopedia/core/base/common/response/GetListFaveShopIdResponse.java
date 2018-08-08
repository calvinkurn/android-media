package com.tokopedia.core.base.common.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author kulomady on 12/09/2016
 */
@SuppressWarnings("unused")
public class GetListFaveShopIdResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private ListFaveShopId data;

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

    public ListFaveShopId getData() {
        return data;
    }

    public void setData(ListFaveShopId data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return serverProcessTime;
    }

    public void setServer_process_time(String server_process_time) {
        this.serverProcessTime = server_process_time;
    }

    public static class ListFaveShopId {
        public ListFaveShopId() {
        }

        @SerializedName("shop_id_list")
        @Expose
        ArrayList<String> shopIdList;

        public ArrayList<String> getShop_id_list() {
            return shopIdList;
        }

        public void setShop_id_list(ArrayList<String> shop_id_list) {
            this.shopIdList = shop_id_list;
        }
 }

}
