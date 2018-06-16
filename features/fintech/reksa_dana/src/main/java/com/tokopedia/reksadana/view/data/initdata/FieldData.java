package com.tokopedia.reksadana.view.data.initdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FieldData {
    @Expose
    @SerializedName("itemName")
    private String itemName;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("fieldType")
    private String fieldType;
    @Expose
    @SerializedName("defaultText")
    private String defaultText;
    @Expose
    @SerializedName("dropdownValues")
    private List<DropdownValues> dropdownValues;

    public FieldData(String itemName, String id, String fieldType, String defaultText, List<DropdownValues> dropdownValues) {
        this.itemName = itemName;
        this.id = id;
        this.fieldType = fieldType;
        this.defaultText = defaultText;
        this.dropdownValues = dropdownValues;
    }

    public String itemName() {
        return itemName;
    }

    public String getId() {
        return id;
    }

    public String fieldType() {
        return fieldType;
    }

    public String defaultText() {
        return defaultText;
    }

    public List<DropdownValues> dropdownValues() {
        return dropdownValues;
    }

}
