package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 24/10/18.
 */
public class AutoCompleteQuery {

    @SerializedName("topadsExploreAutocom")
    @Expose
    private AutoCompleteData data;

    public AutoCompleteData getData() {
        return data;
    }

    public void setData(AutoCompleteData data) {
        this.data = data;
    }
}
