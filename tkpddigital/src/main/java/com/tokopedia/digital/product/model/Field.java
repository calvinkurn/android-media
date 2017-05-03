package com.tokopedia.digital.product.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Field {
    private String name;
    private String type;
    private String text;
    private String help;
    private String placeholder;
    private String _default;
    private List<Validation> validation = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

    public List<Validation> getValidation() {
        return validation;
    }

    public void setValidation(List<Validation> validation) {
        this.validation = validation;
    }
}
