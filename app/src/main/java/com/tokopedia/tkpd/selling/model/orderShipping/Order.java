
package com.tokopedia.tkpd.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Order {

    @SerializedName("is_allow_manage_tx")
    @Expose
    private Integer isAllowManageTx;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("is_gold_shop")
    @Expose
    private Integer isGoldShop;

    /**
     * 
     * @return
     *     The isAllowManageTx
     */
    public Integer getIsAllowManageTx() {
        return isAllowManageTx;
    }

    /**
     * 
     * @param isAllowManageTx
     *     The is_allow_manage_tx
     */
    public void setIsAllowManageTx(Integer isAllowManageTx) {
        this.isAllowManageTx = isAllowManageTx;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 
     * @return
     *     The isGoldShop
     */
    public Integer getIsGoldShop() {
        return isGoldShop;
    }

    /**
     * 
     * @param isGoldShop
     *     The is_gold_shop
     */
    public void setIsGoldShop(Integer isGoldShop) {
        this.isGoldShop = isGoldShop;
    }

}
