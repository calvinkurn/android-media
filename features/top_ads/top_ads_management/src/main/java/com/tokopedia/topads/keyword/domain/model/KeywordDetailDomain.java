package com.tokopedia.topads.keyword.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class KeywordDetailDomain {

    private int keywordId;
    private String keywordTag;
    private int groupId;
    private String groupName;
    private int keywordStatus;
    private String keywordStatusDesc;
    private String keywordTypeId;
    private String keywordTypeDesc;
    private String keywordPriceBidFmt;
    private String statAvgClick;
    private String statTotalSpent;
    private String statTotalImpression;
    private String statTotalClick;
    private String statTotalCtr;
    private String statTotalConversion;
    private String labelPerClick;
    private int statusToogle;
    private int groupBid;

    public int getGroupBid() {
        return groupBid;
    }

    public void setGroupBid(int groupBid) {
        this.groupBid = groupBid;
    }

    public int getStatusToogle() {
        return statusToogle;
    }

    public void setStatusToogle(int statusToogle) {
        this.statusToogle = statusToogle;
    }

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getKeywordStatus() {
        return keywordStatus;
    }

    public void setKeywordStatus(int keywordStatus) {
        this.keywordStatus = keywordStatus;
    }

    public String getKeywordStatusDesc() {
        return keywordStatusDesc;
    }

    public void setKeywordStatusDesc(String keywordStatusDesc) {
        this.keywordStatusDesc = keywordStatusDesc;
    }

    public String getKeywordTypeId() {
        return keywordTypeId;
    }

    public void setKeywordTypeId(String keywordTypeId) {
        this.keywordTypeId = keywordTypeId;
    }

    public String getKeywordTypeDesc() {
        return keywordTypeDesc;
    }

    public void setKeywordTypeDesc(String keywordTypeDesc) {
        this.keywordTypeDesc = keywordTypeDesc;
    }

    public String getKeywordPriceBidFmt() {
        return keywordPriceBidFmt;
    }

    public void setKeywordPriceBidFmt(String keywordPriceBidFmt) {
        this.keywordPriceBidFmt = keywordPriceBidFmt;
    }

    public String getStatAvgClick() {
        return statAvgClick;
    }

    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    public String getStatTotalClick() {
        return statTotalClick;
    }

    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    public String getLabelPerClick() {
        return labelPerClick;
    }

    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }
}
