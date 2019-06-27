package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.List;

public class GlobalNavViewModel implements Parcelable {
    private String title;
    private String keyword;
    private String seeAllApplink;
    private String seeAllUrl;
    private List<Item> itemList;

    public GlobalNavViewModel(String title, String keyword, String seeAllApplink, String seeAllUrl, List<Item> itemList) {
        this.title = title;
        this.keyword = keyword;
        this.seeAllApplink = seeAllApplink;
        this.seeAllUrl = seeAllUrl;
        this.itemList = itemList;
    }

    public String getTitle() {
        return title;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSeeAllApplink() {
        return seeAllApplink;
    }

    public String getSeeAllUrl() {
        return seeAllUrl;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public static class Item implements Parcelable {
        private String name;
        private String info;
        private String imageUrl;
        private String applink;
        private String url;
        private int position;

        public Item(String name, String info, String imageUrl, String applink, String url, int position) {
            this.name = name;
            this.info = info;
            this.imageUrl = imageUrl;
            this.applink = applink;
            this.url = url;
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public String getInfo() {
            return info;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getApplink() {
            return applink;
        }

        public String getUrl() {
            return url;
        }

        public Object getGlobalNavItemAsObjectDataLayer() {
            return DataLayer.mapOf(
                    "id", name,
                    "name", "/search result - widget",
                    "creative", name,
                    "position", Integer.toString(position)
            );
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.info);
            dest.writeString(this.imageUrl);
            dest.writeString(this.applink);
            dest.writeString(this.url);
        }

        protected Item(Parcel in) {
            this.name = in.readString();
            this.info = in.readString();
            this.imageUrl = in.readString();
            this.applink = in.readString();
            this.url = in.readString();
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
        dest.writeString(this.title);
        dest.writeString(this.keyword);
        dest.writeString(this.seeAllApplink);
        dest.writeString(this.seeAllUrl);
        dest.writeTypedList(this.itemList);
    }

    protected GlobalNavViewModel(Parcel in) {
        this.title = in.readString();
        this.keyword = in.readString();
        this.seeAllApplink = in.readString();
        this.seeAllUrl = in.readString();
        this.itemList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<GlobalNavViewModel> CREATOR = new Creator<GlobalNavViewModel>() {
        @Override
        public GlobalNavViewModel createFromParcel(Parcel source) {
            return new GlobalNavViewModel(source);
        }

        @Override
        public GlobalNavViewModel[] newArray(int size) {
            return new GlobalNavViewModel[size];
        }
    };
}
