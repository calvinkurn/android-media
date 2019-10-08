package com.tokopedia.affiliate.feature.dashboard.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory;

/**
 * @author by yfsx on 18/09/18.
 */
public class DashboardItemViewModel implements Visitable<DashboardItemTypeFactory>,Parcelable {

    private String id;
    private String imageUrl;
    private String title;
    private String value;
    private String itemClicked;
    private String itemSold;
    private String productCommission;
    private String earnedComission;
    private boolean isActive;
    private String sectionName;
    private boolean shouldShowSection;
    private Integer type;
    private String createPostApplink;
    private int reviewCount;
    private int productRating;
    private boolean shouldShowButtonCreatePost;

    public DashboardItemViewModel(String id, String imageUrl, String title, String value,
                                  String itemClicked, String itemSold,
                                  String productCommission, String earnedComission, boolean isActive, String sectionName, boolean shouldShowSection,
                                  Integer type, String createPostApplink,
                                  int reviewCount, int productRating, boolean shouldShowButtonCreatePost) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.value = value;
        this.itemClicked = itemClicked;
        this.itemSold = itemSold;
        this.productCommission = productCommission;
        this.earnedComission = earnedComission;
        this.isActive = isActive;
        this.sectionName = sectionName;
        this.shouldShowSection = shouldShowSection;
        this.type = type;
        this.createPostApplink = createPostApplink;
        this.reviewCount = reviewCount;
        this.productRating = productRating;
        this.shouldShowButtonCreatePost = shouldShowButtonCreatePost;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getItemClicked() {
        return itemClicked;
    }

    public void setItemClicked(String itemClicked) {
        this.itemClicked = itemClicked;
    }

    public String getItemSold() {
        return itemSold;
    }

    public void setItemSold(String itemSold) {
        this.itemSold = itemSold;
    }

    public String getProductCommission() {
        return productCommission;
    }

    public void setProductCommission(String productCommission) {
        this.productCommission = productCommission;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public boolean isShouldShowSection() {
        return shouldShowSection;
    }

    public void setShouldShowSection(boolean shouldShowSection) {
        this.shouldShowSection = shouldShowSection;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreatePostApplink() {
        return createPostApplink;
    }

    public void setCreatePostApplink(String createPostApplink) {
        this.createPostApplink = createPostApplink;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public String getEarnedComission() {
        return earnedComission;
    }

    public void setEarnedComission(String earnedComission) {
        this.earnedComission = earnedComission;
    }

    public boolean isShouldShowButtonCreatePost() {
        return shouldShowButtonCreatePost;
    }

    public void setShouldShowButtonCreatePost(boolean shouldShowButtonCreatePost) {
        this.shouldShowButtonCreatePost = shouldShowButtonCreatePost;
    }

    @Override
    public int type(DashboardItemTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeString(this.itemClicked);
        dest.writeString(this.itemSold);
        dest.writeString(this.productCommission);
        dest.writeString(this.earnedComission);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.sectionName);
        dest.writeByte(this.shouldShowSection ? (byte) 1 : (byte) 0);
        dest.writeValue(this.type);
        dest.writeString(this.createPostApplink);
        dest.writeInt(this.reviewCount);
        dest.writeInt(this.productRating);
        dest.writeByte(this.shouldShowButtonCreatePost ? (byte) 1 : (byte) 0);
    }

    protected DashboardItemViewModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.value = in.readString();
        this.itemClicked = in.readString();
        this.itemSold = in.readString();
        this.productCommission = in.readString();
        this.earnedComission = in.readString();
        this.isActive = in.readByte() != 0;
        this.sectionName = in.readString();
        this.shouldShowSection = in.readByte() != 0;
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createPostApplink = in.readString();
        this.reviewCount = in.readInt();
        this.productRating = in.readInt();
        this.shouldShowButtonCreatePost = in.readByte() != 0;
    }

    public static final Creator<DashboardItemViewModel> CREATOR = new Creator<DashboardItemViewModel>() {
        @Override
        public DashboardItemViewModel createFromParcel(Parcel source) {
            return new DashboardItemViewModel(source);
        }

        @Override
        public DashboardItemViewModel[] newArray(int size) {
            return new DashboardItemViewModel[size];
        }
    };
}
