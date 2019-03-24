package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TradeInParams implements Parcelable {
    public static final String TRADE_IN_PARAMS = TradeInParams.class.getSimpleName();
    public static final String PARAM_NEW_PRICE = "NEW PRICE";
    public static final String PARAM_DEVICE_ID = "DEVICE ID";
    public static final String PARAM_USER_ID = "USER ID";
    public static final String PARAM_PRODUCT_ID = "PRODUCT ID";
    public static final String PARAM_NEW_DEVICE_NAME = "NEW DEVICE NAME";
    public static final String PARAM_USE_KYC = "USE KYC";
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
    private int newprice;
    @SerializedName("IsPreOrder")
    private boolean isPreorder;
    @SerializedName("IsOnCampaign")
    private boolean isOnCampaign;

    private String productName;

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

    public int getNewPrice() {
        return newprice;
    }

    public void setPrice(int price) {
        this.newprice = price;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public boolean isOnCampaign() {
        return isOnCampaign;
    }

    public void setOnCampaign(boolean onCampaign) {
        isOnCampaign = onCampaign;
    }

    public TradeInParams() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeInt(this.shopId);
        dest.writeInt(this.categoryId);
        dest.writeInt(this.userId);
        dest.writeString(this.deviceId);
        dest.writeInt(this.newprice);
        dest.writeInt(isPreorder ? 1 : 0);
        dest.writeInt(isOnCampaign ? 1 : 0);
        dest.writeString(this.productName);
        dest.writeInt(this.usedPrice);
        dest.writeInt(this.remainingPrice);
        dest.writeInt(this.useKyc);
        dest.writeInt(this.isEligible);
    }

    protected TradeInParams(Parcel in) {
        this.productId = in.readInt();
        this.shopId = in.readInt();
        this.categoryId = in.readInt();
        this.userId = in.readInt();
        this.deviceId = in.readString();
        this.newprice = in.readInt();
        this.isPreorder = in.readInt() != 0;
        this.isOnCampaign = in.readInt() != 0;
        this.productName = in.readString();
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
