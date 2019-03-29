
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ImgPortrait {

    @SerializedName("277x415")
    @Expose
    private String _277x415;
    @SerializedName("280x418")
    @Expose
    private String _280x418;

    public String get277x415() {
        return _277x415;
    }

    public void set277x415(String _277x415) {
        this._277x415 = _277x415;
    }

    public String get280x418() {
        return _280x418;
    }

    public void set280x418(String _280x418) {
        this._280x418 = _280x418;
    }

}
