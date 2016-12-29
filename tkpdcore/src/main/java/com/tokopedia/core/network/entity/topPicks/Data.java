
package com.tokopedia.core.network.entity.topPicks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("toppicks")
    @Expose
    private List<Toppick> toppicks = null;

    public List<Toppick> getToppicks() {
        return toppicks;
    }

    public void setToppicks(List<Toppick> toppicks) {
        this.toppicks = toppicks;
    }

}
