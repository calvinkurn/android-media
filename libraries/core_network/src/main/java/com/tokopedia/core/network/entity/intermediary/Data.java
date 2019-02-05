
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Data implements Parcelable {

    public static final int GRID_2_VIEW_TYPE = 1;
    public static final int GRID_1_VIEW_TYPE = 2;
    public static final int LIST_VIEW_TYPE = 3;

    @SerializedName("child")
    @Expose
    private List<Child> child = null;
    @SerializedName("curated_product")
    @Expose
    private CuratedProduct curatedProduct;
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
    @SerializedName("is_revamp")
    @Expose
    private Boolean isRevamp = false;
    @SerializedName("is_intermediary")
    @Expose
    private Boolean isIntermediary = false;
    @SerializedName("banner")
    @Expose
    private Banner banner;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("applinks")
    @Expose
    private String applinks;
    @SerializedName("video")
    @Expose
    private Video video;
    @SerializedName("root_category_id")
    @Expose
    private Integer rootCategoryId;
    @SerializedName("header_image_hex_color")
    @Expose
    private String headerHexColor;


    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }

    public CuratedProduct getCuratedProduct() {
        return curatedProduct;
    }

    public void setCuratedProduct(CuratedProduct curatedProduct) {
        this.curatedProduct = curatedProduct;
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

    public Boolean getIsRevamp() {
        return isRevamp;
    }

    public void setIsRevamp(Boolean isRevamp) {
        this.isRevamp = isRevamp;
    }

    public Boolean getIsIntermediary() {
        return isIntermediary;
    }

    public void setIsIntermediary(Boolean isIntermediary) {
        this.isIntermediary = isIntermediary;
    }

    public Boolean getRevamp() {
        return isRevamp;
    }

    public void setRevamp(Boolean revamp) {
        isRevamp = revamp;
    }

    public Boolean getIntermediary() {
        return isIntermediary;
    }

    public void setIntermediary(Boolean intermediary) {
        isIntermediary = intermediary;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Integer getRootCategoryId() {
        return rootCategoryId;
    }

    public void setRootCategoryId(Integer rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    public String getHeaderHexColor() {
        return headerHexColor;
    }

    public void setHeaderHexColor(String headerHexColor) {
        this.headerHexColor = headerHexColor;
    }

    protected Data(Parcel in) {
        if (in.readByte() == 0x01) {
            child = new ArrayList<Child>();
            in.readList(child, Child.class.getClassLoader());
        } else {
            child = null;
        }
        curatedProduct = (CuratedProduct) in.readValue(CuratedProduct.class.getClassLoader());
        id = in.readString();
        name = in.readString();
        description = in.readString();
        titleTag = in.readString();
        metaDescription = in.readString();
        headerImage = in.readString();
        hidden = in.readByte() == 0x00 ? null : in.readInt();
        view = in.readByte() == 0x00 ? null : in.readInt();
        byte isRevampVal = in.readByte();
        isRevamp = isRevampVal == 0x02 ? null : isRevampVal != 0x00;
        byte isIntermediaryVal = in.readByte();
        isIntermediary = isIntermediaryVal == 0x02 ? null : isIntermediaryVal != 0x00;
        banner = (Banner) in.readValue(Banner.class.getClassLoader());
        template = in.readString();
        applinks = in.readString();
        video = (Video) in.readValue(Video.class.getClassLoader());
        rootCategoryId = in.readByte() == 0x00 ? null : in.readInt();
        headerHexColor = in.readString();
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
        dest.writeValue(curatedProduct);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(titleTag);
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
        if (isRevamp == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isRevamp ? 0x01 : 0x00));
        }
        if (isIntermediary == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isIntermediary ? 0x01 : 0x00));
        }
        dest.writeValue(banner);
        dest.writeString(template);
        dest.writeString(applinks);
        dest.writeValue(video);
        if (rootCategoryId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rootCategoryId);
        }
        dest.writeString(headerHexColor);
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