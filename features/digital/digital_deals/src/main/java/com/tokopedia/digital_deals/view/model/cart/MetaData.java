package com.tokopedia.digital_deals.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MetaData implements Parcelable {

    @SerializedName("entity_product_id")
    private Integer entityProductId;

    @SerializedName("entity_end_time")
    private String entityEndTime;

    @SerializedName("entity_start_time")
    private String entityStartTime;

    @SerializedName("entity_category_id")
    private Integer entityCategoryId;


    @SerializedName("total_ticket_price")
    private Integer totalTicketPrice;

    @SerializedName("entity_category_name")
    private String entityCategoryName;

    @SerializedName("entity_group_id")
    private Integer entityGroupId;


    @SerializedName("total_ticket_count")
    private Integer totalTicketCount;

    public void setEntityProductId(int entityProductId) {
        this.entityProductId = entityProductId;
    }

    public Integer getEntityProductId() {
        return entityProductId;
    }

    public void setEntityEndTime(String entityEndTime) {
        this.entityEndTime = entityEndTime;
    }

    public String getEntityEndTime() {
        return entityEndTime;
    }

    public void setEntityStartTime(String entityStartTime) {
        this.entityStartTime = entityStartTime;
    }

    public String getEntityStartTime() {
        return entityStartTime;
    }

    public void setEntityCategoryId(Integer entityCategoryId) {
        this.entityCategoryId = entityCategoryId;
    }

    public Integer getEntityCategoryId() {
        return entityCategoryId;
    }


    public void setTotalTicketPrice(Integer totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public Integer getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public void setEntityCategoryName(String entityCategoryName) {
        this.entityCategoryName = entityCategoryName;
    }

    public String getEntityCategoryName() {
        return entityCategoryName;
    }

    public void setEntityGroupId(Integer entityGroupId) {
        this.entityGroupId = entityGroupId;
    }

    public Integer getEntityGroupId() {
        return entityGroupId;
    }

    public void setTotalTicketCount(Integer totalTicketCount) {
        this.totalTicketCount = totalTicketCount;
    }

    public Integer getTotalTicketCount() {
        return totalTicketCount;
    }

    @Override
    public String toString() {
        return
                "MetaData{" +
                        "entity_product_id = '" + entityProductId + '\'' +
                        ",entity_end_time = '" + entityEndTime + '\'' +
                        ",entity_start_time = '" + entityStartTime + '\'' +
                        ",entity_category_id = '" + entityCategoryId + '\'' +
                        ",total_ticket_price = '" + totalTicketPrice + '\'' +
                        ",entity_category_name = '" + entityCategoryName + '\'' +
                        ",entity_group_id = '" + entityGroupId + '\'' +
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
        dest.writeInt(this.totalTicketPrice);
        dest.writeString(this.entityCategoryName);
        dest.writeInt(this.entityGroupId);
        dest.writeInt(this.totalTicketCount);
    }

    public MetaData() {
    }

    protected MetaData(Parcel in) {
        this.entityProductId = in.readInt();
        this.entityEndTime = in.readString();
        this.entityStartTime = in.readString();
        this.entityCategoryId = in.readInt();
        this.totalTicketPrice = in.readInt();
        this.entityCategoryName = in.readString();
        this.entityGroupId = in.readInt();
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