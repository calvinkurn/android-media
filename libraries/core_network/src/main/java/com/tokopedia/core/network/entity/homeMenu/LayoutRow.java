
package com.tokopedia.core.network.entity.homeMenu;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class LayoutRow {

    @SerializedName("additional_info")
    private String mAdditionalInfo;
    @SerializedName("category_id")
    private Long mCategoryId;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image_url")
    private String mImageUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("type")
    private String mType;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("Weight")
    private Long mWeight;
    @SerializedName("applinks")
    private String appLinks;

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    public void setAdditionalInfo(String additional_info) {
        mAdditionalInfo = additional_info;
    }

    public Long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(Long category_id) {
        mCategoryId = category_id;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String image_url) {
        mImageUrl = image_url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Long getWeight() {
        return mWeight;
    }

    public void setWeight(Long weight) {
        mWeight = weight;
    }

    public String getAppLinks() {
        return appLinks;
    }
}