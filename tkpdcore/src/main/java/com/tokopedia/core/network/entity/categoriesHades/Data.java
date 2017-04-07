
package com.tokopedia.core.network.entity.categoriesHades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable{

    public static final int GRID_2_VIEW_TYPE = 1;
    public static final int GRID_1_VIEW_TYPE = 2;
    public static final int LIST_VIEW_TYPE = 3;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("title_tag")
    @Expose
    private String titleTag;
    @SerializedName("is_revamp")
    @Expose
    private Boolean isRevamp;
    @SerializedName("is_intermediary")
    @Expose
    private Boolean isInterMediary;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("header_image")
    @Expose
    private String headerImage;
    @SerializedName("hidden")
    @Expose
    private Integer hidden;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("child")
    @Expose
    private List<Child> child = null;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Boolean getIsInterMediary() {
        return isInterMediary;
    }

    public void setIsInterMediary(Boolean interMediary) {
        isInterMediary = interMediary;
    }

    public Boolean getRevamp() {
        return isRevamp;
    }

    public void setRevamp(Boolean revamp) {
        isRevamp = revamp;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }


    protected Data(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        titleTag = in.readString();
        byte isRevampVal = in.readByte();
        isRevamp = isRevampVal == 0x02 ? null : isRevampVal != 0x00;
        byte isInterMediaryVal = in.readByte();
        isInterMediary = isInterMediaryVal == 0x02 ? null : isInterMediaryVal != 0x00;
        metaDescription = in.readString();
        headerImage = in.readString();
        hidden = in.readByte() == 0x00 ? null : in.readInt();
        view = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            child = new ArrayList<Child>();
            in.readList(child, Child.class.getClassLoader());
        } else {
            child = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(titleTag);
        if (isRevamp == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isRevamp ? 0x01 : 0x00));
        }
        if (isInterMediary == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isInterMediary ? 0x01 : 0x00));
        }
        dest.writeString(metaDescription);
        dest.writeString(headerImage);
        if (hidden == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hidden);
        }
        if (view == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(view);
        }
        if (child == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(child);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

}
