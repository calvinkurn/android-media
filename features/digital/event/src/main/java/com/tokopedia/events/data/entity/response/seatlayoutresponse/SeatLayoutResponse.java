package com.tokopedia.events.data.entity.response.seatlayoutresponse;

import com.google.gson.annotations.SerializedName;

public class SeatLayoutResponse{

	@SerializedName("kind")
	private String kind;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("product_schedule_id")
	private int productScheduleId;

	@SerializedName("url")
	private String url;

	@SerializedName("product_schedule_package_id")
	private int productSchedulePackageId;

	@SerializedName("provider_section_id")
	private String providerSectionId;

	@SerializedName("layout")
	private String layout;

	@SerializedName("has_seat_layout")
	private int hasSeatLayout;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("provider_meta_data")
	private String providerMetaData;

	@SerializedName("status")
	private int status;

	public void setKind(String kind){
		this.kind = kind;
	}

	public String getKind(){
		return kind;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setProductScheduleId(int productScheduleId){
		this.productScheduleId = productScheduleId;
	}

	public int getProductScheduleId(){
		return productScheduleId;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setProductSchedulePackageId(int productSchedulePackageId){
		this.productSchedulePackageId = productSchedulePackageId;
	}

	public int getProductSchedulePackageId(){
		return productSchedulePackageId;
	}

	public void setProviderSectionId(String providerSectionId){
		this.providerSectionId = providerSectionId;
	}

	public String getProviderSectionId(){
		return providerSectionId;
	}

	public void setLayout(String layout){
		this.layout = layout;
	}

	public String getLayout(){
		return layout;
	}

	public void setHasSeatLayout(int hasSeatLayout){
		this.hasSeatLayout = hasSeatLayout;
	}

	public int getHasSeatLayout(){
		return hasSeatLayout;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setProviderMetaData(String providerMetaData){
		this.providerMetaData = providerMetaData;
	}

	public String getProviderMetaData(){
		return providerMetaData;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"SeatLayoutResponse{" + 
			"kind = '" + kind + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",product_schedule_id = '" + productScheduleId + '\'' + 
			",url = '" + url + '\'' + 
			",product_schedule_package_id = '" + productSchedulePackageId + '\'' + 
			",provider_section_id = '" + providerSectionId + '\'' + 
			",layout = '" + layout + '\'' + 
			",has_seat_layout = '" + hasSeatLayout + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",product_id = '" + productId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",provider_meta_data = '" + providerMetaData + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}