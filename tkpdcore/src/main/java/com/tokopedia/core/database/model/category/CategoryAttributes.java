
package com.tokopedia.core.database.model.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryAttributes implements Parcelable {

    @SerializedName("client_number")
    @Expose
    private ClientNumber clientNumber;
    @SerializedName("default_operator_id")
    @Expose
    private String defaultOperatorId;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("instant_checkout_available")
    @Expose
    private boolean instantCheckoutAvailable;
    @SerializedName("is_new")
    @Expose
    private boolean isNew;
    @SerializedName("validate_prefix")
    @Expose
    private boolean validatePrefix;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("use_phonebook")
    @Expose
    private boolean usePhonebook;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("show_operator")
    @Expose
    private boolean showOperator;

    public ClientNumber getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(ClientNumber clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isInstantCheckoutAvailable() {
        return instantCheckoutAvailable;
    }

    public void setInstantCheckoutAvailable(boolean instantCheckoutAvailable) {
        this.instantCheckoutAvailable = instantCheckoutAvailable;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isValidatePrefix() {
        return validatePrefix;
    }

    public void setValidatePrefix(boolean validatePrefix) {
        this.validatePrefix = validatePrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isUsePhonebook() {
        return usePhonebook;
    }

    public void setUsePhonebook(boolean usePhonebook) {
        this.usePhonebook = usePhonebook;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isShowOperator() {
        return showOperator;
    }

    public void setShowOperator(boolean showOperator) {
        this.showOperator = showOperator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.clientNumber, flags);
        dest.writeString(this.defaultOperatorId);
        dest.writeString(this.icon);
        dest.writeByte(this.instantCheckoutAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeByte(this.validatePrefix ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.slug);
        dest.writeInt(this.status);
        dest.writeByte(this.usePhonebook ? (byte) 1 : (byte) 0);
        dest.writeInt(this.weight);
        dest.writeByte(this.showOperator ? (byte) 1 : (byte) 0);
    }

    public CategoryAttributes() {
    }

    protected CategoryAttributes(Parcel in) {
        this.clientNumber = in.readParcelable(ClientNumber.class.getClassLoader());
        this.defaultOperatorId = in.readString();
        this.icon = in.readString();
        this.instantCheckoutAvailable = in.readByte() != 0;
        this.isNew = in.readByte() != 0;
        this.validatePrefix = in.readByte() != 0;
        this.name = in.readString();
        this.slug = in.readString();
        this.status = in.readInt();
        this.usePhonebook = in.readByte() != 0;
        this.weight = in.readInt();
        this.showOperator = in.readByte() != 0;
    }

    public static final Creator<CategoryAttributes> CREATOR = new Creator<CategoryAttributes>() {
        @Override
        public CategoryAttributes createFromParcel(Parcel source) {
            return new CategoryAttributes(source);
        }

        @Override
        public CategoryAttributes[] newArray(int size) {
            return new CategoryAttributes[size];
        }
    };
}