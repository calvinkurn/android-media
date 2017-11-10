package com.tokopedia.digital.product.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class CategoryData implements Parcelable {

    public static final String STYLE_PRODUCT_CATEGORY_1 = "style_1";
    public static final String STYLE_PRODUCT_CATEGORY_2 = "style_2";
    public static final String STYLE_PRODUCT_CATEGORY_3 = "style_3";
    public static final String STYLE_PRODUCT_CATEGORY_4 = "style_4";
    public static final String STYLE_PRODUCT_CATEGORY_5 = "style_5";
    public static final String STYLE_PRODUCT_CATEGORY_99 = "style_99";
    public static final String SLUG_PRODUCT_CATEGORY_PULSA = "pulsa";


    private static final String[] STYLE_COLLECTION_SUPPORTED = new String[]{
            STYLE_PRODUCT_CATEGORY_1, STYLE_PRODUCT_CATEGORY_2, STYLE_PRODUCT_CATEGORY_2,
            STYLE_PRODUCT_CATEGORY_3, STYLE_PRODUCT_CATEGORY_4, STYLE_PRODUCT_CATEGORY_5,
            STYLE_PRODUCT_CATEGORY_99
    };

    private String categoryId;
    private String categoryType;

    private String titleText;
    private String name;
    private String icon;
    private String iconUrl;
    private Teaser teaser;
    private boolean isNew;
    private boolean instantCheckout;
    private String slug;
    private String defaultOperatorId;
    private String operatorStyle;
    private String operatorLabel;
    private List<ClientNumber> clientNumberList = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();

    private List<BannerData> bannerDataListIncluded = new ArrayList<>();
    private List<BannerData> otherBannerDataListIncluded = new ArrayList<>();

    private CategoryData(Builder builder) {
        setCategoryId(builder.categoryId);
        setCategoryType(builder.categoryType);
        setTitleText(builder.titleText);
        setName(builder.name);
        setIcon(builder.icon);
        setIconUrl(builder.iconUrl);
        setTeaser(builder.teaser);
        setNew(builder.isNew);
        setInstantCheckout(builder.instantCheckout);
        setSlug(builder.slug);
        setDefaultOperatorId(builder.defaultOperatorId);
        setOperatorStyle(builder.operatorStyle);
        setOperatorLabel(builder.operatorLabel);
        setClientNumberList(builder.clientNumberList);
        setOperatorList(builder.operatorList);
        setBannerDataListIncluded(builder.bannerDataListIncluded);
        setOtherBannerDataListIncluded(builder.otherBannerDataListIncluded);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Teaser getTeaser() {
        return teaser;
    }

    public void setTeaser(Teaser teaser) {
        this.teaser = teaser;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public void setOperatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
    }

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public void setOperatorLabel(String operatorLabel) {
        this.operatorLabel = operatorLabel;
    }

    public List<ClientNumber> getClientNumberList() {
        return clientNumberList;
    }

    public void setClientNumberList(List<ClientNumber> clientNumberList) {
        this.clientNumberList = clientNumberList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public List<Operator> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<Operator> operatorList) {
        this.operatorList = operatorList;
    }

    public boolean isSupportedStyle() {
        return (Arrays.asList(STYLE_COLLECTION_SUPPORTED).contains(operatorStyle));
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public List<BannerData> getBannerDataListIncluded() {
        return bannerDataListIncluded;
    }

    public void setBannerDataListIncluded(List<BannerData> bannerDataListIncluded) {
        this.bannerDataListIncluded = bannerDataListIncluded;
    }

    public List<BannerData> getOtherBannerDataListIncluded() {
        return otherBannerDataListIncluded;
    }

    public void setOtherBannerDataListIncluded(List<BannerData> otherBannerDataListIncluded) {
        this.otherBannerDataListIncluded = otherBannerDataListIncluded;
    }

    public CategoryData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryType);
        dest.writeString(this.titleText);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.iconUrl);
        dest.writeParcelable(this.teaser, flags);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeByte(this.instantCheckout ? (byte) 1 : (byte) 0);
        dest.writeString(this.slug);
        dest.writeString(this.defaultOperatorId);
        dest.writeString(this.operatorStyle);
        dest.writeString(this.operatorLabel);
        dest.writeTypedList(this.clientNumberList);
        dest.writeTypedList(this.operatorList);
        dest.writeTypedList(this.bannerDataListIncluded);
        dest.writeTypedList(this.otherBannerDataListIncluded);
    }

    protected CategoryData(Parcel in) {
        this.categoryId = in.readString();
        this.categoryType = in.readString();
        this.titleText = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.iconUrl = in.readString();
        this.teaser = in.readParcelable(Teaser.class.getClassLoader());
        this.isNew = in.readByte() != 0;
        this.instantCheckout = in.readByte() != 0;
        this.slug = in.readString();
        this.defaultOperatorId = in.readString();
        this.operatorStyle = in.readString();
        this.operatorLabel = in.readString();
        this.clientNumberList = in.createTypedArrayList(ClientNumber.CREATOR);
        this.operatorList = in.createTypedArrayList(Operator.CREATOR);
        this.bannerDataListIncluded = in.createTypedArrayList(BannerData.CREATOR);
        this.otherBannerDataListIncluded = in.createTypedArrayList(BannerData.CREATOR);
    }

    public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel source) {
            return new CategoryData(source);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };

    public static final class Builder {
        private String categoryId;
        private String categoryType;
        private String titleText;
        private String name;
        private String icon;
        private String iconUrl;
        private Teaser teaser;
        private boolean isNew;
        private boolean instantCheckout;
        private String slug;
        private String defaultOperatorId;
        private String operatorStyle;
        private String operatorLabel;
        private List<ClientNumber> clientNumberList;
        private List<Operator> operatorList;
        private List<BannerData> bannerDataListIncluded;
        private List<BannerData> otherBannerDataListIncluded;

        public Builder() {
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder categoryType(String val) {
            categoryType = val;
            return this;
        }

        public Builder titleText(String val) {
            titleText = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder iconUrl(String val) {
            iconUrl = val;
            return this;
        }

        public Builder teaser(Teaser val) {
            teaser = val;
            return this;
        }

        public Builder isNew(boolean val) {
            isNew = val;
            return this;
        }

        public Builder instantCheckout(boolean val) {
            instantCheckout = val;
            return this;
        }

        public Builder slug(String val) {
            slug = val;
            return this;
        }

        public Builder defaultOperatorId(String val) {
            defaultOperatorId = val;
            return this;
        }

        public Builder operatorStyle(String val) {
            operatorStyle = val;
            return this;
        }

        public Builder operatorLabel(String val) {
            operatorLabel = val;
            return this;
        }

        public Builder clientNumberList(List<ClientNumber> val) {
            clientNumberList = val;
            return this;
        }

        public Builder operatorList(List<Operator> val) {
            operatorList = val;
            return this;
        }

        public Builder bannerDataListIncluded(List<BannerData> val) {
            bannerDataListIncluded = val;
            return this;
        }

        public Builder otherBannerDataListIncluded(List<BannerData> val) {
            otherBannerDataListIncluded = val;
            return this;
        }

        public CategoryData build() {
            return new CategoryData(this);
        }
    }
}
