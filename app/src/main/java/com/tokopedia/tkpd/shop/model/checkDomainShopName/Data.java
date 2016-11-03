
package com.tokopedia.tkpd.shop.model.checkDomainShopName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Data {

    @SerializedName("status_domain")
    @Expose
    Integer statusDomain;

    @SerializedName("status_shop_name")
    @Expose
    Integer statusShopName;

    /**
     * 
     * @return
     *     The statusDomain
     */
    public Integer getStatusDomain() {
        return statusDomain;
    }

    /**
     * 
     * @param statusDomain
     *     The status_domain
     */
    public void setStatusDomain(Integer statusDomain) {
        this.statusDomain = statusDomain;
    }

    /**
     *
     * @param statusShopName
     *      The status_shop_name
     */
    public Integer getStatusShopName() {
        return statusShopName;
    }

    /**
     *
     * @param statusShopName
     *      The status_shop_name
     */
    public void setStatusShopName(Integer statusShopName) {
        this.statusShopName = statusShopName;
    }
}
