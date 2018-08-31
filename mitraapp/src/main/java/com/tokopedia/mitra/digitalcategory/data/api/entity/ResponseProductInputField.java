package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 31/08/18.
 */
class ResponseProductInputField {

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

//    private String default;

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

}
