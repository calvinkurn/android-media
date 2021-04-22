package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class TickerDataView implements Parcelable, Visitable<ProductListTypeFactory> {
    private String text;
    private String query;
    private int typeId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public TickerDataView() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.query);
        dest.writeInt(this.typeId);
    }

    protected TickerDataView(Parcel in) {
        this.text = in.readString();
        this.query = in.readString();
        this.typeId = in.readInt();
    }

    public static final Creator<TickerDataView> CREATOR = new Creator<TickerDataView>() {
        @Override
        public TickerDataView createFromParcel(Parcel source) {
            return new TickerDataView(source);
        }

        @Override
        public TickerDataView[] newArray(int size) {
            return new TickerDataView[size];
        }
    };
}
