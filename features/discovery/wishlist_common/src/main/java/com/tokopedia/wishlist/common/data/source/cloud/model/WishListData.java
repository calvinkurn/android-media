
package com.tokopedia.wishlist.common.data.source.cloud.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListData {

    @SerializedName("ids")
    @Expose
    private List<String> ids = null;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
