package com.tokopedia.digital.widget.view.model.category;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/14/17.
 */

public class Attributes implements Parcelable {

    private String name;
    private int weight;
    private String icon;
    private boolean isNew;
    private boolean validatePrefix;
    private boolean instantCheckoutAvailable;
    private ClientNumber clientNumber;
    private String defaultOperatorId;
    private boolean usePhonebook;
    private boolean showOperator;
    private String slug;
    private int status;
    private String operatorLabel;

    public Attributes() {
    }

    protected Attributes(Parcel in) {
        name = in.readString();
        weight = in.readInt();
        icon = in.readString();
        isNew = in.readByte() != 0;
        validatePrefix = in.readByte() != 0;
        instantCheckoutAvailable = in.readByte() != 0;
        clientNumber = in.readParcelable(ClientNumber.class.getClassLoader());
        defaultOperatorId = in.readString();
        usePhonebook = in.readByte() != 0;
        showOperator = in.readByte() != 0;
        slug = in.readString();
        status = in.readInt();
        operatorLabel = in.readString();
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

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

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public void setOperatorLabel(String operatorLabel) {
        this.operatorLabel = operatorLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(weight);
        dest.writeString(icon);
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeByte((byte) (validatePrefix ? 1 : 0));
        dest.writeByte((byte) (instantCheckoutAvailable ? 1 : 0));
        dest.writeParcelable(clientNumber, flags);
        dest.writeString(defaultOperatorId);
        dest.writeByte((byte) (usePhonebook ? 1 : 0));
        dest.writeByte((byte) (showOperator ? 1 : 0));
        dest.writeString(slug);
        dest.writeInt(status);
        dest.writeString(operatorLabel);
    }
}