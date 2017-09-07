package com.tokopedia.digital.tokocash.model;

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

    public ActionHistory() {
    }

    protected ActionHistory(Parcel in) {
        title = in.readString();
        method = in.readString();
        url = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(method);
        dest.writeString(url);
        dest.writeString(name);
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
}
