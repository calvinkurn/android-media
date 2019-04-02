
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendationProduct {

    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("has_next_page")
    @Expose
    private Boolean hasNextPage;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

}
