package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLikesResponse {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("average_rating")
    @Expose
    private int averageRating;
    @SerializedName("total_likes")
    @Expose
    private int totalLikes;
    @SerializedName("is_liked")
    @Expose
    private boolean isLiked;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}