package com.tokopedia.promogamification.common.floating.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GamiFloatingButtonEntity {

    @SerializedName("resultStatus")
    @Expose
    private ResultStatus resultStatus;
    @SerializedName("sumToken")
    @Expose
    private int sumToken;
    @SerializedName("sumTokenStr")
    @Expose
    private String sumTokenStr;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tokenUnit")
    @Expose
    private String tokenUnit;
    @SerializedName("cta")
    @Expose
    private FloatingCtaEntity cta;
    @SerializedName("timeRemainingSeconds")
    @Expose
    private int timeRemainingSeconds;
    @SerializedName("isShowTime")
    @Expose
    private boolean isShowTime;
    @SerializedName("unixTimestamp")
    @Expose
    private int unixTimestamp;
    @SerializedName("imgURL")
    @Expose
    private String imgURL;
    @SerializedName("isPermanent")
    @Expose
    private Boolean isPermanent;
    @SerializedName("timerBGColor")
    @Expose
    private String timerBGColor;
    @SerializedName("timerFontColor")
    @Expose
    private String timerFontColor;

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getSumToken() {
        return sumToken;
    }

    public void setSumToken(int sumToken) {
        this.sumToken = sumToken;
    }

    public String getSumTokenStr() {
        return sumTokenStr;
    }

    public void setSumTokenStr(String sumTokenStr) {
        this.sumTokenStr = sumTokenStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenUnit() {
        return tokenUnit;
    }

    public void setTokenUnit(String tokenUnit) {
        this.tokenUnit = tokenUnit;
    }

    public FloatingCtaEntity getCta() {
        return cta;
    }

    public void setCta(FloatingCtaEntity cta) {
        this.cta = cta;
    }

    public int getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(int timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public boolean isShowTime() {
        return isShowTime;
    }

    public void setIsShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    public int getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(int unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Boolean getPermanent() {
        return isPermanent;
    }

    public void setPermanent(Boolean permanent) {
        isPermanent = permanent;
    }

    public String getTimerBGColor() {
        return timerBGColor;
    }

    public void setTimerBGColor(String timerBGColor) {
        this.timerBGColor = timerBGColor;
    }

    public String getTimerFontColor() {
        return timerFontColor;
    }

    public void setTimerFontColor(String timerFontColor) {
        this.timerFontColor = timerFontColor;
    }
}
