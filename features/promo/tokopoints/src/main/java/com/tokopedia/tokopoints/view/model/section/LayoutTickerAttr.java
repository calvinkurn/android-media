
package com.tokopedia.tokopoints.view.model.section;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tokopoints.view.model.TickerContainer;

public class LayoutTickerAttr {

    @SerializedName("tickerList")
    @Expose
    private List<TickerContainer> tickerList;

    public List<TickerContainer> getTickerList() {
        return tickerList;
    }

    public void setTickerList(List<TickerContainer> tickerList) {
        this.tickerList = tickerList;
    }

}
