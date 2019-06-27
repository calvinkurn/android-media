package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.SerializedName;

public class EgoldMessage {

    @SerializedName("title_text")
    private String titleText;

    @SerializedName("sub_text")
    private String subText;

    @SerializedName("ticker_text")
    private String tickerText;

    @SerializedName("tooltip_text")
    private String tooltipText;

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }
}
