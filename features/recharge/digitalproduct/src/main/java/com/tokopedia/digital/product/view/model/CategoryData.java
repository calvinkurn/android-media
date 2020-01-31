package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.Operator;

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
    public static final String SLUG_PRODUCT_CATEGORY_EMONEY = "emoney";

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
    private boolean isNew;
    private boolean instantCheckout;
    private String slug;
    private String defaultOperatorId;
    private String operatorStyle;
    private String operatorLabel;
    private AdditionalFeature additionalFeature;
    private List<ClientNumber> clientNumberList = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();
    private List<BannerData> bannerDataListIncluded = new ArrayList<>();
    private List<BannerData> otherBannerDataListIncluded = new ArrayList<>();
    private List<GuideData> guideDataList = new ArrayList<>();

    private CategoryData(Builder builder) {
        setCategoryId(builder.categoryId);
        setTitleText(builder.titleText);
        setName(builder.name);
        setIcon(builder.icon);
        setIconUrl(builder.iconUrl);
        setNew(builder.isNew);
        setInstantCheckout(builder.instantCheckout);
        setSlug(builder.slug);
        setDefaultOperatorId(builder.defaultOperatorId);
        setOperatorStyle(builder.operatorStyle);
        setOperatorLabel(builder.operatorLabel);
        setAdditionalFeature(builder.additionalFeature);
        setClientNumberList(builder.clientNumberList);
        setOperatorList(builder.operatorList);
        setBannerDataListIncluded(builder.bannerDataListIncluded);
        setOtherBannerDataListIncluded(builder.otherBannerDataListIncluded);
        setGuideDataList(builder.guideDataList);
    }

    protected CategoryData(Parcel in) {
        categoryId = in.readString();
        categoryType = in.readString();
        titleText = in.readString();
        name = in.readString();
        icon = in.readString();
        iconUrl = in.readString();
        isNew = in.readByte() != 0;
        instantCheckout = in.readByte() != 0;
        slug = in.readString();
        defaultOperatorId = in.readString();
        operatorStyle = in.readString();
        operatorLabel = in.readString();
        additionalFeature = in.readParcelable(AdditionalFeature.class.getClassLoader());
        clientNumberList = in.createTypedArrayList(ClientNumber.CREATOR);
        operatorList = in.createTypedArrayList(Operator.CREATOR);
        bannerDataListIncluded = in.createTypedArrayList(BannerData.CREATOR);
        otherBannerDataListIncluded = in.createTypedArrayList(BannerData.CREATOR);
        guideDataList = in.createTypedArrayList(GuideData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(categoryType);
        dest.writeString(titleText);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(iconUrl);
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeByte((byte) (instantCheckout ? 1 : 0));
        dest.writeString(slug);
        dest.writeString(defaultOperatorId);
        dest.writeString(operatorStyle);
        dest.writeString(operatorLabel);
        dest.writeParcelable(additionalFeature, flags);
        dest.writeTypedList(clientNumberList);
        dest.writeTypedList(operatorList);
        dest.writeTypedList(bannerDataListIncluded);
        dest.writeTypedList(otherBannerDataListIncluded);
        dest.writeTypedList(guideDataList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel in) {
            return new CategoryData(in);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };

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

    public AdditionalFeature getAdditionalFeature() {
        return additionalFeature;
    }

    public void setAdditionalFeature(AdditionalFeature additionalFeature) {
        this.additionalFeature = additionalFeature;
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

    public List<GuideData> getGuideDataList() {
        return guideDataList;
    }

    public void setGuideDataList(List<GuideData> guideDataList) {
        this.guideDataList = guideDataList;
    }

    public CategoryData() {
    }

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
        private AdditionalFeature additionalFeature;
        private List<ClientNumber> clientNumberList;
        private List<Operator> operatorList;
        private List<BannerData> bannerDataListIncluded;
        private List<BannerData> otherBannerDataListIncluded;
        private List<GuideData> guideDataList;

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

        public Builder additionalFeature(AdditionalFeature val) {
            additionalFeature = val;
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

        public Builder guideDataList(List<GuideData> val) {
            guideDataList = val;
            return this;
        }

        public CategoryData build() {
            return new CategoryData(this);
        }
    }

}
