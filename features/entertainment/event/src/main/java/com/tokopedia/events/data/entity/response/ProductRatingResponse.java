package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.SerializedName;

public class ProductRatingResponse{

	@SerializedName("product_id")
	private int productId;

	@SerializedName("average_rating")
	private int averageRating;

	@SerializedName("total_likes")
	private int totalLikes;

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setAverageRating(int averageRating){
		this.averageRating = averageRating;
	}

	public int getAverageRating(){
		return averageRating;
	}

	public void setTotalLikes(int totalLikes){
		this.totalLikes = totalLikes;
	}

	public int getTotalLikes(){
		return totalLikes;
	}
}