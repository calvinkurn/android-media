
package com.tokopedia.topads.dashboard.data.model.ticker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("topAdsTicker")
    @Expose
    private TopAdsTicker topAdsTicker;

    public TopAdsTicker getTopAdsTicker() {
        return topAdsTicker;
    }

    public void setTopAdsTicker(TopAdsTicker topAdsTicker) {
        this.topAdsTicker = topAdsTicker;
    }

}
