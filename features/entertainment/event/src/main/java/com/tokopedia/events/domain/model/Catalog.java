package com.tokopedia.events.domain.model;

import com.google.gson.annotations.SerializedName;

public class Catalog{

	@SerializedName("digital_product_code")
	private String digitalProductCode;

	@SerializedName("digital_category_id")
	private int digitalCategoryId;

	@SerializedName("digital_product_id")
	private int digitalProductId;

	public void setDigitalProductCode(String digitalProductCode){
		this.digitalProductCode = digitalProductCode;
	}

	public String getDigitalProductCode(){
		return digitalProductCode;
	}

	public void setDigitalCategoryId(int digitalCategoryId){
		this.digitalCategoryId = digitalCategoryId;
	}

	public int getDigitalCategoryId(){
		return digitalCategoryId;
	}

	public void setDigitalProductId(int digitalProductId){
		this.digitalProductId = digitalProductId;
	}

	public int getDigitalProductId(){
		return digitalProductId;
	}
}