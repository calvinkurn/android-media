
package com.tokopedia.tkpd.drawer.model.LoyaltyItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Merchant {

    @SerializedName("is_lucky")
    @Expose
    private int isLucky;
    @SerializedName("expire_time")
    @Expose
    private String expireTime;

    /**
     * 
     * @return
     *     The isLucky
     */
    public int getIsLucky() {
        return isLucky;
    }

    /**
     * 
     * @param isLucky
     *     The is_lucky
     */
    public void setIsLucky(int isLucky) {
        this.isLucky = isLucky;
    }

    /**
     * 
     * @return
     *     The expireTime
     */
    public String getExpireTime() {
        return expireTime;
    }

    /**
     * 
     * @param expireTime
     *     The expire_time
     */
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

}
