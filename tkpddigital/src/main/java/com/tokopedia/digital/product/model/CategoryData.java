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

    private static final String[] STYLE_COLLECTION_SUPPORTED = new String[]{
            STYLE_PRODUCT_CATEGORY_1, STYLE_PRODUCT_CATEGORY_2, STYLE_PRODUCT_CATEGORY_2,
            STYLE_PRODUCT_CATEGORY_3, STYLE_PRODUCT_CATEGORY_4, STYLE_PRODUCT_CATEGORY_5,
            STYLE_PRODUCT_CATEGORY_99
    };

    private String categoryId;
    private String categoryType;

    private String name;
    private String icon;
    private String iconUrl;
    private Teaser teaser;
    private boolean isNew;
    private boolean instantCheckout;
    private String slug;
    private String defaultOperatorId;
    private String operatorStyle;
    private List<ClientNumber> clientNumberList = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryType);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.iconUrl);
        dest.writeParcelable(this.teaser, flags);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeByte(this.instantCheckout ? (byte) 1 : (byte) 0);
        dest.writeString(this.slug);
        dest.writeString(this.defaultOperatorId);
        dest.writeString(this.operatorStyle);
        dest.writeList(this.clientNumberList);
        dest.writeTypedList(this.operatorList);
    }

    public CategoryData() {
    }

    protected CategoryData(Parcel in) {
        this.categoryId = in.readString();
        this.categoryType = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.iconUrl = in.readString();
        this.teaser = in.readParcelable(Teaser.class.getClassLoader());
        this.isNew = in.readByte() != 0;
        this.instantCheckout = in.readByte() != 0;
        this.slug = in.readString();
        this.defaultOperatorId = in.readString();
        this.operatorStyle = in.readString();
        this.clientNumberList = new ArrayList<ClientNumber>();
        in.readList(this.clientNumberList, ClientNumber.class.getClassLoader());
        this.operatorList = in.createTypedArrayList(Operator.CREATOR);
    }

    public static final Parcelable.Creator<CategoryData> CREATOR = new Parcelable.Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel source) {
            return new CategoryData(source);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };
}
