
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantDatum implements Parcelable {

    @SerializedName("pvd_id")
    @Expose
    private Integer pvdId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("PvoListString")
    @Expose
    private String pvoListString;
    @SerializedName("v_code")
    @Expose
    private String vCode;

    public Integer getPvdId() {
        return pvdId;
    }

    public void setPvdId(Integer pvdId) {
        this.pvdId = pvdId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPvoListString() {
        return pvoListString;
    }

    public void setPvoListString(String pvoListString) {
        this.pvoListString = pvoListString;
    }

    public String getVCode() {
        return vCode;
    }

    public void setVCode(String vCode) {
        this.vCode = vCode;
    }


    protected VariantDatum(Parcel in) {
        pvdId = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readByte() == 0x00 ? null : in.readInt();
        stock = in.readByte() == 0x00 ? null : in.readInt();
        pvoListString = in.readString();
        vCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (pvdId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(pvdId);
        }
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        if (stock == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(stock);
        }
        dest.writeString(pvoListString);
        dest.writeString(vCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VariantDatum> CREATOR = new Parcelable.Creator<VariantDatum>() {
        @Override
        public VariantDatum createFromParcel(Parcel in) {
            return new VariantDatum(in);
        }

        @Override
        public VariantDatum[] newArray(int size) {
            return new VariantDatum[size];
        }
    };
}