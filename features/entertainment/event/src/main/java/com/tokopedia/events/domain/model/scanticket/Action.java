package com.tokopedia.events.domain.model.scanticket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action implements Parcelable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ui_control")
    @Expose
    private String uiControl;
    @SerializedName("button_type")
    @Expose
    private String buttonType;
    @SerializedName("url_params")
    @Expose
    private UrlParams urlParams;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("text_color")
    @Expose
    private String textColor;

    protected Action(Parcel in) {
        label = in.readString();
        name = in.readString();
        uiControl = in.readString();
        buttonType = in.readString();
        weight = in.readInt();
        color = in.readString();
        textColor = in.readString();
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUiControl() {
        return uiControl;
    }

    public void setUiControl(String uiControl) {
        this.uiControl = uiControl;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public UrlParams getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(UrlParams urlParams) {
        this.urlParams = urlParams;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(name);
        parcel.writeString(uiControl);
        parcel.writeString(buttonType);
        parcel.writeInt(weight);
        parcel.writeString(color);
        parcel.writeString(textColor);
    }
}