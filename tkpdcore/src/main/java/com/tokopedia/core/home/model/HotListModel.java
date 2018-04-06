package com.tokopedia.core.home.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

/**
 * Created by m.normansyah on 28/10/2015.
 */
public class HotListModel extends RecyclerViewItem{
    @SerializedName("id")
    String hotListId;
    @SerializedName("title")
    String hotListName;
    @SerializedName("price_start")
    String hotListPrice;
    @SerializedName("image_url")
    String hotListImage;
    @SerializedName("image_url_600")
    String hotListBiggerImage;
    @SerializedName("url")
    String hotListProductUrl;
    String hotListKey;
    private int trackerEnhancePosition;
    private String trackerEnhanceName;

    public HotListModel(){
        setType(TkpdState.RecyclerView.VIEW_STANDARD);
    }

    public String getHotListId() {
        return hotListId;
    }

    public void setHotListId(String hotListId) {
        this.hotListId = hotListId;
    }

    public String getHotListImage() {
        return hotListImage;
    }

    public void setHotListImage(String hotListImage) {
        this.hotListImage = hotListImage;
    }

    public String getHotListPrice() {
        return hotListPrice;
    }

    public void setHotListPrice(String hotListPrice) {
        this.hotListPrice = hotListPrice;
    }

    public String getHotListName() {
        return hotListName;
    }

    public void setHotListName(String hotListName) {
        this.hotListName = hotListName;
    }

    public String getHotListBiggerImage() {
        return hotListBiggerImage;
    }

    public void setHotListBiggerImage(String hotListBiggerImage) {
        this.hotListBiggerImage = hotListBiggerImage;
    }

    public String getHotListProductUrl() {
        return hotListProductUrl;
    }

    public void setHotListProductUrl(String hotListProductUrl) {
        this.hotListProductUrl = hotListProductUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotListModel that = (HotListModel) o;

        if (hotListName != null ? !hotListName.equals(that.hotListName) : that.hotListName != null)
            return false;
        if (hotListPrice != null ? !hotListPrice.equals(that.hotListPrice) : that.hotListPrice != null)
            return false;
        if (hotListImage != null ? !hotListImage.equals(that.hotListImage) : that.hotListImage != null)
            return false;
        if (hotListBiggerImage != null ? !hotListBiggerImage.equals(that.hotListBiggerImage) : that.hotListBiggerImage != null)
            return false;
        if (hotListProductUrl != null ? !hotListProductUrl.equals(that.hotListProductUrl) : that.hotListProductUrl != null)
            return false;
        return !(hotListKey != null ? !hotListKey.equals(that.hotListKey) : that.hotListKey != null);

    }

    @Override
    public int hashCode() {
        int result = hotListName != null ? hotListName.hashCode() : 0;
        result = 31 * result + (hotListPrice != null ? hotListPrice.hashCode() : 0);
        result = 31 * result + (hotListImage != null ? hotListImage.hashCode() : 0);
        result = 31 * result + (hotListBiggerImage != null ? hotListBiggerImage.hashCode() : 0);
        result = 31 * result + (hotListProductUrl != null ? hotListProductUrl.hashCode() : 0);
        result = 31 * result + (hotListKey != null ? hotListKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotListModel{" +
                "hotListName='" + hotListName + '\'' +
                ", hotListPrice='" + hotListPrice + '\'' +
                ", hotListImage='" + hotListImage + '\'' +
                ", hotListBiggerImage='" + hotListBiggerImage + '\'' +
                ", hotListProductUrl='" + hotListProductUrl + '\'' +
                ", hotListKey='" + hotListKey + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.hotListName);
        dest.writeString(this.hotListPrice);
        dest.writeString(this.hotListImage);
        dest.writeString(this.hotListBiggerImage);
        dest.writeString(this.hotListProductUrl);
        dest.writeString(this.hotListKey);
        dest.writeString(this.hotListId);
        dest.writeString(this.trackerEnhanceName);
        dest.writeInt(this.trackerEnhancePosition);
    }

    protected HotListModel(Parcel in) {
        super(in);
        this.hotListName = in.readString();
        this.hotListPrice = in.readString();
        this.hotListImage = in.readString();
        this.hotListBiggerImage = in.readString();
        this.hotListProductUrl = in.readString();
        this.hotListKey = in.readString();
        this.hotListId = in.readString();
        this.trackerEnhanceName = in.readString();
        this.trackerEnhancePosition = in.readInt();
    }

    public static final Creator<HotListModel> CREATOR = new Creator<HotListModel>() {
        @Override
        public HotListModel createFromParcel(Parcel source) {
            return new HotListModel(source);
        }

        @Override
        public HotListModel[] newArray(int size) {
            return new HotListModel[size];
        }
    };

    public void setTrackerEnhancePosition(int trackerEnhancePosition) {
        this.trackerEnhancePosition = trackerEnhancePosition;
    }

    public int getTrackerEnhancePosition() {
        return trackerEnhancePosition;
    }

    public void setTrackerEnhanceName(String trackerEnhanceName) {
        this.trackerEnhanceName = trackerEnhanceName;
    }

    public String getTrackerEnhanceName() {
        return trackerEnhanceName;
    }
}
