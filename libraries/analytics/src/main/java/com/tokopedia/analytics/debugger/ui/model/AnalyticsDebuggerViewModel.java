package com.tokopedia.analytics.debugger.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.database.GtmLogDB;
import com.tokopedia.analytics.debugger.ui.adapter.AnalyticsDebuggerTypeFactory;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerViewModel implements Visitable<AnalyticsDebuggerTypeFactory>,Parcelable {
    private long id;
    private String name;
    private String category;
    private String data;
    private String dataExcerpt;
    private String timestamp;

    public AnalyticsDebuggerViewModel() {

    }

    @Override
    public int type(AnalyticsDebuggerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataExcerpt() {
        return dataExcerpt;
    }

    public void setDataExcerpt(String dataExcerpt) {
        this.dataExcerpt = dataExcerpt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.data);
        dest.writeString(this.dataExcerpt);
        dest.writeString(this.timestamp);
    }

    protected AnalyticsDebuggerViewModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.category = in.readString();
        this.data = in.readString();
        this.dataExcerpt = in.readString();
        this.timestamp = in.readString();
    }

    public static final Parcelable.Creator<AnalyticsDebuggerViewModel> CREATOR = new Parcelable.Creator<AnalyticsDebuggerViewModel>() {
        @Override
        public AnalyticsDebuggerViewModel createFromParcel(Parcel source) {
            return new AnalyticsDebuggerViewModel(source);
        }

        @Override
        public AnalyticsDebuggerViewModel[] newArray(int size) {
            return new AnalyticsDebuggerViewModel[size];
        }
    };
}
