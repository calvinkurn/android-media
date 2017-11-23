
package com.tokopedia.core.network.entity.categories;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child implements Parcelable {

    @SerializedName("child")
    @Expose
    private List<Child> child = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("applinks")
    @Expose
    private String applinks;
    @SerializedName("has_child")
    @Expose
    private Boolean hasChild;

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }


    protected Child(Parcel in) {
        if (in.readByte() == 0x01) {
            child = new ArrayList<Child>();
            in.readList(child, Object.class.getClassLoader());
        } else {
            child = null;
        }
        id = in.readString();
        name = in.readString();
        identifier = in.readString();
        title = in.readString();
        url = in.readString();
        applinks = in.readString();
        byte hasChildVal = in.readByte();
        hasChild = hasChildVal == 0x02 ? null : hasChildVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (child == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(child);
        }
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(identifier);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(applinks);
        if (hasChild == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (hasChild ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}