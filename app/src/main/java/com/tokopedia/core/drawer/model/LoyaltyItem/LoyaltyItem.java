
package com.tokopedia.core.drawer.model.LoyaltyItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyItem {

    @SerializedName("merchant")
    @Expose
    private Merchant merchant;
    @SerializedName("loyalty_point")
    @Expose
    private LoyaltyPoint loyaltyPoint;
    @SerializedName("buyer")
    @Expose
    private Buyer buyer;
    @SerializedName("uri")
    @Expose
    private String uri;

    /**
     * 
     * @return
     *     The merchant
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * 
     * @param merchant
     *     The merchant
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * 
     * @return
     *     The loyaltyPoint
     */
    public LoyaltyPoint getLoyaltyPoint() {
        return loyaltyPoint;
    }

    /**
     * 
     * @param loyaltyPoint
     *     The loyalty_point
     */
    public void setLoyaltyPoint(LoyaltyPoint loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    /**
     * 
     * @return
     *     The buyer
     */
    public Buyer getBuyer() {
        return buyer;
    }

    /**
     * 
     * @param buyer
     *     The buyer
     */
    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    /**
     * 
     * @return
     *     The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

}
