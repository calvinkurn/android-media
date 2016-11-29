
package com.tokopedia.sellerapp.home.model.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
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
