package com.tokopedia.sellerapp.home.model.deposit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepositModel {

    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("server_process_time")
    @Expose
    private Double serverProcessTime;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     *
     * @param config
     * The config
     */
    public void setConfig(Object config) {
        this.config = config;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The serverProcessTime
     */
    public Double getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     *
     * @param serverProcessTime
     * The server_process_time
     */
    public void setServerProcessTime(Double serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    public static class Data {

        @SerializedName("deposit_total")
        @Expose
        private String depositTotal;

        /**
         *
         * @return
         * The depositTotal
         */
        public String getDepositTotal() {
            return depositTotal;
        }

        /**
         *
         * @param depositTotal
         * The deposit_total
         */
        public void setDepositTotal(String depositTotal) {
            this.depositTotal = depositTotal;
        }

    }
}