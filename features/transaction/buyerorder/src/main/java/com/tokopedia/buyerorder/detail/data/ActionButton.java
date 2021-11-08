package com.tokopedia.buyerorder.detail.data;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActionButton implements Serializable {

    @SerializedName("body")
    @Expose
    private Body Body;

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

    @SerializedName("color")
    @Expose
    private ActionColor actionColor;

    public Body getBody() {
        return Body;
    }

    public void setBody(Body Body) {
        this.Body = Body;
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

    public String getHeader() {
        return Header;
    }

    public ActionColor getActionColor() {
        return actionColor;
    }

    @Override
    public String toString() {
        return "ActionButton [Body = " + Body + ", label = " + label + ", uri = " + uri + ", Control = " + Control + ", Name = " + Name + ", buttonType = " + buttonType + ", Header = " + Header + "]";
    }

    public static final String PRIMARY_BUTTON = "primaryButton";
    public static final String SECONDARY_BUTTON = "secondaryButton";
}