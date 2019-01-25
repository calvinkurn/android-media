
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ImgSquare {

    @SerializedName("200x200")
    @Expose
    private String _200x200;

    public String get200x200() {
        return _200x200;
    }

    public void set200x200(String _200x200) {
        this._200x200 = _200x200;
    }

}
