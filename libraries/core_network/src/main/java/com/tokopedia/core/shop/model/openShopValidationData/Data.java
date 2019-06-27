
package com.tokopedia.core.shop.model.openShopValidationData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Deprecated
@Parcel
public class Data {

    @SerializedName("post_key")
    @Expose
    String postKey;
    @SerializedName("shop_id")
    @Expose
    Integer shopId;
    @SerializedName("shop_url")
    @Expose
    String shopUrl;
    @SerializedName("is_success")
    @Expose
    Integer isSuccess;

    /**
     * 
     * @return
     *     The postKey
     */
    public String getPostKey() {
        return postKey;
    }

    /**
     * 
     * @param postKey
     *     The post_key
     */
    public void setPostKey(String postKey) {
        this.postKey = postKey;
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
