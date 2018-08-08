
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("is_default")
    @Expose
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
