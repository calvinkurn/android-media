
package com.tokopedia.sellerapp.home.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Booking {

    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("api_url")
    @Expose
    private String apiUrl;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("ut")
    @Expose
    private Integer ut;

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
     *     The apiUrl
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * 
     * @param apiUrl
     *     The api_url
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The token
     */
    public String getToken() {
        return token;
    }

    /**
     * 
     * @param token
     *     The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 
     * @return
     *     The ut
     */
    public Integer getUt() {
        return ut;
    }

    /**
     * 
     * @param ut
     *     The ut
     */
    public void setUt(Integer ut) {
        this.ut = ut;
    }

}
