package com.tokopedia.oms.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaData{

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("entity_product_id")
	private Integer entityProductId;

	@SerializedName("entity_provider_id")
	private Integer entityProviderId;

	@SerializedName("entity_category_id")
	private Integer entityCategoryId;

	@SerializedName("tax_per_quantity")
	private List<Object> taxPerQuantity;

	@SerializedName("use_pdf")
	private Integer usePdf;

	@SerializedName("total_other_charges")
	private Integer totalOtherCharges;

	@SerializedName("total_ticket_price")
	private Integer totalTicketPrice;

	@SerializedName("other_charges")
	private List<OtherChargesItem> otherCharges;

	@SerializedName("entity_image")
	private String entityImage;

	@SerializedName("links")
	private String links;

	@SerializedName("total_tax_amount")
	private Integer totalTaxAmount;

	@SerializedName("entity_schedule_id")
	private Integer entityScheduleId;

	@SerializedName("entity_packages")
	private List<EntityPackagesItem> entityPackages;

	@SerializedName("start_date")
	private String startDate;

	@SerializedName("total_ticket_count")
	private Integer totalTicketCount;

	@SerializedName("entity_end_time")
	private String entityEndTime;

	@SerializedName("entity_start_time")
	private String entityStartTime;

	@SerializedName("city_searched")
	private String citySearched;

	@SerializedName("entity_passengers")
	private List<EntityPassengersItem> entityPassengers;

	@SerializedName("entity_category_name")
	private String entityCategoryName;

	@SerializedName("entity_group_id")
	private Integer entityGroupId;

	@SerializedName("entity_invoice_id")
	private Integer entityInvoiceId;

	@SerializedName("entity_address")
	private EntityAddress entityAddress;

	@SerializedName("entity_product_name")
	private String entityProductName;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setEntityProductId(Integer entityProductId){
		this.entityProductId = entityProductId;
	}

	public Integer getEntityProductId(){
		return entityProductId;
	}

	public void setEntityProviderId(Integer entityProviderId){
		this.entityProviderId = entityProviderId;
	}

	public Integer getEntityProviderId(){
		return entityProviderId;
	}

	public void setEntityCategoryId(Integer entityCategoryId){
		this.entityCategoryId = entityCategoryId;
	}

	public Integer getEntityCategoryId(){
		return entityCategoryId;
	}

	public void setTaxPerQuantity(List<Object> taxPerQuantity){
		this.taxPerQuantity = taxPerQuantity;
	}

	public List<Object> getTaxPerQuantity(){
		return taxPerQuantity;
	}

	public void setUsePdf(Integer usePdf){
		this.usePdf = usePdf;
	}

	public Integer getUsePdf(){
		return usePdf;
	}

	public void setTotalOtherCharges(Integer totalOtherCharges){
		this.totalOtherCharges = totalOtherCharges;
	}

	public Integer getTotalOtherCharges(){
		return totalOtherCharges;
	}

	public void setTotalTicketPrice(Integer totalTicketPrice){
		this.totalTicketPrice = totalTicketPrice;
	}

	public Integer getTotalTicketPrice(){
		return totalTicketPrice;
	}

	public void setOtherCharges(List<OtherChargesItem> otherCharges){
		this.otherCharges = otherCharges;
	}

	public List<OtherChargesItem> getOtherCharges(){
		return otherCharges;
	}

	public void setEntityImage(String entityImage){
		this.entityImage = entityImage;
	}

	public String getEntityImage(){
		return entityImage;
	}

	public void setLinks(String links){
		this.links = links;
	}

	public String getLinks(){
		return links;
	}

	public void setTotalTaxAmount(Integer totalTaxAmount){
		this.totalTaxAmount = totalTaxAmount;
	}

	public Integer getTotalTaxAmount(){
		return totalTaxAmount;
	}

	public void setEntityScheduleId(Integer entityScheduleId){
		this.entityScheduleId = entityScheduleId;
	}

	public Integer getEntityScheduleId(){
		return entityScheduleId;
	}

	public void setEntityPackages(List<EntityPackagesItem> entityPackages){
		this.entityPackages = entityPackages;
	}

	public List<EntityPackagesItem> getEntityPackages(){
		return entityPackages;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setTotalTicketCount(Integer totalTicketCount){
		this.totalTicketCount = totalTicketCount;
	}

	public Integer getTotalTicketCount(){
		return totalTicketCount;
	}

	public void setEntityEndTime(String entityEndTime){
		this.entityEndTime = entityEndTime;
	}

	public String getEntityEndTime(){
		return entityEndTime;
	}

	public void setEntityStartTime(String entityStartTime){
		this.entityStartTime = entityStartTime;
	}

	public String getEntityStartTime(){
		return entityStartTime;
	}

	public void setCitySearched(String citySearched){
		this.citySearched = citySearched;
	}

	public String getCitySearched(){
		return citySearched;
	}

	public void setEntityPassengers(List<EntityPassengersItem> entityPassengers){
		this.entityPassengers = entityPassengers;
	}

	public List<EntityPassengersItem> getEntityPassengers(){
		return entityPassengers;
	}

	public void setEntityCategoryName(String entityCategoryName){
		this.entityCategoryName = entityCategoryName;
	}

	public String getEntityCategoryName(){
		return entityCategoryName;
	}

	public void setEntityGroupId(Integer entityGroupId){
		this.entityGroupId = entityGroupId;
	}

	public Integer getEntityGroupId(){
		return entityGroupId;
	}

	public void setEntityInvoiceId(Integer entityInvoiceId){
		this.entityInvoiceId = entityInvoiceId;
	}

	public Integer getEntityInvoiceId(){
		return entityInvoiceId;
	}

	public void setEntityAddress(EntityAddress entityAddress){
		this.entityAddress = entityAddress;
	}

	public EntityAddress getEntityAddress(){
		return entityAddress;
	}

	public void setEntityProductName(String entityProductName){
		this.entityProductName = entityProductName;
	}

	public String getEntityProductName(){
		return entityProductName;
	}

	@Override
 	public String toString(){
		return 
			"MetaData{" + 
			"end_date = '" + endDate + '\'' + 
			",entity_product_id = '" + entityProductId + '\'' + 
			",entity_provider_id = '" + entityProviderId + '\'' + 
			",entity_category_id = '" + entityCategoryId + '\'' + 
			",tax_per_quantity = '" + taxPerQuantity + '\'' + 
			",use_pdf = '" + usePdf + '\'' + 
			",total_other_charges = '" + totalOtherCharges + '\'' + 
			",total_ticket_price = '" + totalTicketPrice + '\'' + 
			",other_charges = '" + otherCharges + '\'' + 
			",entity_image = '" + entityImage + '\'' + 
			",links = '" + links + '\'' + 
			",total_tax_amount = '" + totalTaxAmount + '\'' + 
			",entity_schedule_id = '" + entityScheduleId + '\'' + 
			",entity_packages = '" + entityPackages + '\'' + 
			",start_date = '" + startDate + '\'' + 
			",total_ticket_count = '" + totalTicketCount + '\'' + 
			",entity_end_time = '" + entityEndTime + '\'' + 
			",entity_start_time = '" + entityStartTime + '\'' + 
			",city_searched = '" + citySearched + '\'' + 
			",entity_passengers = '" + entityPassengers + '\'' + 
			",entity_category_name = '" + entityCategoryName + '\'' + 
			",entity_group_id = '" + entityGroupId + '\'' + 
			",entity_invoice_id = '" + entityInvoiceId + '\'' + 
			",entity_address = '" + entityAddress + '\'' + 
			",entity_product_name = '" + entityProductName + '\'' + 
			"}";
		}
}