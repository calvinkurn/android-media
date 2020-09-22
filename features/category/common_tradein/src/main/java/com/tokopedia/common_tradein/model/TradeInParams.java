package com.tokopedia.common_tradein.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TradeInParams implements Parcelable {
    public static final String TRADE_IN_PARAMS = TradeInParams.class.getSimpleName();
    public static final String PARAM_NEW_PRICE = "NEW PRICE";
    public static final String PARAM_DEVICE_ID = "DEVICE ID";
    public static final String PARAM_PHONE_TYPE = "PHONE TYPE";
    public static final String PARAM_PHONE_PRICE = "PHONE PRICE";
    public static final String PARAM_USER_ID = "USER ID";
    public static final String PARAM_PRODUCT_ID = "PRODUCT ID";
    public static final String PARAM_NEW_DEVICE_NAME = "NEW DEVICE NAME";
    public static final String PARAM_USE_KYC = "USE KYC";
    public static final String PARAM_PERMISSION_GIVEN = "PERMISSION GIVEN";
    public static final int HANDFONE_ID = 24;
    public static final String HANDFONE = "Handphone";
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
    @SerializedName("TradeInType")
    private int tradeInType;
    @SerializedName("ModelId")
    private int modelID;
    @SerializedName("WidgetString")
    private String widgetString;

    private String productName;

    private int usedPrice;
    private int remainingPrice;
    private int useKyc;
    private int isEligible;
    private String origin;
    private String productImage;
    private int weight;

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

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
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

    public int getTradeInType() {
        return tradeInType;
    }

    public void setTradeInType(int tradeInType) {
        this.tradeInType = tradeInType;
    }

    public String getWidgetString() {
        return widgetString;
    }

    public void setWidgetString(String widgetString) {
        this.widgetString = widgetString;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public TradeInParams() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeInt(shopId);
        dest.writeInt(categoryId);
        dest.writeInt(userId);
        dest.writeString(deviceId);
        dest.writeInt(newprice);
        dest.writeByte((byte) (isPreorder ? 1 : 0));
        dest.writeByte((byte) (isOnCampaign ? 1 : 0));
        dest.writeInt(tradeInType);
        dest.writeInt(modelID);
        dest.writeString(widgetString);
        dest.writeString(productName);
        dest.writeInt(usedPrice);
        dest.writeInt(remainingPrice);
        dest.writeInt(useKyc);
        dest.writeInt(isEligible);
        dest.writeString(origin);
        dest.writeString(productImage);
        dest.writeInt(weight);
    }

    protected TradeInParams(Parcel in) {
        productId = in.readInt();
        shopId = in.readInt();
        categoryId = in.readInt();
        userId = in.readInt();
        deviceId = in.readString();
        newprice = in.readInt();
        isPreorder = in.readByte() != 0;
        isOnCampaign = in.readByte() != 0;
        tradeInType = in.readInt();
        modelID = in.readInt();
        widgetString = in.readString();
        productName = in.readString();
        usedPrice = in.readInt();
        remainingPrice = in.readInt();
        useKyc = in.readInt();
        isEligible = in.readInt();
        origin = in.readString();
        productImage = in.readString();
        weight = in.readInt();
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
