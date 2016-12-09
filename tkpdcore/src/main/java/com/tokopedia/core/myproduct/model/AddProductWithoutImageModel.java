
package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductWithoutImageModel {

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

        @SerializedName("product_primary_pic")
        @Expose
        private Object productPrimaryPic;
        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("product_desc")
        @Expose
        private String productDesc;
        @SerializedName("product_etalase")
        @Expose
        private String productEtalase;
        @SerializedName("product_dest")
        @Expose
        private String productDest;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_url")
        @Expose
        private String productUrl;

        /**
         *
         * @return
         *     The productPrimaryPic
         */
        public Object getProductPrimaryPic() {
            return productPrimaryPic;
        }

        /**
         *
         * @param productPrimaryPic
         *     The product_primary_pic
         */
        public void setProductPrimaryPic(Object productPrimaryPic) {
            this.productPrimaryPic = productPrimaryPic;
        }

        /**
         *
         * @return
         *     The productId
         */
        public Integer getProductId() {
            return productId;
        }

        /**
         *
         * @param productId
         *     The product_id
         */
        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        /**
         *
         * @return
         *     The productDesc
         */
        public String getProductDesc() {
            return productDesc;
        }

        /**
         *
         * @param productDesc
         *     The product_desc
         */
        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        /**
         *
         * @return
         *     The productEtalase
         */
        public String getProductEtalase() {
            return productEtalase;
        }

        /**
         *
         * @param productEtalase
         *     The product_etalase
         */
        public void setProductEtalase(String productEtalase) {
            this.productEtalase = productEtalase;
        }

        /**
         *
         * @return
         *     The productDest
         */
        public String getProductDest() {
            return productDest;
        }

        /**
         *
         * @param productDest
         *     The product_dest
         */
        public void setProductDest(String productDest) {
            this.productDest = productDest;
        }

        /**
         *
         * @return
         *     The productName
         */
        public String getProductName() {
            return productName;
        }

        /**
         *
         * @param productName
         *     The product_name
         */
        public void setProductName(String productName) {
            this.productName = productName;
        }

        /**
         *
         * @return
         *     The productUrl
         */
        public String getProductUrl() {
            return productUrl;
        }

        /**
         *
         * @param productUrl
         *     The product_url
         */
        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

    }

}
