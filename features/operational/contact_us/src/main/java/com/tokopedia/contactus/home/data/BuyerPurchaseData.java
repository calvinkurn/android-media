package com.tokopedia.contactus.home.data;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.common.data.Paging;

import java.util.List;

public class BuyerPurchaseData{

	@SerializedName("paging")
	private Paging paging;

	@SerializedName("is_success")
	private int isSuccess;

	@SerializedName("list")
	private List<BuyerPurchaseList> buyerPurchaseList;

	public void setPaging(Paging paging){
		this.paging = paging;
	}

	public Paging getPaging(){
		return paging;
	}

	public void setIsSuccess(int isSuccess){
		this.isSuccess = isSuccess;
	}

	public int getIsSuccess(){
		return isSuccess;
	}

	public void setBuyerPurchaseList(List<BuyerPurchaseList> list){
		this.buyerPurchaseList = list;
	}

	public List<BuyerPurchaseList> getBuyerPurchaseList(){
		return buyerPurchaseList;
	}

	@Override
 	public String toString(){
		return 
			"BuyerPurchaseData{" + 
			"paging = '" + paging + '\'' + 
			",is_success = '" + isSuccess + '\'' + 
			",list = '" + buyerPurchaseList + '\'' +
			"}";
		}
}