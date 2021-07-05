package com.tokopedia.buyerorder.detail.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActionButton implements Serializable {

    @SerializedName("body")
    @Expose
    private Body Body;

    @SerializedName("value")
    @Expose
    private String Value;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("uri")
    @Expose
    private String uri ="";

    @SerializedName("control")
    @Expose
    private String Control;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("buttonType")
    @Expose
    private String buttonType;

    @SerializedName("header")
    @Expose
    private String Header;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("color")
    @Expose
    private ActionColor actionColor;

    @SerializedName("popup")
    @Expose
    private ActionButtonPopUp actionButtonPopUp = new ActionButtonPopUp();

    public Body getBody() {
        return Body;
    }

    public void setBody(Body Body) {
        this.Body = Body;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Header getHeaderObject() {
        Gson gson = new Gson();
        return gson.fromJson(this.getHeader(), Header.class);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getControl() {
        return Control;
    }

    public void setControl(String Control) {
        this.Control = Control;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String Header) {
        this.Header = Header;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ActionColor getActionColor() {
        return actionColor;
    }

    public void setActionColor(ActionColor actionColor) {
        this.actionColor = actionColor;
    }

    public ActionButtonPopUp getActionButtonPopUp() {
        return actionButtonPopUp;
    }

    public void setActionButtonPopUp(ActionButtonPopUp actionButtonPopUp) {
        this.actionButtonPopUp = actionButtonPopUp;
    }

    @Override
    public String toString() {
        return "ActionButton [Body = " + Body + ", Value = " + Value + ", weight = " + weight + ", label = " + label + ", uri = " + uri + ", Control = " + Control + ", Name = " + Name + ", buttonType = " + buttonType + ", Header = " + Header + ", method = " + method + ", key = " + key + "]";
    }

    public static final String PRIMARY_BUTTON = "primaryButton";
    public static final String SECONDARY_BUTTON = "secondaryButton";
}