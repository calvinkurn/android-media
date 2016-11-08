
package com.tokopedia.core.shopinfo.models.talkmodel;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TalkModel {
    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.core.shopinfo.models.talkmodel.List> list = new ArrayList<com.tokopedia.core.shopinfo.models.talkmodel.List>();

}
