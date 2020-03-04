package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Rating;

public class LikeUpdateModel{

	@SerializedName("rating")
	private Rating rating;

	public void setRating(Rating rating){
		this.rating = rating;
	}

	public Rating getRating(){
		return rating;
	}

	@Override
 	public String toString(){
		return 
			"LikeUpdateModel{" + 
			"rating = '" + rating + '\'' + 
			"}";
		}
}