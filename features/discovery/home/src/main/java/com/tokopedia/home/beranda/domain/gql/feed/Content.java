
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("inspirasi")
    @Expose
    private List<Inspirasi> inspirasi = null;
    @SerializedName("topads")
    @Expose
    private List<Topad> topads;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Inspirasi> getInspirasi() {
        return inspirasi;
    }

    public void setInspirasi(List<Inspirasi> inspirasi) {
        this.inspirasi = inspirasi;
    }

    public List<Topad> getTopads() {
        return topads;
    }

    public void setTopads(List<Topad> topads) {
        this.topads = topads;
    }
}
