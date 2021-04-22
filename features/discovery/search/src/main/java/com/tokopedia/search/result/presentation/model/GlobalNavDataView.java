package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import java.util.List;

public class GlobalNavDataView implements Parcelable, Visitable<ProductListTypeFactory> {
    private String source;
    private String title;
    private String keyword;
    private String navTemplate;
    private String background;
    private String seeAllApplink;
    private String seeAllUrl;
    private boolean isShowTopAds;
    private List<Item> itemList;

    public GlobalNavDataView(
            String source,
            String title,
            String keyword,
            String navTemplate,
            String background,
            String seeAllApplink,
            String seeAllUrl,
            boolean isShowTopAds,
            List<Item> itemList
    ) {
        this.source = source;
        this.title = title;
        this.keyword = keyword;
        this.navTemplate = navTemplate;
        this.background = background;
        this.seeAllApplink = seeAllApplink;
        this.seeAllUrl = seeAllUrl;
        this.isShowTopAds = isShowTopAds;
        this.itemList = itemList;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getNavTemplate() {
        return this.navTemplate;
    }

    public String getBackground() {
        return this.background;
    }

    public String getSeeAllApplink() {
        return seeAllApplink;
    }

    public String getSeeAllUrl() {
        return seeAllUrl;
    }

    public boolean getIsShowTopAds() {
        return isShowTopAds;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public static class Item implements Parcelable {

        private String categoryName;
        private String name;
        private String info;
        private String imageUrl;
        private String applink;
        private String url;
        private String subtitle;
        private String strikethrough;
        private String backgroundUrl;
        private String logoUrl;
        private int position;

        public Item(
                String categoryName,
                String name,
                String info,
                String imageUrl,
                String applink,
                String url,
                String subtitle,
                String strikethrough,
                String backgroundUrl,
                String logoUrl,
                int position) {
            this.categoryName = categoryName;
            this.name = name;
            this.info = info;
            this.imageUrl = imageUrl;
            this.applink = applink;
            this.url = url;
            this.subtitle = subtitle;
            this.strikethrough = strikethrough;
            this.backgroundUrl = backgroundUrl;
            this.logoUrl = logoUrl;
            this.position = position;
        }

        public String getCategoryName() {
            return categoryName;
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

        public String getSubtitle() {
            return subtitle;
        }

        public String getStrikethrough() {
            return strikethrough;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public int getPosition() {
            return position;
        }

        public Object getGlobalNavItemAsObjectDataLayer(String creativeName) {
            return DataLayer.mapOf(
                    "id", name,
                    "name", "/search result - widget",
                    "creative", creativeName,
                    "position", Integer.toString(position)
            );
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.categoryName);
            dest.writeString(this.name);
            dest.writeString(this.info);
            dest.writeString(this.imageUrl);
            dest.writeString(this.applink);
            dest.writeString(this.url);
            dest.writeString(this.subtitle);
            dest.writeString(this.strikethrough);
            dest.writeString(this.backgroundUrl);
            dest.writeString(this.logoUrl);
            dest.writeInt(this.position);
        }

        protected Item(Parcel in) {
            this.categoryName = in.readString();
            this.name = in.readString();
            this.info = in.readString();
            this.imageUrl = in.readString();
            this.applink = in.readString();
            this.url = in.readString();
            this.subtitle = in.readString();
            this.strikethrough = in.readString();
            this.backgroundUrl = in.readString();
            this.logoUrl = in.readString();
            this.position = in.readInt();
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
        dest.writeString(this.source);
        dest.writeString(this.title);
        dest.writeString(this.keyword);
        dest.writeString(this.navTemplate);
        dest.writeString(this.background);
        dest.writeString(this.seeAllApplink);
        dest.writeString(this.seeAllUrl);
        dest.writeTypedList(this.itemList);
    }

    protected GlobalNavDataView(Parcel in) {
        this.source = in.readString();
        this.title = in.readString();
        this.keyword = in.readString();
        this.navTemplate = in.readString();
        this.background = in.readString();
        this.seeAllApplink = in.readString();
        this.seeAllUrl = in.readString();
        this.itemList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<GlobalNavDataView> CREATOR = new Creator<GlobalNavDataView>() {
        @Override
        public GlobalNavDataView createFromParcel(Parcel source) {
            return new GlobalNavDataView(source);
        }

        @Override
        public GlobalNavDataView[] newArray(int size) {
            return new GlobalNavDataView[size];
        }
    };
}
