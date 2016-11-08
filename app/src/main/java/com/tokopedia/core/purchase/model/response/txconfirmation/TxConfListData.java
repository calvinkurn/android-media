package com.tokopedia.core.purchase.model.response.txconfirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 12/05/2016.
 */
public class TxConfListData implements Parcelable {
    private static final String TAG = TxConfListData.class.getSimpleName();

    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private List<TxConfData> txConfDataList = new ArrayList<>();

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    public List<TxConfData> getTxConfDataList() {
        return txConfDataList;
    }

    public void setTxConfDataList(List<TxConfData> txConfDataList) {
        this.txConfDataList = txConfDataList;
    }

    protected TxConfListData(Parcel in) {
        paging = (PagingHandler.PagingHandlerModel) in.readValue(PagingHandler.PagingHandlerModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            txConfDataList = new ArrayList<TxConfData>();
            in.readList(txConfDataList, TxConfData.class.getClassLoader());
        } else {
            txConfDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paging);
        if (txConfDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(txConfDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxConfListData> CREATOR = new Parcelable.Creator<TxConfListData>() {
        @Override
        public TxConfListData createFromParcel(Parcel in) {
            return new TxConfListData(in);
        }

        @Override
        public TxConfListData[] newArray(int size) {
            return new TxConfListData[size];
        }
    };
}
