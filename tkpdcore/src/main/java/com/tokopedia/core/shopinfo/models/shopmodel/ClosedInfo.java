
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClosedInfo {

    @SerializedName("until")
    @Expose
    public String until;
    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("note")
    @Expose
    public String note;

}
