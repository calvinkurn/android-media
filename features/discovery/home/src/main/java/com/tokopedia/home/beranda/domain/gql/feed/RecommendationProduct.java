
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendationProduct {

    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("banner")
    @Expose
    private List<Banner> banners = null;
    @SerializedName("position")
    @Expose
    private List<LayoutType> layoutTypes = null;

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

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public List<LayoutType> getLayoutTypes() {
        return layoutTypes;
    }

    public void setLayoutTypes(List<LayoutType> layoutTypes) {
        this.layoutTypes = layoutTypes;
    }
}
