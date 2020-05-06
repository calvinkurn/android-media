
package com.tokopedia.topads.common.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
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
