
package com.tokopedia.tkpd.discovery.model.history.searchSuggestion;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Domains {

    @SerializedName("general")
    @Expose
    private List<General> general = new ArrayList<General>();
    @SerializedName("hotlist")
    @Expose
    private List<Hotlist> hotlist = new ArrayList<Hotlist>();

    /**
     * 
     * @return
     *     The general
     */
    public List<General> getGeneral() {
        return general;
    }

    /**
     * 
     * @param general
     *     The general
     */
    public void setGeneral(List<General> general) {
        this.general = general;
    }

    /**
     * 
     * @return
     *     The hotlist
     */
    public List<Hotlist> getHotlist() {
        return hotlist;
    }

    /**
     * 
     * @param hotlist
     *     The hotlist
     */
    public void setHotlist(List<Hotlist> hotlist) {
        this.hotlist = hotlist;
    }

}
