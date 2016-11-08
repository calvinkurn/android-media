
package com.tokopedia.core.inboxreputation.model.inboxreputation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class NavsTab {

    @SerializedName("my_review")
    @Expose
    Integer myReview;
    @SerializedName("all")
    @Expose
    Integer all;
    @SerializedName("my_product")
    @Expose
    Integer myProduct;

    /**
     * 
     * @return
     *     The myReview
     */
    public Integer getMyReview() {
        return myReview;
    }

    /**
     * 
     * @param myReview
     *     The my_review
     */
    public void setMyReview(Integer myReview) {
        this.myReview = myReview;
    }

    /**
     * 
     * @return
     *     The all
     */
    public Integer getAll() {
        return all;
    }

    /**
     * 
     * @param all
     *     The all
     */
    public void setAll(Integer all) {
        this.all = all;
    }

    /**
     * 
     * @return
     *     The myProduct
     */
    public Integer getMyProduct() {
        return myProduct;
    }

    /**
     * 
     * @param myProduct
     *     The my_product
     */
    public void setMyProduct(Integer myProduct) {
        this.myProduct = myProduct;
    }

}
