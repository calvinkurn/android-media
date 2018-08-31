package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
class ResponseOperatorInputField {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("placeholder")
    @Expose
    private String placeholder;

//    private String default;

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

    public String getPlaceholder() {
        return placeholder;
    }

    public List<ResponseValidation> getValidation() {
        return validation;
    }
}
