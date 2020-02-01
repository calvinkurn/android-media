
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ImgPortrait {

    @SerializedName("280x418")
    @Expose
    private String _280x418;

    public String get280x418() {
        return _280x418;
    }
}
