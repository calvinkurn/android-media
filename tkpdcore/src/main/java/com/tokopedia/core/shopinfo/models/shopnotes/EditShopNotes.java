
package com.tokopedia.core.shopinfo.models.shopnotes;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditShopNotes {

    @SerializedName("message_error")
    @Expose
    private List<String> messageError = new ArrayList<String>();
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message_status")
    @Expose
    private List<String> messageStatus = new ArrayList<String>();
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

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
     *     The messageStatus
     */
    public List<String> getMessageStatus() {
        return messageStatus;
    }

    /**
     * 
     * @param messageStatus
     *     The message_status
     */
    public void setMessageStatus(List<String> messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     * 
     * @return
     *     The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     *     The config
     */
    public void setConfig(Object config) {
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

    public static class Data {

        @SerializedName("is_success")
        @Expose
        private Integer isSuccess;

        /**
         *
         * @return
         *     The isSuccess
         */
        public Integer getIsSuccess() {
            return isSuccess;
        }

        /**
         *
         * @param isSuccess
         *     The is_success
         */
        public void setIsSuccess(Integer isSuccess) {
            this.isSuccess = isSuccess;
        }

    }
}
