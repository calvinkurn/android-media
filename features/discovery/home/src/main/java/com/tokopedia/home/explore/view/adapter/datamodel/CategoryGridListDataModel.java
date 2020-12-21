package com.tokopedia.home.explore.view.adapter.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

import java.util.List;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class CategoryGridListDataModel implements Visitable<TypeFactory>, Parcelable {

    private int sectionId;
    private String title;
    private List<LayoutRows> itemList;


    public CategoryGridListDataModel() {
    }

    protected CategoryGridListDataModel(Parcel in) {
        sectionId = in.readInt();
        title = in.readString();
        itemList = in.createTypedArrayList(LayoutRows.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sectionId);
        dest.writeString(title);
        dest.writeTypedList(itemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryGridListDataModel> CREATOR = new Creator<CategoryGridListDataModel>() {
        @Override
        public CategoryGridListDataModel createFromParcel(Parcel in) {
            return new CategoryGridListDataModel(in);
        }

        @Override
        public CategoryGridListDataModel[] newArray(int size) {
            return new CategoryGridListDataModel[size];
        }
    };

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LayoutRows> getItemList() {
        return itemList;
    }

    public void setItemList(List<LayoutRows> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
