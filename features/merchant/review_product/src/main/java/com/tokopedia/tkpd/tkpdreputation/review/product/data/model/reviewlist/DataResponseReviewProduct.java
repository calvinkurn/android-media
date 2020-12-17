
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponseReviewProduct extends DataResponseReview {

    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("list")
    @Expose
    private List<Review> list = null;

    public List<Review> getList() {
        return list;
    }

    public void setList(List<Review> list) {
        this.list = list;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
