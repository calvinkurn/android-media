
package com.tokopedia.core.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class OrderCustomer {

    @SerializedName("customer_url")
    @Expose
    String customerUrl;
    @SerializedName("customer_id")
    @Expose
    String customerId;
    @SerializedName("customer_name")
    @Expose
    String customerName;
    @SerializedName("customer_image")
    @Expose
    String customerImage;

    /**
     * 
     * @return
     *     The customerUrl
     */
    public String getCustomerUrl() {
        return customerUrl;
    }

    /**
     * 
     * @param customerUrl
     *     The customer_url
     */
    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    /**
     * 
     * @return
     *     The customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * 
     * @param customerId
     *     The customer_id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * 
     * @return
     *     The customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 
     * @param customerName
     *     The customer_name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 
     * @return
     *     The customerImage
     */
    public String getCustomerImage() {
        return customerImage;
    }

    /**
     * 
     * @param customerImage
     *     The customer_image
     */
    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

}
