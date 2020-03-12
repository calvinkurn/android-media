package com.tokopedia.analyticsdebugger.debugger.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.ui.adapter.ApplinkDebuggerTypeFactory;

public class ApplinkDebuggerViewModel implements Visitable<ApplinkDebuggerTypeFactory>,Parcelable {
    private long id;
    private String applink;
    private String trace;
    private String previewTrace;
    private String timestamp;

    public ApplinkDebuggerViewModel() {

    }

    @Override
    public int type(ApplinkDebuggerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getPreviewTrace() {
        return previewTrace;
    }

    public void setPreviewTrace(String previewTrace) {
        this.previewTrace = previewTrace;
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
        dest.writeString(this.applink);
        dest.writeString(this.trace);
        dest.writeString(this.previewTrace);
        dest.writeString(this.timestamp);
    }

    protected ApplinkDebuggerViewModel(Parcel in) {
        this.id = in.readLong();
        this.applink = in.readString();
        this.trace = in.readString();
        this.previewTrace = in.readString();
        this.timestamp = in.readString();
    }

    public static final Creator<ApplinkDebuggerViewModel> CREATOR = new Creator<ApplinkDebuggerViewModel>() {
        @Override
        public ApplinkDebuggerViewModel createFromParcel(Parcel source) {
            return new ApplinkDebuggerViewModel(source);
        }

        @Override
        public ApplinkDebuggerViewModel[] newArray(int size) {
            return new ApplinkDebuggerViewModel[size];
        }
    };
}
