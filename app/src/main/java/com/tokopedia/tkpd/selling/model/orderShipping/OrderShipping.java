
package com.tokopedia.tkpd.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderShipping {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private OrderShippingData orderShippingData;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

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
     *     The orderShippingData
     */
    public OrderShippingData getOrderShippingData() {
        return orderShippingData;
    }

    /**
     * 
     * @param orderShippingData
     *     The orderShippingData
     */
    public void setOrderShippingData(OrderShippingData orderShippingData) {
        this.orderShippingData = orderShippingData;
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
