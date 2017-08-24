
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VariantDatum implements Parcelable {

    @SerializedName("pvd_id")
    @Expose
    private Integer pvdId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("option_ids")
    @Expose
    private List<Integer> optionIds = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("v_code")
    @Expose
    private String vCode;

    public Integer getPvdId() {
        return pvdId;
    }

    public void setPvdId(Integer pvdId) {
        this.pvdId = pvdId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
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

    public String getVCode() {
        return vCode;
    }

    public void setVCode(String vCode) {
        this.vCode = vCode;
    }

    protected VariantDatum(Parcel in) {
        pvdId = in.readByte() == 0x00 ? null : in.readInt();
        productId = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            optionIds = new ArrayList<Integer>();
            in.readList(optionIds, Integer.class.getClassLoader());
        } else {
            optionIds = null;
        }
        status = in.readByte() == 0x00 ? null : in.readInt();
        stock = in.readByte() == 0x00 ? null : in.readInt();
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
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        if (optionIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(optionIds);
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