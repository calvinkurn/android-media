package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Field {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("help")
    @Expose
    private String help;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("validation")
    @Expose
    private List<Validation> validation = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getHelp() {
        return help;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String get_default() {
        return _default;
    }

    public List<Validation> getValidation() {
        return validation;
    }
}
