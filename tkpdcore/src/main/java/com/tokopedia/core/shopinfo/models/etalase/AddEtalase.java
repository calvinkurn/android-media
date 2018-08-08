
package com.tokopedia.core.shopinfo.models.etalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddEtalase {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("message_error")
    @Expose
    private List<String> messageError = new ArrayList<String>();

    public List<String> getMessageError() {
        return messageError;
    }

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

        @SerializedName("etalase_id")
        @Expose
        private String etalaseId;
        @SerializedName("is_success")
        @Expose
        private Integer isSuccess;

        /**
         *
         * @return
         *     The etalaseId
         */
        public String getEtalaseId() {
            return etalaseId;
        }

        /**
         *
         * @param etalaseId
         *     The etalase_id
         */
        public void setEtalaseId(String etalaseId) {
            this.etalaseId = etalaseId;
        }

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
