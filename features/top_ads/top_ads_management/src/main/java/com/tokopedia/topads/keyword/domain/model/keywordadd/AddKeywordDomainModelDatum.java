package com.tokopedia.topads.keyword.domain.model.keywordadd;

import com.tokopedia.topads.keyword.constant.KeywordTypeDef;

/**
 * @author Hendry on 5/26/2017.
 */

public class AddKeywordDomainModelDatum {
    private String keywordTag;
    @KeywordTypeDef
    private int keyWordTypeId;
    private String groupId;
    private String shopId;
    private String source;

    public AddKeywordDomainModelDatum(String keywordTag, int keyWordTypeId, String groupId, String shopId) {
        this.keywordTag = keywordTag;
        this.keyWordTypeId = keyWordTypeId;
        this.groupId = groupId;
        this.shopId = shopId;
    }

    public AddKeywordDomainModelDatum(String keywordTag, int keyWordTypeId, String groupId, String shopId, String source) {
        this.keywordTag = keywordTag;
        this.keyWordTypeId = keyWordTypeId;
        this.groupId = groupId;
        this.shopId = shopId;
        this.source = source;
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
}