package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

public class EgoldAttributeModel implements ShipmentData, Parcelable {

    private boolean isEligible;
    private int minEgoldRange;
    private int maxEgoldRange;
    private String titleText;
    private String subText;
    private String tickerText;
    private String tooltipText;
    private boolean checked;
    private int buyEgoldValue;

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public int getMinEgoldRange() {
        return minEgoldRange;
    }

    public void setMinEgoldRange(int minEgoldRange) {
        this.minEgoldRange = minEgoldRange;
    }

    public int getMaxEgoldRange() {
        return maxEgoldRange;
    }

    public void setMaxEgoldRange(int maxEgoldRange) {
        this.maxEgoldRange = maxEgoldRange;
    }

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

    public int getBuyEgoldValue() {
        return buyEgoldValue;
    }

    public void setBuyEgoldValue(int buyEgoldValue) {
        this.buyEgoldValue = buyEgoldValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public EgoldAttributeModel(){}

    protected EgoldAttributeModel(Parcel in) {
        isEligible = in.readByte() != 0;
        checked = in.readByte() != 0;
        minEgoldRange = in.readInt();
        maxEgoldRange = in.readInt();
        buyEgoldValue = in.readInt();
        titleText = in.readString();
        subText = in.readString();
        tickerText = in.readString();
        tooltipText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isEligible ? 1 : 0));
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeInt(minEgoldRange);
        dest.writeInt(maxEgoldRange);
        dest.writeInt(buyEgoldValue);
        dest.writeString(titleText);
        dest.writeString(subText);
        dest.writeString(tickerText);
        dest.writeString(tooltipText);
    }

    public static final Creator<EgoldAttributeModel> CREATOR = new Creator<EgoldAttributeModel>() {
        @Override
        public EgoldAttributeModel createFromParcel(Parcel in) {
            return new EgoldAttributeModel(in);
        }

        @Override
        public EgoldAttributeModel[] newArray(int size) {
            return new EgoldAttributeModel[size];
        }
    };

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
