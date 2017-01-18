
package com.tokopedia.core.network.entity.topPicks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("toppicks")
    @Expose
    private List<Toppick> toppicks = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Toppick> getToppicks() {
        return toppicks;
    }

    public void setToppicks(List<Toppick> toppicks) {
        this.toppicks = toppicks;
    }

}
