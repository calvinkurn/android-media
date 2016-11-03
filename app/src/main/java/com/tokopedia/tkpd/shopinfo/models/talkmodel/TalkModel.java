
package com.tokopedia.tkpd.shopinfo.models.talkmodel;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TalkModel {
    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.tkpd.shopinfo.models.talkmodel.List> list = new ArrayList<com.tokopedia.tkpd.shopinfo.models.talkmodel.List>();

}
