package com.tokopedia.common_digital.product.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class InputFieldModel {

    private String name;
    private String type;
    private String text;
    private String placeholder;
    private String _default;
    private List<Validation> validation;

    public InputFieldModel(String name, String type, String text, String placeholder, String _default, List<Validation> validation) {
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

}
