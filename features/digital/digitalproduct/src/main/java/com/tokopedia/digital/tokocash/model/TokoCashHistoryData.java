package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class TokoCashHistoryData implements Parcelable {

    private List<HeaderHistory> headerHistory;

    private List<ItemHistory> itemHistoryList;

    private boolean next_uri;

    public TokoCashHistoryData() {
    }

    protected TokoCashHistoryData(Parcel in) {
        headerHistory = in.createTypedArrayList(HeaderHistory.CREATOR);
        itemHistoryList = in.createTypedArrayList(ItemHistory.CREATOR);
        next_uri = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(headerHistory);
        dest.writeTypedList(itemHistoryList);
        dest.writeByte((byte) (next_uri ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokoCashHistoryData> CREATOR = new Creator<TokoCashHistoryData>() {
        @Override
        public TokoCashHistoryData createFromParcel(Parcel in) {
            return new TokoCashHistoryData(in);
        }

        @Override
        public TokoCashHistoryData[] newArray(int size) {
            return new TokoCashHistoryData[size];
        }
    };

    public List<HeaderHistory> getHeaderHistory() {
        return headerHistory;
    }

    public void setHeaderHistory(List<HeaderHistory> headerHistory) {
        this.headerHistory = headerHistory;
    }

    public List<ItemHistory> getItemHistoryList() {
        return itemHistoryList;
    }

    public void setItemHistoryList(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList = itemHistoryList;
    }

    public boolean isNext_uri() {
        return next_uri;
    }

    public void setNext_uri(boolean next_uri) {
        this.next_uri = next_uri;
    }
}
