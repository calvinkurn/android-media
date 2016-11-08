
package com.tokopedia.core.shop.model.openShopSubmitData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Data {

    @SerializedName("is_success")
    @Expose
    Integer isSuccess;
    @SerializedName("shop_id")
    @Expose
    Integer shopId;
    @SerializedName("shop_url")
    @Expose
    String shopUrl;

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

    /**
     * 
     * @return
     *     The shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * @return
     *     The shopUrl
     */
    public String getShopUrl() {
        return shopUrl;
    }

    /**
     * 
     * @param shopUrl
     *     The shop_url
     */
    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

}
