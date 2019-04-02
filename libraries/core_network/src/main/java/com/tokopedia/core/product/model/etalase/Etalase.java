package com.tokopedia.core.product.model.etalase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.GeneralUtils;

/**
 * Created by Angga.Prasetiyo on 13/11/2015.
 */

@Deprecated
public class Etalase implements Parcelable {
    private static final String TAG = Etalase.class.getSimpleName();

    @SerializedName("etalase_id")
    @Expose
    private Integer etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private String etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    private String etalaseTotalProduct;

    public Etalase(Integer i, String s) {
        this.etalaseName = s;
        this.etalaseId = i;

    }

    public Integer getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public String getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    public void setEtalaseNumProduct(String etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    public String getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    public void setEtalaseTotalProduct(String etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

    @Override
    public String toString() {
        return GeneralUtils.fromHtml(etalaseName).toString();
    }

    protected Etalase(Parcel in) {
        etalaseId = in.readByte() == 0x00 ? null : in.readInt();
        etalaseName = in.readString();
        etalaseNumProduct = in.readString();
        etalaseTotalProduct = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (etalaseId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(etalaseId);
        }
        dest.writeString(etalaseName);
        dest.writeString(etalaseNumProduct);
        dest.writeString(etalaseTotalProduct);
    }

    @SuppressWarnings("unused")
    public static final Creator<Etalase> CREATOR = new Creator<Etalase>() {
        @Override
        public Etalase createFromParcel(Parcel in) {
            return new Etalase(in);
        }

        @Override
        public Etalase[] newArray(int size) {
            return new Etalase[size];
        }
    };
}
