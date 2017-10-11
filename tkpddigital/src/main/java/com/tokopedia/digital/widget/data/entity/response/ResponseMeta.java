package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author rizkyfadillah on 10/11/2017.
 */

public class ResponseMeta {

    @SerializedName("default_index")
    @Expose
    private int defaultIndex;

    public int getDefaultIndex() {
        return defaultIndex;
    }

}
