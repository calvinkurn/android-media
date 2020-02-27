package com.tokopedia.analytics.debugger.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.ui.adapter.FpmDebuggerTypeFactory;

public class FpmDebuggerViewModel implements Visitable<FpmDebuggerTypeFactory>,Parcelable {
    private long id;
    private String name;
    private long duration;
    private String metrics;
    private String attributes;
    private String timestamp;

    public FpmDebuggerViewModel() {

    }

    @Override
    public int type(FpmDebuggerTypeFactory typeFactory) {
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
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
        dest.writeLong(this.duration);
        dest.writeString(this.metrics);
        dest.writeString(this.attributes);
        dest.writeString(this.timestamp);
    }

    protected FpmDebuggerViewModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.duration = in.readLong();
        this.metrics = in.readString();
        this.attributes = in.readString();
        this.timestamp = in.readString();
    }

    public static final Creator<FpmDebuggerViewModel> CREATOR = new Creator<FpmDebuggerViewModel>() {
        @Override
        public FpmDebuggerViewModel createFromParcel(Parcel source) {
            return new FpmDebuggerViewModel(source);
        }

        @Override
        public FpmDebuggerViewModel[] newArray(int size) {
            return new FpmDebuggerViewModel[size];
        }
    };
}
