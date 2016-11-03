package com.tokopedia.tkpd.purchase.model.response.txverification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 25/05/2016.
 */
public class TxVerListData implements Parcelable {
    private static final String TAG = TxVerListData.class.getSimpleName();
    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("list")
    @Expose
    private List<TxVerData> txVerDataList = new ArrayList<>();

    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    public List<TxVerData> getTxVerDataList() {
        return txVerDataList;
    }

    public void setTxVerDataList(List<TxVerData> txVerDataList) {
        this.txVerDataList = txVerDataList;
    }

    protected TxVerListData(Parcel in) {
        paging = (PagingHandler.PagingHandlerModel) in.readValue(PagingHandler.PagingHandlerModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            txVerDataList = new ArrayList<TxVerData>();
            in.readList(txVerDataList, TxVerData.class.getClassLoader());
        } else {
            txVerDataList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paging);
        if (txVerDataList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(txVerDataList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TxVerListData> CREATOR = new Parcelable.Creator<TxVerListData>() {
        @Override
        public TxVerListData createFromParcel(Parcel in) {
            return new TxVerListData(in);
        }

        @Override
        public TxVerListData[] newArray(int size) {
            return new TxVerListData[size];
        }
    };
}
