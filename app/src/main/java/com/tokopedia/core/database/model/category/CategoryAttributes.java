
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
    private Boolean instantCheckoutAvailable;
    @SerializedName("is_new")
    @Expose
    private Boolean isNew;
    @SerializedName("validate_prefix")
    @Expose
    private Boolean validatePrefix;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("use_phonebook")
    @Expose
    private Boolean usePhonebook;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("show_operator")
    @Expose
    private Boolean showOperator;

    protected CategoryAttributes(Parcel in) {
        clientNumber = (ClientNumber) in.readValue(ClientNumber.class.getClassLoader());
        defaultOperatorId = in.readString();
        icon = in.readString();
        byte instantCheckoutAvailableVal = in.readByte();
        instantCheckoutAvailable = instantCheckoutAvailableVal == 0x02 ? null : instantCheckoutAvailableVal != 0x00;
        byte isNewVal = in.readByte();
        isNew = isNewVal == 0x02 ? null : isNewVal != 0x00;
        byte validatePrefixVal = in.readByte();
        validatePrefix = validatePrefixVal == 0x02 ? null : validatePrefixVal != 0x00;
        name = in.readString();
        slug = in.readString();
        status = in.readByte() == 0x00 ? null : in.readInt();
        byte usePhonebookVal = in.readByte();
        usePhonebook = usePhonebookVal == 0x02 ? null : usePhonebookVal != 0x00;
        weight = in.readByte() == 0x00 ? null : in.readInt();
        byte showOperatorVal = in.readByte();
        showOperator = showOperatorVal == 0x02 ? null : showOperatorVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clientNumber);
        dest.writeString(defaultOperatorId);
        dest.writeString(icon);
        if (instantCheckoutAvailable == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (instantCheckoutAvailable ? 0x01 : 0x00));
        }
        if (isNew == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isNew ? 0x01 : 0x00));
        }
        if (validatePrefix == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (validatePrefix ? 0x01 : 0x00));
        }
        dest.writeString(name);
        dest.writeString(slug);
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        if (usePhonebook == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (usePhonebook ? 0x01 : 0x00));
        }
        if (weight == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(weight);
        }
        if (showOperator == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (showOperator ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CategoryAttributes> CREATOR = new Parcelable.Creator<CategoryAttributes>() {
        @Override
        public CategoryAttributes createFromParcel(Parcel in) {
            return new CategoryAttributes(in);
        }

        @Override
        public CategoryAttributes[] newArray(int size) {
            return new CategoryAttributes[size];
        }
    };
    /**
     *
     * @return
     *     The clientNumber
     */
    public ClientNumber getClientNumber() {
        return clientNumber;
    }

    /**
     *
     * @param clientNumber
     *     The client_number
     */
    public void setClientNumber(ClientNumber clientNumber) {
        this.clientNumber = clientNumber;
    }

    /**
     *
     * @return
     *     The defaultOperatorId
     */
    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    /**
     *
     * @param defaultOperatorId
     *     The default_operator_id
     */
    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    /**
     *
     * @return
     *     The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     *     The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     *     The instantCheckoutAvailable
     */
    public Boolean getInstantCheckoutAvailable() {
        return instantCheckoutAvailable;
    }

    /**
     *
     * @param instantCheckoutAvailable
     *     The instant_checkout_available
     */
    public void setInstantCheckoutAvailable(Boolean instantCheckoutAvailable) {
        this.instantCheckoutAvailable = instantCheckoutAvailable;
    }

    /**
     *
     * @return
     *     The isNew
     */
    public Boolean getIsNew() {
        return isNew;
    }

    /**
     *
     * @param isNew
     *     The is_new
     */
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     *
     * @param slug
     *     The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     *
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The teaser
     */
//    public Object getTeaser() {
//        return teaser;
//    }

    /**
     *
     * @param teaser
     *     The teaser
     */
//    public void setTeaser(String teaser) {
//        this.teaser = teaser;
//    }

    /**
     *
     * @return
     *     The usePhonebook
     */
    public Boolean getUsePhonebook() {
        return usePhonebook;
    }

    /**
     *
     * @param usePhonebook
     *     The use_phonebook
     */
    public void setUsePhonebook(Boolean usePhonebook) {
        this.usePhonebook = usePhonebook;
    }

    /**
     *
     * @return
     *     The weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     *     The weight
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getValidatePrefix() {
        return this.validatePrefix;
    }

    public void setValidatePrefix(Boolean validatePrefix) {
        this.validatePrefix = validatePrefix;
    }

    public Boolean getShowOperator() {
        return showOperator;
    }

    public void setShowOperator(Boolean showOperator) {
        this.showOperator = showOperator;
    }
}