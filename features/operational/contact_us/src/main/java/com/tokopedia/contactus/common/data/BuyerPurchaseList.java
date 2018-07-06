package com.tokopedia.contactus.common.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class BuyerPurchaseList implements Serializable{

	@SerializedName("payment")
	private Payment payment;

	@SerializedName("detail")
	private Detail detail;

	@SerializedName("products")
	private List<ProductsItem> products;

	public void setPayment(Payment payment){
		this.payment = payment;
	}

	public Payment getPayment(){
		return payment;
	}

	public void setDetail(Detail detail){
		this.detail = detail;
	}

	public Detail getDetail(){
		return detail;
	}

	public void setProducts(List<ProductsItem> products){
		this.products = products;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}

	@Override
 	public String toString(){
		return 
			"BuyerPurchaseList{" + 
			"payment = '" + payment + '\'' + 
			",detail = '" + detail + '\'' + 
			",products = '" + products + '\'' + 
			"}";
		}


}