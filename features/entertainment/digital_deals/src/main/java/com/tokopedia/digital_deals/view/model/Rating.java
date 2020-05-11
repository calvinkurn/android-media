package com.tokopedia.digital_deals.view.model;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("feedback")
    private String feedback;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("is_liked")
    private String isLiked;


    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getIsLiked() {
        return isLiked;
    }

    @Override
    public String toString() {
        return
                "Rating{" +
                        "feedback = '" + feedback + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",product_id = '" + productId + '\'' +
                        ",rating = '" + rating + '\'' +
                        ",is_liked = '" + isLiked + '\'' +
                        "}";
    }

}