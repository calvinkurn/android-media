package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseProductInputField {

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
    private List<ResponseValidation> validation;

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

    public String getDefault() {
        return _default;
    }

    public List<ResponseValidation> getValidation() {
        return validation;
    }
}
