
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("list")
    @Expose
    private java.util.List<Review> reviews = null;
    @SerializedName("owner")
    @Expose
    private Owner owner;

    public java.util.List<Review> getReviews() {
        return reviews;
    }

    public void setList(java.util.List<Review> list) {
        this.reviews = list;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
