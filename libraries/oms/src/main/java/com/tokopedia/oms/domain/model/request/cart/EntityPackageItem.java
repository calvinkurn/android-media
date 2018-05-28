package com.tokopedia.oms.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityPackageItem implements Parcelable {

    @SerializedName("price_per_seat")
    private Integer pricePerSeat;

    @SerializedName("seat_row_id")
    private List<String> seatRowId;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("seat_id")
    private List<String> seatId;

    @SerializedName("seat_physical_row_id")
    private List<String> seatPhysicalRowId;

    @SerializedName("area_id")
    private String areaId;

    @SerializedName("actual_seat_nos")
    private List<String> actualSeatNos;

    @SerializedName("provider_group_id")
    private String providerGroupId;

    @SerializedName("area_code")
    private List<String> areaCode;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("description")
    private String description;

    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("package_id")
    private Integer packageId;

    @SerializedName("schedule_id")
    private Integer scheduleId;

    public void setPricePerSeat(Integer pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Integer getPricePerSeat() {
        return pricePerSeat;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setSeatId(List<String> seatId) {
        this.seatId = seatId;
    }

    public List<String> getSeatId() {
        return seatId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setSeatRowId(List<String> seatRowId) {
        this.seatRowId = seatRowId;
    }

    public List<String> getSeatPhysicalRowId() {
        return seatPhysicalRowId;
    }

    public void setSeatPhysicalRowId(List<String> seatPhysicalRowId) {
        this.seatPhysicalRowId = seatPhysicalRowId;
    }

    public List<String> getActualSeatNos() {
        return actualSeatNos;
    }

    public void setActualSeatNos(List<String> actualSeatNos) {
        this.actualSeatNos = actualSeatNos;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getProviderGroupId() {
        return providerGroupId;
    }

    public void setProviderGroupId(String providerGroupId) {
        this.providerGroupId = providerGroupId;
    }

    public void setAreaCode(List<String> areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return
                "EntityPackageItem{" +
                        "price_per_seat = '" + pricePerSeat + '\'' +
                        ",seat_row_id = '" + seatRowId + '\'' +
                        ",quantity = '" + quantity + '\'' +
                        ",group_id = '" + groupId + '\'' +
                        ",seat_id = '" + seatId + '\'' +
                        ",area_code = '" + areaCode + '\'' +
                        ",product_id = '" + productId + '\'' +
                        ",description = '" + description + '\'' +
                        ",session_id = '" + sessionId + '\'' +
                        ",package_id = '" + packageId + '\'' +
                        ",schedule_id = '" + scheduleId + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pricePerSeat);
        dest.writeStringList(this.seatRowId);
        dest.writeInt(this.quantity);
        dest.writeInt(this.groupId);
        dest.writeStringList(this.seatId);
        dest.writeStringList(this.seatPhysicalRowId);
        dest.writeString(this.areaId);
        dest.writeStringList(this.actualSeatNos);
        dest.writeString(this.providerGroupId);
        dest.writeStringList(this.areaCode);
        dest.writeInt(this.productId);
        dest.writeString(this.description);
        dest.writeString(this.sessionId);
        dest.writeInt(this.packageId);
        dest.writeInt(this.scheduleId);
    }

    public EntityPackageItem() {
    }

    protected EntityPackageItem(Parcel in) {
        this.pricePerSeat = in.readInt();
        this.seatRowId = in.createStringArrayList();
        this.quantity = in.readInt();
        this.groupId = in.readInt();
        this.seatId = in.createStringArrayList();
        this.seatPhysicalRowId = in.createStringArrayList();
        this.areaId = in.readString();
        this.actualSeatNos = in.createStringArrayList();
        this.providerGroupId = in.readString();
        this.areaCode = in.createStringArrayList();
        this.productId = in.readInt();
        this.description = in.readString();
        this.sessionId = in.readString();
        this.packageId = in.readInt();
        this.scheduleId = in.readInt();
    }

    public static final Creator<EntityPackageItem> CREATOR = new Creator<EntityPackageItem>() {
        @Override
        public EntityPackageItem createFromParcel(Parcel source) {
            return new EntityPackageItem(source);
        }

        @Override
        public EntityPackageItem[] newArray(int size) {
            return new EntityPackageItem[size];
        }
    };
}