package com.tokopedia.digital_deals.data.entity.response.searchresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse{


	@Expose
	@SerializedName("data")
	SearchDataResponse data;


	public SearchDataResponse getData() {
		return data;
	}

	public void setData(SearchDataResponse data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return data.toString();
	}

}