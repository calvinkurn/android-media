package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ActionHistory implements Parcelable {

    private String title;

    private String method;

    private String url;

    private ParamsActionHistory params;

    private String name;

    private String type;

    public ActionHistory() {
    }

    protected ActionHistory(Parcel in) {
        title = in.readString();
        method = in.readString();
        url = in.readString();
        params = in.readParcelable(ParamsActionHistory.class.getClassLoader());
        name = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(method);
        dest.writeString(url);
        dest.writeParcelable(params, flags);
        dest.writeString(name);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActionHistory> CREATOR = new Creator<ActionHistory>() {
        @Override
        public ActionHistory createFromParcel(Parcel in) {
            return new ActionHistory(in);
        }

        @Override
        public ActionHistory[] newArray(int size) {
            return new ActionHistory[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ParamsActionHistory getParams() {
        return params;
    }

    public void setParams(ParamsActionHistory params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
