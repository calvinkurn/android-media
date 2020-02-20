
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Img {

    @SerializedName("375x200")
    @Expose
    private String _375x200;

    public String get375x200() {
        return _375x200;
    }

}
