
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoMoengage {

    @SerializedName("info")
    @Expose
    private Info info = new Info();
    @SerializedName("owner")
    @Expose
    private Owner owner = new Owner();
    @SerializedName("stats")
    @Expose
    private Stats stats = new Stats();

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

}
