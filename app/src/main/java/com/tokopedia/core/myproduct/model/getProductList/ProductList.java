
package com.tokopedia.core.myproduct.model.getProductList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductList {

    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public static class Data {

        @SerializedName("is_product_manager")
        @Expose
        private String isProductManager;
        @SerializedName("default_sort")
        @Expose
        private Integer defaultSort;
        @SerializedName("total_data")
        @Expose
        private Object totalData;
        @SerializedName("paging")
        @Expose
        private Paging paging;
        @SerializedName("list")
        @Expose
        private List<Product> list = new ArrayList<Product>();
        @SerializedName("etalase_name")
        @Expose
        private String etalaseName;
        @SerializedName("shop_is_gold")
        @Expose
        private Integer shopIsGold;
        @SerializedName("etalase_id")
        @Expose
        private Object etalaseId;
        @SerializedName("is_inbox_manager")
        @Expose
        private Integer isInboxManager;
        @SerializedName("is_tx_manager")
        @Expose
        private Integer isTxManager;

        /**
         *
         * @return
         *     The isProductManager
         */
        public String getIsProductManager() {
            return isProductManager;
        }

        /**
         *
         * @param isProductManager
         *     The is_product_manager
         */
        public void setIsProductManager(String isProductManager) {
            this.isProductManager = isProductManager;
        }

        /**
         *
         * @return
         *     The defaultSort
         */
        public Integer getDefaultSort() {
            return defaultSort;
        }

        /**
         *
         * @param defaultSort
         *     The default_sort
         */
        public void setDefaultSort(Integer defaultSort) {
            this.defaultSort = defaultSort;
        }

        /**
         *
         * @return
         *     The totalData
         */
        public Object getTotalData() {
            return totalData;
        }

        /**
         *
         * @param totalData
         *     The total_data
         */
        public void setTotalData(Object totalData) {
            this.totalData = totalData;
        }

        /**
         *
         * @return
         *     The paging
         */
        public Paging getPaging() {
            return paging;
        }

        /**
         *
         * @param paging
         *     The paging
         */
        public void setPaging(Paging paging) {
            this.paging = paging;
        }

        public List<Product> getList() {
            return list;
        }

        public void setList(List<Product> list) {
            this.list = list;
        }

        /**
         *
         * @return
         *     The etalaseName
         */
        public String getEtalaseName() {
            return etalaseName;
        }

        /**
         *
         * @param etalaseName
         *     The etalase_name
         */
        public void setEtalaseName(String etalaseName) {
            this.etalaseName = etalaseName;
        }

        /**
         *
         * @return
         *     The shopIsGold
         */
        public Integer getShopIsGold() {
            return shopIsGold;
        }

        /**
         *
         * @param shopIsGold
         *     The shop_is_gold
         */
        public void setShopIsGold(Integer shopIsGold) {
            this.shopIsGold = shopIsGold;
        }

        /**
         *
         * @return
         *     The etalaseId
         */
        public Object getEtalaseId() {
            return etalaseId;
        }

        /**
         *
         * @param etalaseId
         *     The etalase_id
         */
        public void setEtalaseId(Object etalaseId) {
            this.etalaseId = etalaseId;
        }

        /**
         *
         * @return
         *     The isInboxManager
         */
        public Integer getIsInboxManager() {
            return isInboxManager;
        }

        /**
         *
         * @param isInboxManager
         *     The is_inbox_manager
         */
        public void setIsInboxManager(Integer isInboxManager) {
            this.isInboxManager = isInboxManager;
        }

        /**
         *
         * @return
         *     The isTxManager
         */
        public Integer getIsTxManager() {
            return isTxManager;
        }

        /**
         *
         * @param isTxManager
         *     The is_tx_manager
         */
        public void setIsTxManager(Integer isTxManager) {
            this.isTxManager = isTxManager;
        }

    }

    public static class Product {

        @SerializedName("product_image")
        @Expose
        private String productImage;
        @SerializedName("product_shop_id")
        @Expose
        private Integer productShopId;
        @SerializedName("product_currency")
        @Expose
        private String productCurrency;
        @SerializedName("product_returnable")
        @Expose
        private Integer productReturnable;
        @SerializedName("product_rating_point")
        @Expose
        private String productRatingPoint;
        @SerializedName("product_url")
        @Expose
        private String productUrl;
        @SerializedName("product_shop_owner")
        @Expose
        private Integer productShopOwner;
        @SerializedName("product_etalase_id")
        @Expose
        private String productEtalaseId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_image_300")
        @Expose
        private String productImage300;
        @SerializedName("product_no_idr_price")
        @Expose
        private String productNoIdrPrice;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("product_etalase")
        @Expose
        private String productEtalase;
        @SerializedName("product_currency_id")
        @Expose
        private String productCurrencyId;
        @SerializedName("product_status")
        @Expose
        private String productStatus;
        @SerializedName("product_count_talk")
        @Expose
        private String productCountTalk;
        @SerializedName("product_currency_symbol")
        @Expose
        private String productCurrencySymbol;
        @SerializedName("product_normal_price")
        @Expose
        private String productNormalPrice;
        @SerializedName("product_image_full")
        @Expose
        private String productImageFull;
        @SerializedName("product_preorder")
        @Expose
        private Integer productPreorder;
        @SerializedName("product_count_review")
        @Expose
        private String productCountReview;
        @SerializedName("product_department")
        @Expose
        private String productDepartment;
        @SerializedName("product_count_sold")
        @Expose
        private String productCountSold;
        @SerializedName("product_rating_desc")
        @Expose
        private String productRatingDesc;

        /**
         *
         * @return
         *     The productImage
         */
        public String getProductImage() {
            return productImage;
        }

        /**
         *
         * @param productImage
         *     The product_image
         */
        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        /**
         *
         * @return
         *     The productShopId
         */
        public Integer getProductShopId() {
            return productShopId;
        }

        /**
         *
         * @param productShopId
         *     The product_shop_id
         */
        public void setProductShopId(Integer productShopId) {
            this.productShopId = productShopId;
        }

        /**
         *
         * @return
         *     The productCurrency
         */
        public String getProductCurrency() {
            return productCurrency;
        }

        /**
         *
         * @param productCurrency
         *     The product_currency
         */
        public void setProductCurrency(String productCurrency) {
            this.productCurrency = productCurrency;
        }

        /**
         *
         * @return
         *     The productReturnable
         */
        public Integer getProductReturnable() {
            return productReturnable;
        }

        /**
         *
         * @param productReturnable
         *     The product_returnable
         */
        public void setProductReturnable(Integer productReturnable) {
            this.productReturnable = productReturnable;
        }

        /**
         *
         * @return
         *     The productRatingPoint
         */
        public String getProductRatingPoint() {
            return productRatingPoint;
        }

        /**
         *
         * @param productRatingPoint
         *     The product_rating_point
         */
        public void setProductRatingPoint(String productRatingPoint) {
            this.productRatingPoint = productRatingPoint;
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

        /**
         *
         * @return
         *     The productShopOwner
         */
        public Integer getProductShopOwner() {
            return productShopOwner;
        }

        /**
         *
         * @param productShopOwner
         *     The product_shop_owner
         */
        public void setProductShopOwner(Integer productShopOwner) {
            this.productShopOwner = productShopOwner;
        }

        /**
         *
         * @return
         *     The productEtalaseId
         */
        public String getProductEtalaseId() {
            return productEtalaseId;
        }

        /**
         *
         * @param productEtalaseId
         *     The product_etalase_id
         */
        public void setProductEtalaseId(String productEtalaseId) {
            this.productEtalaseId = productEtalaseId;
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
         *     The productImage300
         */
        public String getProductImage300() {
            return productImage300;
        }

        /**
         *
         * @param productImage300
         *     The product_image_300
         */
        public void setProductImage300(String productImage300) {
            this.productImage300 = productImage300;
        }

        /**
         *
         * @return
         *     The productNoIdrPrice
         */
        public String getProductNoIdrPrice() {
            return productNoIdrPrice;
        }

        /**
         *
         * @param productNoIdrPrice
         *     The product_no_idr_price
         */
        public void setProductNoIdrPrice(String productNoIdrPrice) {
            this.productNoIdrPrice = productNoIdrPrice;
        }

        /**
         *
         * @return
         *     The productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         *
         * @param productId
         *     The product_id
         */
        public void setProductId(String productId) {
            this.productId = productId;
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
         *     The productCurrencyId
         */
        public String getProductCurrencyId() {
            return productCurrencyId;
        }

        /**
         *
         * @param productCurrencyId
         *     The product_currency_id
         */
        public void setProductCurrencyId(String productCurrencyId) {
            this.productCurrencyId = productCurrencyId;
        }

        /**
         *
         * @return
         *     The productStatus
         */
        public String getProductStatus() {
            return productStatus;
        }

        /**
         *
         * @param productStatus
         *     The product_status
         */
        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        /**
         *
         * @return
         *     The productCountTalk
         */
        public String getProductCountTalk() {
            return productCountTalk;
        }

        /**
         *
         * @param productCountTalk
         *     The product_count_talk
         */
        public void setProductCountTalk(String productCountTalk) {
            this.productCountTalk = productCountTalk;
        }

        /**
         *
         * @return
         *     The productCurrencySymbol
         */
        public String getProductCurrencySymbol() {
            return productCurrencySymbol;
        }

        /**
         *
         * @param productCurrencySymbol
         *     The product_currency_symbol
         */
        public void setProductCurrencySymbol(String productCurrencySymbol) {
            this.productCurrencySymbol = productCurrencySymbol;
        }

        /**
         *
         * @return
         *     The productNormalPrice
         */
        public String getProductNormalPrice() {
            return productNormalPrice;
        }

        /**
         *
         * @param productNormalPrice
         *     The product_normal_price
         */
        public void setProductNormalPrice(String productNormalPrice) {
            this.productNormalPrice = productNormalPrice;
        }

        /**
         *
         * @return
         *     The productImageFull
         */
        public String getProductImageFull() {
            return productImageFull;
        }

        /**
         *
         * @param productImageFull
         *     The product_image_full
         */
        public void setProductImageFull(String productImageFull) {
            this.productImageFull = productImageFull;
        }

        /**
         *
         * @return
         *     The productPreorder
         */
        public Integer getProductPreorder() {
            return productPreorder;
        }

        /**
         *
         * @param productPreorder
         *     The product_preorder
         */
        public void setProductPreorder(Integer productPreorder) {
            this.productPreorder = productPreorder;
        }

        /**
         *
         * @return
         *     The productCountReview
         */
        public String getProductCountReview() {
            return productCountReview;
        }

        /**
         *
         * @param productCountReview
         *     The product_count_review
         */
        public void setProductCountReview(String productCountReview) {
            this.productCountReview = productCountReview;
        }

        /**
         *
         * @return
         *     The productDepartment
         */
        public String getProductDepartment() {
            return productDepartment;
        }

        /**
         *
         * @param productDepartment
         *     The product_department
         */
        public void setProductDepartment(String productDepartment) {
            this.productDepartment = productDepartment;
        }

        /**
         *
         * @return
         *     The productCountSold
         */
        public String getProductCountSold() {
            return productCountSold;
        }

        /**
         *
         * @param productCountSold
         *     The product_count_sold
         */
        public void setProductCountSold(String productCountSold) {
            this.productCountSold = productCountSold;
        }

        /**
         *
         * @return
         *     The productRatingDesc
         */
        public String getProductRatingDesc() {
            return productRatingDesc;
        }

        /**
         *
         * @param productRatingDesc
         *     The product_rating_desc
         */
        public void setProductRatingDesc(String productRatingDesc) {
            this.productRatingDesc = productRatingDesc;
        }

    }

    public static class Paging {

        @SerializedName("uri_previous")
        @Expose
        private String uriPrevious;
        @SerializedName("uri_next")
        @Expose
        private String uriNext;

        /**
         *
         * @return
         *     The uriPrevious
         */
        public String getUriPrevious() {
            return uriPrevious;
        }

        /**
         *
         * @param uriPrevious
         *     The uri_previous
         */
        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

        /**
         *
         * @return
         *     The uriNext
         */
        public String getUriNext() {
            return uriNext;
        }

        /**
         *
         * @param uriNext
         *     The uri_next
         */
        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

    }
}
