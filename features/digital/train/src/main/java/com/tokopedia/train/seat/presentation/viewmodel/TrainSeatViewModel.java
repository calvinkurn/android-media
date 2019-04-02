package com.tokopedia.train.seat.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapterTypeFactory;

public class TrainSeatViewModel implements Parcelable, Visitable<TrainSeatAdapterTypeFactory> {
    private String column;
    private int row;
    private boolean available;
    private boolean isEmpty;

    public TrainSeatViewModel() {
    }

    protected TrainSeatViewModel(Parcel in) {
        column = in.readString();
        row = in.readInt();
        available = in.readByte() != 0;
        isEmpty = in.readByte() != 0;
    }

    public static final Creator<TrainSeatViewModel> CREATOR = new Creator<TrainSeatViewModel>() {
        @Override
        public TrainSeatViewModel createFromParcel(Parcel in) {
            return new TrainSeatViewModel(in);
        }

        @Override
        public TrainSeatViewModel[] newArray(int size) {
            return new TrainSeatViewModel[size];
        }
    };

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrainSeatViewModel && ((TrainSeatViewModel) obj).getRow() == row && ((TrainSeatViewModel) obj).getColumn().equals(column);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(column);
        parcel.writeInt(row);
        parcel.writeByte((byte) (available ? 1 : 0));
        parcel.writeByte((byte) (isEmpty ? 1 : 0));
    }

    @Override
    public int type(TrainSeatAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
