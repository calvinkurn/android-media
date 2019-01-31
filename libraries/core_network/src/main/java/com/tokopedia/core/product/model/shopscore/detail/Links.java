
package com.tokopedia.core.product.model.shopscore.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Links {

    @SerializedName("self")
    @Expose
    private String self;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

}
