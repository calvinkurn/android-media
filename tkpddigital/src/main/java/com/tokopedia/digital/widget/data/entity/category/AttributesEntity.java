package com.tokopedia.digital.widget.data.entity.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/14/17.
 */

public class AttributesEntity {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("is_new")
    @Expose
    private boolean isNew;
    @SerializedName("validate_prefix")
    @Expose
    private boolean validatePrefix;
    @SerializedName("instant_checkout_available")
    @Expose
    private boolean instantCheckoutAvailable;
    @SerializedName("client_number")
    @Expose
    private ClientNumberEntity clientNumber;
    @SerializedName("default_operator_id")
    @Expose
    private String defaultOperatorId;
    @SerializedName("use_phonebook")
    @Expose
    private boolean usePhonebook;
    @SerializedName("show_operator")
    @Expose
    private boolean showOperator;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("operator_label")
    @Expose
    private String operatorLabel;

    public ClientNumberEntity getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(ClientNumberEntity clientNumber) {
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
}