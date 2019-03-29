package com.tokopedia.contactus.common.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Detail implements Serializable{

	@SerializedName("fraud_status")
	private int fraudStatus;

	@SerializedName("detail_url")
	private String detailUrl;

	@SerializedName("code")
	private String code;

	@SerializedName("create_time")
	private String createTime;

	@SerializedName("type_id")
	private int typeId;

	@SerializedName("voucher_code")
	private String voucherCode;

	@SerializedName("product_type_id")
	private int productTypeId;

	@SerializedName("type")
	private String type;

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("is_fraud")
	private boolean isFraud;

	@SerializedName("id")
	private long id;

	@SerializedName("voucher_label")
	private String voucherLabel;

	@SerializedName("status")
	private String status;

	public void setFraudStatus(int fraudStatus){
		this.fraudStatus = fraudStatus;
	}

	public int getFraudStatus(){
		return fraudStatus;
	}

	public void setDetailUrl(String detailUrl){
		this.detailUrl = detailUrl;
	}

	public String getDetailUrl(){
		return detailUrl;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}

	public void setTypeId(int typeId){
		this.typeId = typeId;
	}

	public int getTypeId(){
		return typeId;
	}

	public void setVoucherCode(String voucherCode){
		this.voucherCode = voucherCode;
	}

	public String getVoucherCode(){
		return voucherCode;
	}

	public void setProductTypeId(int productTypeId){
		this.productTypeId = productTypeId;
	}

	public int getProductTypeId(){
		return productTypeId;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setStatusId(int statusId){
		this.statusId = statusId;
	}

	public int getStatusId(){
		return statusId;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setIsFraud(boolean isFraud){
		this.isFraud = isFraud;
	}

	public boolean isIsFraud(){
		return isFraud;
	}

	public void setId(long id){
		this.id = id;
	}

	public long getId(){
		return id;
	}

	public void setVoucherLabel(String voucherLabel){
		this.voucherLabel = voucherLabel;
	}

	public String getVoucherLabel(){
		return voucherLabel;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Detail{" + 
			"fraud_status = '" + fraudStatus + '\'' + 
			",detail_url = '" + detailUrl + '\'' + 
			",code = '" + code + '\'' + 
			",create_time = '" + createTime + '\'' + 
			",type_id = '" + typeId + '\'' + 
			",voucher_code = '" + voucherCode + '\'' + 
			",product_type_id = '" + productTypeId + '\'' + 
			",type = '" + type + '\'' + 
			",status_id = '" + statusId + '\'' + 
			",total_amount = '" + totalAmount + '\'' + 
			",is_fraud = '" + isFraud + '\'' + 
			",id = '" + id + '\'' + 
			",voucher_label = '" + voucherLabel + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}