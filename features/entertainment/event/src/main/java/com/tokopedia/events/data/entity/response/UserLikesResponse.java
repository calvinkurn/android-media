package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.SerializedName;

public class UserLikesResponse{

	@SerializedName("max_end_date")
	private int maxEndDate;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("sales_price")
	private int salesPrice;

	@SerializedName("min_start_date")
	private int minStartDate;

	@SerializedName("is_liked")
	private boolean isLiked;

	public void setMaxEndDate(int maxEndDate){
		this.maxEndDate = maxEndDate;
	}

	public int getMaxEndDate(){
		return maxEndDate;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setSalesPrice(int salesPrice){
		this.salesPrice = salesPrice;
	}

	public int getSalesPrice(){
		return salesPrice;
	}

	public void setMinStartDate(int minStartDate){
		this.minStartDate = minStartDate;
	}

	public int getMinStartDate(){
		return minStartDate;
	}

	public void setIsLiked(boolean isLiked){
		this.isLiked = isLiked;
	}

	public boolean isIsLiked(){
		return isLiked;
	}
}