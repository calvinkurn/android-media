package com.tokopedia.oms.domain.model.request.verify;

import com.google.gson.annotations.SerializedName;

public class ValidateShow{

	@SerializedName("quantity")
	private Integer quantity;

	@SerializedName("group_id")
	private Integer groupId;

	@SerializedName("product_id")
	private Integer productId;

	@SerializedName("package_id")
	private Integer packageId;

	@SerializedName("schedule_id")
	private Integer scheduleId;

	public void setQuantity(Integer quantity){
		this.quantity = quantity;
	}

	public Integer getQuantity(){
		return quantity;
	}

	public void setGroupId(Integer groupId){
		this.groupId = groupId;
	}

	public Integer getGroupId(){
		return groupId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setPackageId(Integer packageId){
		this.packageId = packageId;
	}

	public Integer getPackageId(){
		return packageId;
	}

	public void setScheduleId(Integer scheduleId){
		this.scheduleId = scheduleId;
	}

	public Integer getScheduleId(){
		return scheduleId;
	}

	@Override
 	public String toString(){
		return 
			"ValidateShow{" + 
			"quantity = '" + quantity + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",product_id = '" + productId + '\'' + 
			",package_id = '" + packageId + '\'' + 
			",schedule_id = '" + scheduleId + '\'' + 
			"}";
		}
}