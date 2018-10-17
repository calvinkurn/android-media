
package com.tokopedia.tokocash.balance.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbTagEntity {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("tag")
    @Expose
    private String tag;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
