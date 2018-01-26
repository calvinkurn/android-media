package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeGraphql {
    @Expose
    HomeData data;

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }
}
