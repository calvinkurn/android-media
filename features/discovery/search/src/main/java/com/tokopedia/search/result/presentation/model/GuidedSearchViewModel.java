package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import java.util.List;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchViewModel implements Parcelable {
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public static class Item implements Parcelable {
        private String keyword;
        private String url;
        private int position;
        private String previousKey;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPreviousKey(String previousKey) {
            this.previousKey = previousKey;
        }

        public String getPreviousKey() {
            return previousKey;
        }

        public Item() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.keyword);
            dest.writeString(this.url);
            dest.writeInt(this.position);
            dest.writeString(this.previousKey);
        }

        protected Item(Parcel in) {
            this.keyword = in.readString();
            this.url = in.readString();
            this.position = in.readInt();
            this.previousKey = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.itemList);
    }

    public GuidedSearchViewModel() {
    }

    protected GuidedSearchViewModel(Parcel in) {
        this.itemList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<GuidedSearchViewModel> CREATOR = new Creator<GuidedSearchViewModel>() {
        @Override
        public GuidedSearchViewModel createFromParcel(Parcel source) {
            return new GuidedSearchViewModel(source);
        }

        @Override
        public GuidedSearchViewModel[] newArray(int size) {
            return new GuidedSearchViewModel[size];
        }
    };
}
