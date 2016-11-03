package com.tokopedia.tkpd.purchase.model.response.txconfirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 12/05/2016.
 */
public class TxConfData implements Parcelable {
    private static final String TAG = TxConfData.class.getSimpleName();

    @SerializedName("total_extra_fee_plain")
    @Expose
    private String totalExtraFeePlain;
    @SerializedName("confirmation")
    @Expose
    private Confirmation confirmation;
    @SerializedName("total_extra_fee")
    @Expose
    private String totalExtraFee;
    @SerializedName("order_list")
    @Expose
    private List<OrderData> orderDataList = new ArrayList<>();
    @SerializedName("extra_fee")
    @Expose
    private List<ExtraFee> extraFee = new ArrayList<>();

    private boolean checked;

    public String getTotalExtraFeePlain() {
        return totalExtraFeePlain;
    }

    public void setTotalExtraFeePlain(String totalExtraFeePlain) {
        this.totalExtraFeePlain = totalExtraFeePlain;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public String getTotalExtraFee() {
        return totalExtraFee;
    }

    public void setTotalExtraFee(String totalExtraFee) {
        this.totalExtraFee = totalExtraFee;
    }

    public List<OrderData> getOrderDataList() {
        return orderDataList;
    }

    public void setOrderDataList(List<OrderData> orderDataList) {
        this.orderDataList = orderDataList;
    }

    public List<ExtraFee> getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(List<ExtraFee> extraFee) {
        this.extraFee = extraFee;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof TxConfData) {
            TxConfData data = (TxConfData) o;
            if (this.confirmation != null) {
                return (this.confirmation.getConfirmationId()
                        .equals(data.getConfirmation().getConfirmationId()));
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.confirmation != null) {
            return (Integer.parseInt(this.confirmation.getConfirmationId())
                    * this.confirmation.getCreateTime().hashCode()
                    * this.confirmation.getPayDueDate().hashCode());
        } else {
            return super.hashCode();
        }
    }

    protected TxConfData(Parcel in) {
        totalExtraFeePlain = in.readString();
        confirmation = (Confirmation) in.readValue(Confirmation.class.getClassLoader());
        totalExtraFee = in.readString();
        if (in.readByte() == 0x01) {
            orderDataList = new ArrayList<OrderData>();
            in.readList(orderDataList, OrderData.class.getClassLoader());
        } else {
            orderDataList = null;
        }
        if (in.readByte() == 0x01) {
            extraFee = new ArrayList<ExtraFee>();
            in.readList(extraFee, ExtraFee.class.getClassLoader());
        } else {
            extraFee = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalExtraFeePlain);
        dest.writeValue(confirmation);
        dest.writeString(totalExtraFee);
        if (orderDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(orderDataList);
        }
        if (extraFee == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(extraFee);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxConfData> CREATOR = new Parcelable.Creator<TxConfData>() {
        @Override
        public TxConfData createFromParcel(Parcel in) {
            return new TxConfData(in);
        }

        @Override
        public TxConfData[] newArray(int size) {
            return new TxConfData[size];
        }
    };
}
