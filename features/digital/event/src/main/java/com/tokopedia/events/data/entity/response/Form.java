
package com.tokopedia.events.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Form implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("element_type")
    @Expose
    private String elementType;
    @SerializedName("help_text")
    @Expose
    private String helpText;
    @SerializedName("required")
    @Expose
    private int required;
    @SerializedName("validator_regex")
    @Expose
    private String validatorRegex;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("status")
    @Expose
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getValidatorRegex() {
        return validatorRegex;
    }

    public void setValidatorRegex(String validatorRegex) {
        this.validatorRegex = validatorRegex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.productId);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeString(this.elementType);
        dest.writeString(this.helpText);
        dest.writeInt(this.required);
        dest.writeString(this.validatorRegex);
        dest.writeString(this.errorMessage);
        dest.writeInt(this.status);
    }

    public Form() {
    }

    protected Form(Parcel in) {
        this.id = in.readInt();
        this.productId = in.readInt();
        this.name = in.readString();
        this.title = in.readString();
        this.value = in.readString();
        this.elementType = in.readString();
        this.helpText = in.readString();
        this.required = in.readInt();
        this.validatorRegex = in.readString();
        this.errorMessage = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<Form> CREATOR = new Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel source) {
            return new Form(source);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
}
