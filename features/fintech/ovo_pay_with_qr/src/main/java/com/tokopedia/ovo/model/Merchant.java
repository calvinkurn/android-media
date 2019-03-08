package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Merchant {

@SerializedName("name")
@Expose
private String name;
@SerializedName("description")
@Expose
private String description;

    public Merchant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

}