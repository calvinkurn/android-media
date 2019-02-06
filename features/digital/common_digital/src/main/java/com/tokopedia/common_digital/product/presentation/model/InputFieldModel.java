package com.tokopedia.common_digital.product.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class InputFieldModel {

    public static final String NAME_OPERATOR_ID = "operator_id";
    public static final String NAME_PRODUCT_ID = "product_id";

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_NUMERIC = "numeric";
    public static final String TYPE_TEL = "tel";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_RADIO = "radio";
    public static final String TYPE_CHECKBOX = "checkbox";
    public static final String TYPE_SELECT_CARD = "select_card";
    public static final String TYPE_SELECT_LIST = "select_list";
    public static final String TYPE_NUMERIC_STICKY = "numeric_sticky";

    private String name;
    private String type;
    private String text;
    private String placeholder;
    private String _default;
    private List<Validation> validation;
    private AdditionalButton additionalButton;

    public InputFieldModel(String name, String type, String text, String placeholder, String _default,
                           List<Validation> validation) {
        this.name = name;
        this.type = type;
        this.text = text;
        this.placeholder = placeholder;
        this._default = _default;
        this.validation = validation;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getDefault() {
        return _default;
    }

    public List<Validation> getValidation() {
        return validation;
    }

    public AdditionalButton getAdditionalButton() {
        return additionalButton;
    }

    public void setAdditionalButton(AdditionalButton additionalButton) {
        this.additionalButton = additionalButton;
    }

}
