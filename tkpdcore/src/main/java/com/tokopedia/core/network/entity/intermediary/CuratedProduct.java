
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CuratedProduct implements Parcelable {

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("sections")
    @Expose
    private List<Section> sections = null;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }


    protected CuratedProduct(Parcel in) {
        categoryId = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            sections = new ArrayList<Section>();
            in.readList(sections, Section.class.getClassLoader());
        } else {
            sections = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (categoryId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(categoryId);
        }
        if (sections == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sections);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CuratedProduct> CREATOR = new Parcelable.Creator<CuratedProduct>() {
        @Override
        public CuratedProduct createFromParcel(Parcel in) {
            return new CuratedProduct(in);
        }

        @Override
        public CuratedProduct[] newArray(int size) {
            return new CuratedProduct[size];
        }
    };
}