package com.tokopedia.topads.keyword.domain.model;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class Datum {

    private int keywordId;// 1
    private String keywordTag;// 2
    private int groupId;// 3
    private String groupName;// 4
    private int keywordStatus;// 5
    private String keywordStatusDesc;// 6
    private String keywordTypeId;//7
    private String keywordTypeDesc;// 8
    private int keywordStatusToogle;// 9
    private String keywordPriceBidFmt;// 10
    private String statAvgClick; // 11
    private String statTotalSpent; // 12
    private String statTotalImpression; // 13
    private String statTotalClick; // 14
    private String statTotalCtr; // 15
    private String statTotalConversion; // 16
    private String labelEdit; // 17
    private String labelPerClick; // 18
    private String labelOf; // 19
    private int groupBid;

    public int getGroupBid() {
        return groupBid;
    }

    public void setGroupBid(int groupBid) {
        this.groupBid = groupBid;
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

    public int getKeywordStatusToogle() {
        return keywordStatusToogle;
    }

    public void setKeywordStatusToogle(int keywordStatusToogle) {
        this.keywordStatusToogle = keywordStatusToogle;
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

    public String getLabelEdit() {
        return labelEdit;
    }

    public void setLabelEdit(String labelEdit) {
        this.labelEdit = labelEdit;
    }

    public String getLabelPerClick() {
        return labelPerClick;
    }

    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    public String getLabelOf() {
        return labelOf;
    }

    public void setLabelOf(String labelOf) {
        this.labelOf = labelOf;
    }
}
