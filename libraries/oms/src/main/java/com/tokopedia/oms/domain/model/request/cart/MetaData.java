package com.tokopedia.oms.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MetaData implements Parcelable {

	@SerializedName("entity_product_id")
	private Integer entityProductId;

	@SerializedName("entity_end_time")
	private String entityEndTime;

	@SerializedName("entity_start_time")
	private String entityStartTime;

	@SerializedName("entity_category_id")
	private Integer entityCategoryId;

	@SerializedName("city_searched")
	private String citySearched;

	@SerializedName("tax_per_quantity")
	private List<TaxPerQuantityItem> taxPerQuantity;

	@SerializedName("total_other_charges")
	private Integer totalOtherCharges;

	@SerializedName("entity_passengers")
	private List<EntityPassengerItem> entityPassengers;

	@SerializedName("total_ticket_price")
	private Integer totalTicketPrice;

	@SerializedName("entity_category_name")
	private String entityCategoryName;

	@SerializedName("entity_group_id")
	private Integer entityGroupId;

	@SerializedName("other_charges")
	private List<OtherChargesItem> otherCharges;

	@SerializedName("entity_address")
	private EntityAddress entityAddress;

	@SerializedName("total_tax_amount")
	private Integer totalTaxAmount;

	@SerializedName("entity_image")
	private String entityImage;

	@SerializedName("entity_schedule_id")
	private Integer entityScheduleId;

	@SerializedName("entity_packages")
	private List<EntityPackageItem> entityPackages;

	@SerializedName("total_ticket_count")
	private Integer totalTicketCount;

	public void setEntityProductId(int entityProductId){
		this.entityProductId = entityProductId;
	}

	public Integer getEntityProductId(){
		return entityProductId;
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

	public void setEntityCategoryId(Integer entityCategoryId){
		this.entityCategoryId = entityCategoryId;
	}

	public Integer getEntityCategoryId(){
		return entityCategoryId;
	}

	public void setCitySearched(String citySearched){
		this.citySearched = citySearched;
	}

	public String getCitySearched(){
		return citySearched;
	}

	public void setTaxPerQuantity(List<TaxPerQuantityItem> taxPerQuantity){
		this.taxPerQuantity = taxPerQuantity;
	}

	public List<TaxPerQuantityItem> getTaxPerQuantity(){
		return taxPerQuantity;
	}

	public void setTotalOtherCharges(Integer totalOtherCharges){
		this.totalOtherCharges = totalOtherCharges;
	}

	public Integer getTotalOtherCharges(){
		return totalOtherCharges;
	}

	public void setEntityPassengers(List<EntityPassengerItem> entityPassengers){
		this.entityPassengers = entityPassengers;
	}

	public List<EntityPassengerItem> getEntityPassengers(){
		return entityPassengers;
	}

	public void setTotalTicketPrice(Integer totalTicketPrice){
		this.totalTicketPrice = totalTicketPrice;
	}

	public Integer getTotalTicketPrice(){
		return totalTicketPrice;
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

	public void setOtherCharges(List<OtherChargesItem> otherCharges){
		this.otherCharges = otherCharges;
	}

	public List<OtherChargesItem> getOtherCharges(){
		return otherCharges;
	}

	public void setEntityAddress(EntityAddress entityAddress){
		this.entityAddress = entityAddress;
	}

	public EntityAddress getEntityAddress(){
		return entityAddress;
	}

	public void setTotalTaxAmount(Integer totalTaxAmount){
		this.totalTaxAmount = totalTaxAmount;
	}

	public Integer getTotalTaxAmount(){
		return totalTaxAmount;
	}

	public void setEntityImage(String entityImage){
		this.entityImage = entityImage;
	}

	public String getEntityImage(){
		return entityImage;
	}

	public void setEntityScheduleId(Integer entityScheduleId){
		this.entityScheduleId = entityScheduleId;
	}

	public Integer getEntityScheduleId(){
		return entityScheduleId;
	}

	public void setEntityPackages(List<EntityPackageItem> entityPackages){
		this.entityPackages = entityPackages;
	}

	public List<EntityPackageItem> getEntityPackages(){
		return entityPackages;
	}

	public void setTotalTicketCount(Integer totalTicketCount){
		this.totalTicketCount = totalTicketCount;
	}

	public Integer getTotalTicketCount(){
		return totalTicketCount;
	}

	@Override
 	public String toString(){
		return
			"MetaData{" +
			"entity_product_id = '" + entityProductId + '\'' +
			",entity_end_time = '" + entityEndTime + '\'' +
			",entity_start_time = '" + entityStartTime + '\'' +
			",entity_category_id = '" + entityCategoryId + '\'' +
			",city_searched = '" + citySearched + '\'' +
			",tax_per_quantity = '" + taxPerQuantity + '\'' +
			",total_other_charges = '" + totalOtherCharges + '\'' +
			",entity_passengers = '" + entityPassengers + '\'' +
			",total_ticket_price = '" + totalTicketPrice + '\'' +
			",entity_category_name = '" + entityCategoryName + '\'' +
			",entity_group_id = '" + entityGroupId + '\'' +
			",other_charges = '" + otherCharges + '\'' +
			",entity_address = '" + entityAddress + '\'' +
			",total_tax_amount = '" + totalTaxAmount + '\'' +
			",entity_image = '" + entityImage + '\'' +
			",entity_schedule_id = '" + entityScheduleId + '\'' +
			",entity_packages = '" + entityPackages + '\'' +
			",total_ticket_count = '" + totalTicketCount + '\'' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.entityProductId);
		dest.writeString(this.entityEndTime);
		dest.writeString(this.entityStartTime);
		dest.writeInt(this.entityCategoryId);
		dest.writeString(this.citySearched);
		dest.writeList(this.taxPerQuantity);
		dest.writeInt(this.totalOtherCharges);
		dest.writeList(this.entityPassengers);
		dest.writeInt(this.totalTicketPrice);
		dest.writeString(this.entityCategoryName);
		dest.writeInt(this.entityGroupId);
		dest.writeList(this.otherCharges);
		dest.writeParcelable(this.entityAddress, flags);
		dest.writeInt(this.totalTaxAmount);
		dest.writeString(this.entityImage);
		dest.writeInt(this.entityScheduleId);
		dest.writeList(this.entityPackages);
		dest.writeInt(this.totalTicketCount);
	}

	public MetaData() {
	}

	protected MetaData(Parcel in) {
		this.entityProductId = in.readInt();
		this.entityEndTime = in.readString();
		this.entityStartTime = in.readString();
		this.entityCategoryId = in.readInt();
		this.citySearched = in.readString();
		this.taxPerQuantity = new ArrayList<TaxPerQuantityItem>();
		in.readList(this.taxPerQuantity, TaxPerQuantityItem.class.getClassLoader());
		this.totalOtherCharges = in.readInt();
		this.entityPassengers = new ArrayList<EntityPassengerItem>();
		in.readList(this.entityPassengers, EntityPassengerItem.class.getClassLoader());
		this.totalTicketPrice = in.readInt();
		this.entityCategoryName = in.readString();
		this.entityGroupId = in.readInt();
		this.otherCharges = new ArrayList<OtherChargesItem>();
		in.readList(this.otherCharges, OtherChargesItem.class.getClassLoader());
		this.entityAddress = in.readParcelable(EntityAddress.class.getClassLoader());
		this.totalTaxAmount = in.readInt();
		this.entityImage = in.readString();
		this.entityScheduleId = in.readInt();
		this.entityPackages = new ArrayList<EntityPackageItem>();
		in.readList(this.entityPackages, EntityPackageItem.class.getClassLoader());
		this.totalTicketCount = in.readInt();
	}

	public static final Creator<MetaData> CREATOR = new Creator<MetaData>() {
		@Override
		public MetaData createFromParcel(Parcel source) {
			return new MetaData(source);
		}

		@Override
		public MetaData[] newArray(int size) {
			return new MetaData[size];
		}
	};
}