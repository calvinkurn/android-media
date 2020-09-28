package com.tokopedia.topads.keyword.domain.model.keywordadd;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.topads.keyword.constant.KeywordTypeDef;

/**
 * @author Hendry on 5/26/2017.
 */

public class AddKeywordDomainModelDatum implements Parcelable {
    private String keywordTag;
    @KeywordTypeDef
    private int keyWordTypeId;
    private String groupId;
    private String shopId;
    private String source;
    private int priceBid;

    public AddKeywordDomainModelDatum(String keywordTag, int keyWordTypeId, String groupId, String shopId,int priceBid) {
        this.keywordTag = keywordTag;
        this.keyWordTypeId = keyWordTypeId;
        this.groupId = groupId;
        this.shopId = shopId;
        this.priceBid = priceBid;
    }

    public AddKeywordDomainModelDatum(String keywordTag, int keyWordTypeId, String groupId, String shopId, String source) {
        this.keywordTag = keywordTag;
        this.keyWordTypeId = keyWordTypeId;
        this.groupId = groupId;
        this.shopId = shopId;
        this.source = source;
    }

    protected AddKeywordDomainModelDatum(Parcel in) {
        keywordTag = in.readString();
        keyWordTypeId = in.readInt();
        groupId = in.readString();
        shopId = in.readString();
        source = in.readString();
        priceBid = in.readInt();
    }

    public static final Creator<AddKeywordDomainModelDatum> CREATOR = new Creator<AddKeywordDomainModelDatum>() {
        @Override
        public AddKeywordDomainModelDatum createFromParcel(Parcel in) {
            return new AddKeywordDomainModelDatum(in);
        }

        @Override
        public AddKeywordDomainModelDatum[] newArray(int size) {
            return new AddKeywordDomainModelDatum[size];
        }
    };


    public void setPriceBid(int priceBid) {
        this.priceBid = priceBid;
    }

    public int getPriceBid() {
        return priceBid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public int getKeyWordTypeId() {
        return keyWordTypeId;
    }

    public void setKeyWordTypeId(int keyWordTypeId) {
        this.keyWordTypeId = keyWordTypeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(keywordTag);
        parcel.writeInt(keyWordTypeId);
        parcel.writeString(groupId);
        parcel.writeString(shopId);
        parcel.writeString(source);
        parcel.writeInt(priceBid);
    }
}