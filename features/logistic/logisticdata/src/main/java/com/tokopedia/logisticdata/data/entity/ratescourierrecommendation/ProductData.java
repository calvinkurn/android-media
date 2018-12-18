package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ProductData implements Parcelable {

    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("shipper_id")
    @Expose
    private int shipperId;
    @SerializedName("shipper_product_id")
    @Expose
    private int shipperProductId;
    @SerializedName("shipper_product_name")
    @Expose
    private String shipperProductName;
    @SerializedName("shipper_product_desc")
    @Expose
    private String shipperProductDesc;
    @SerializedName("shipper_weight")
    @Expose
    private int shipperWeight;
    @SerializedName("is_show_map")
    @Expose
    private int isShowMap;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("recommend")
    @Expose
    private boolean recommend;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String unixTime;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("price")
    @Expose
    private PriceData price;
    @SerializedName("etd")
    @Expose
    private EstimatedTimeDeliveryData etd;
    @SerializedName("insurance")
    @Expose
    private InsuranceData insurance;
    @SerializedName("texts")
    @Expose
    private ProductTextData texts;
    @SerializedName("error")
    @Expose
    private ErrorProductData error;
    @SerializedName("cod")
    @Expose
    private CodProductData codProductData;

    public ProductData() {
    }

    protected ProductData(Parcel in) {
        shipperName = in.readString();
        shipperId = in.readInt();
        shipperProductId = in.readInt();
        shipperProductName = in.readString();
        shipperProductDesc = in.readString();
        shipperWeight = in.readInt();
        isShowMap = in.readInt();
        status = in.readInt();
        recommend = in.readByte() != 0;
        checkSum = in.readString();
        unixTime = in.readString();
        promoCode = in.readString();
        price = in.readParcelable(PriceData.class.getClassLoader());
        etd = in.readParcelable(EstimatedTimeDeliveryData.class.getClassLoader());
        insurance = in.readParcelable(InsuranceData.class.getClassLoader());
        texts = in.readParcelable(ProductTextData.class.getClassLoader());
        error = in.readParcelable(ErrorProductData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipperName);
        dest.writeInt(shipperId);
        dest.writeInt(shipperProductId);
        dest.writeString(shipperProductName);
        dest.writeString(shipperProductDesc);
        dest.writeInt(shipperWeight);
        dest.writeInt(isShowMap);
        dest.writeInt(status);
        dest.writeByte((byte) (recommend ? 1 : 0));
        dest.writeString(checkSum);
        dest.writeString(unixTime);
        dest.writeString(promoCode);
        dest.writeParcelable(price, flags);
        dest.writeParcelable(etd, flags);
        dest.writeParcelable(insurance, flags);
        dest.writeParcelable(texts, flags);
        dest.writeParcelable(error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel in) {
            return new ProductData(in);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(int shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getShipperProductName() {
        return shipperProductName;
    }

    public void setShipperProductName(String shipperProductName) {
        this.shipperProductName = shipperProductName;
    }

    public String getShipperProductDesc() {
        return shipperProductDesc;
    }

    public void setShipperProductDesc(String shipperProductDesc) {
        this.shipperProductDesc = shipperProductDesc;
    }

    public int getShipperWeight() {
        return shipperWeight;
    }

    public void setShipperWeight(int shipperWeight) {
        this.shipperWeight = shipperWeight;
    }

    public int getIsShowMap() {
        return isShowMap;
    }

    public void setIsShowMap(int isShowMap) {
        this.isShowMap = isShowMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public PriceData getPrice() {
        return price;
    }

    public void setPrice(PriceData price) {
        this.price = price;
    }

    public EstimatedTimeDeliveryData getEtd() {
        return etd;
    }

    public void setEtd(EstimatedTimeDeliveryData etd) {
        this.etd = etd;
    }

    public InsuranceData getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceData insurance) {
        this.insurance = insurance;
    }

    public ProductTextData getTexts() {
        return texts;
    }

    public void setTexts(ProductTextData texts) {
        this.texts = texts;
    }

    public ErrorProductData getError() {
        return error;
    }

    public void setError(ErrorProductData error) {
        this.error = error;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public CodProductData getCodProductData() {
        return codProductData;
    }

    public void setCodProductData(CodProductData codProductData) {
        this.codProductData = codProductData;
    }
}
