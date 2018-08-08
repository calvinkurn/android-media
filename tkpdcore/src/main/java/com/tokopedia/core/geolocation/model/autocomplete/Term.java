
package com.tokopedia.core.geolocation.model.autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("offset")
    @Expose
    private Integer offset;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

}
