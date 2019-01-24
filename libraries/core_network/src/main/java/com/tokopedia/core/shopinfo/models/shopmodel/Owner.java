
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("owner_image")
    @Expose
    public String ownerImage;
    @SerializedName("owner_phone")
    @Expose
    public int ownerPhone;
    @SerializedName("owner_id")
    @Expose
    public int ownerId;
    @SerializedName("owner_email")
    @Expose
    public String ownerEmail;
    @SerializedName("owner_name")
    @Expose
    public String ownerName;
    @SerializedName("owner_messenger")
    @Expose
    public String ownerMessenger;

}
