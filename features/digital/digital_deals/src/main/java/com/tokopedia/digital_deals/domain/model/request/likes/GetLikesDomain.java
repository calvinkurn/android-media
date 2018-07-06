package com.tokopedia.digital_deals.domain.model.request.likes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLikesDomain {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("average_rating")
    @Expose
    private int averageRating;
    @SerializedName("total_likes")
    @Expose
    private int totalLikes;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

}