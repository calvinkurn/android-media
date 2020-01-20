package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

public class TickerViewModel implements Parcelable, Visitable<ProductListTypeFactory> {
    private String text;
    private String query;

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

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public TickerViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.query);
    }

    protected TickerViewModel(Parcel in) {
        this.text = in.readString();
        this.query = in.readString();
    }

    public static final Creator<TickerViewModel> CREATOR = new Creator<TickerViewModel>() {
        @Override
        public TickerViewModel createFromParcel(Parcel source) {
            return new TickerViewModel(source);
        }

        @Override
        public TickerViewModel[] newArray(int size) {
            return new TickerViewModel[size];
        }
    };
}
