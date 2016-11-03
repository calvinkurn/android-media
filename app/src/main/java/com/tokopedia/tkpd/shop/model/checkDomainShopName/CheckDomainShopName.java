
package com.tokopedia.tkpd.shop.model.checkDomainShopName;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CheckDomainShopName {

    @SerializedName("message_error")
    @Expose
    List<String> messageError = new ArrayList<String>();
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("config")
    @Expose
    String config;
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;

    /**
     * 
     * @return
     *     The messageError
     */
    public List<String> getMessageError() {
        return messageError;
    }

    /**
     * 
     * @param messageError
     *     The message_error
     */
    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
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

}
