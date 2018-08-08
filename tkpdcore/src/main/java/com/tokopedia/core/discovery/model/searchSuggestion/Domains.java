
/*
 * Created By Kulomady on 11/25/16 11:59 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:59 PM
 */

package com.tokopedia.core.discovery.model.searchSuggestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
