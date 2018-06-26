package com.tokopedia.digital_deals.data.entity.response.searchresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse{

	@SerializedName("grid_layout")
	private JsonArray deals;

	@SerializedName("filters")
	private JsonArray filters;

	@SerializedName("page")
	private JsonObject page;

	@SerializedName("count")
	private int count;

	public JsonArray getDeals() {
		return deals;
	}

	public void setDeals(JsonArray deals) {
		this.deals = deals;
	}

	public JsonArray getFilters() {
		return filters;
	}

	public void setFilters(JsonArray filters) {
		this.filters = filters;
	}

	public JsonObject getPage() {
		return page;
	}

	public void setPage(JsonObject page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}