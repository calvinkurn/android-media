package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TradeInParams implements Parcelable {
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("ShopId")
    private int shopId;
    @SerializedName("CategoryId")
    private int categoryId;
    @SerializedName("UserId")
    private int userId;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("NewPrice")
    private int price;
    @SerializedName("Source")
    private String source = "pdp-android";

    private int usedPrice;
    private int remainingPrice;
    private int useKyc;
    private int isEligible;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(int usedPrice) {
        this.usedPrice = usedPrice;
    }

    public int getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(int remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public int isUseKyc() {
        return useKyc;
    }

    public void setUseKyc(int useKyc) {
        this.useKyc = useKyc;
    }

    public int getIsEligible() {
        return isEligible;
    }

    public void setIsEligible(int isEligible) {
        this.isEligible = isEligible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.price);
        dest.writeInt(this.usedPrice);
        dest.writeInt(this.remainingPrice);
        dest.writeInt(this.useKyc);
        dest.writeInt(this.isEligible);
    }

    public TradeInParams() {
    }

    protected TradeInParams(Parcel in) {
        this.price = in.readInt();
        this.usedPrice = in.readInt();
        this.remainingPrice = in.readInt();
        this.useKyc = in.readInt();
        this.isEligible = in.readInt();
    }

    public static final Creator<TradeInParams> CREATOR = new Creator<TradeInParams>() {
        @Override
        public TradeInParams createFromParcel(Parcel source) {
            return new TradeInParams(source);
        }

        @Override
        public TradeInParams[] newArray(int size) {
            return new TradeInParams[size];
        }
    };
}
