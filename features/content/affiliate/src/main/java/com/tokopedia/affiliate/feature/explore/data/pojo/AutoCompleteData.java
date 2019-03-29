package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompleteData {
    @SerializedName("match")
    @Expose
    private List<AutoCompletePojo> match;

    public void setMatch(List<AutoCompletePojo> match) {
        this.match = match;
    }

    public List<AutoCompletePojo> getMatch() {

        return match;
    }
}
