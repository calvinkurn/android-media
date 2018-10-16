package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 14/03/2016.
 */
@Deprecated
public class ProductPreOrder implements Parcelable {
    private static final String TAG = ProductPreOrder.class.getSimpleName();

    @SerializedName("preorder_status")
    @Expose
    private String preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private String preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private String preorderProcessTime;

    public void convertToDay(){
        String result = "";
        switch (Integer.parseInt(preorderProcessTimeType)){
            case 2:
                result = Integer.parseInt(preorderProcessTime)*7+"";
                break;
            case 1:
            default:
                result = preorderProcessTime;
                break;
        }
        preorderProcessTime = result;
    }

    public String getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(String preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public String getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(String preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public String getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(String preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    protected ProductPreOrder(Parcel in) {
        preorderStatus = in.readString();
        preorderProcessTimeType = in.readString();
        preorderProcessTimeTypeString = in.readString();
        preorderProcessTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(preorderStatus);
        dest.writeString(preorderProcessTimeType);
        dest.writeString(preorderProcessTimeTypeString);
        dest.writeString(preorderProcessTime);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductPreOrder> CREATOR =
            new Creator<ProductPreOrder>() {
                @Override
                public ProductPreOrder createFromParcel(Parcel in) {
                    return new ProductPreOrder(in);
                }

                @Override
                public ProductPreOrder[] newArray(int size) {
                    return new ProductPreOrder[size];
                }
            };

    @Override
    public String toString() {
        return "ProductPreOrder{" +
                "preorderStatus='" + preorderStatus + '\'' +
                ", preorderProcessTimeType='" + preorderProcessTimeType + '\'' +
                ", preorderProcessTimeTypeString='" + preorderProcessTimeTypeString + '\'' +
                ", preorderProcessTime='" + preorderProcessTime + '\'' +
                '}';
    }
}
