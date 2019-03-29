
package com.tokopedia.core.shop.model.openShopSubmitData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Parcel
public class OpenShopSubmitData {

    @SerializedName("message_error")
    @Expose
    List<String> messageError = new ArrayList<String>();
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;
    @SerializedName("config")
    @Expose
    String config;


    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     * 
     * @param serverProcessTime
     *     The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    /**
     * 
     * @return
     *     The config
     */
    public String getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     *     The config
     */
    public void setConfig(String config) {
        this.config = config;
    }

}
