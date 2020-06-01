
package com.tokopedia.product.addedit.imagepicker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Specification {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("row")
    @Expose
    private List<Row> row = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }

}
