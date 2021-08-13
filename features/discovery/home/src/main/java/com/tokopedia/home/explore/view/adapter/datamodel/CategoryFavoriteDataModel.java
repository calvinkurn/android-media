package com.tokopedia.home.explore.view.adapter.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.explore.domain.model.LayoutRows;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by errysuprayogi on 1/31/18.
 */

public class CategoryFavoriteDataModel implements Visitable<TypeFactory>, Parcelable {

    private int sectionId;
    private String title;
    private List<LayoutRows> itemList;

    public CategoryFavoriteDataModel() {
    }

    protected CategoryFavoriteDataModel(Parcel in) {
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

    public static final Creator<CategoryFavoriteDataModel> CREATOR = new Creator<CategoryFavoriteDataModel>() {
        @Override
        public CategoryFavoriteDataModel createFromParcel(Parcel in) {
            return new CategoryFavoriteDataModel(in);
        }

        @Override
        public CategoryFavoriteDataModel[] newArray(int size) {
            return new CategoryFavoriteDataModel[size];
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

    public Map<String, Object> getHomePageEnhanceDataLayer() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getItemList().size(); i++) {
            list.add(
                    convertFavCategoryItemIntoDataLayer(getItemList().get(i), i + 1)
            );
        }
        return DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage",
                "eventAction", "beli ini itu favorite category impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])

                                )
                        )
                )
        );
    }

    private Map<String, Object> convertFavCategoryItemIntoDataLayer(LayoutRows item, int position) {
        return DataLayer.mapOf(
                "id", String.valueOf(item.getId()),
                "name", "/explore beli - p1 - Kategori Favorit Anda",
                "creative", item.getName(),
                "position", String.valueOf(position)
        );
    }
}
