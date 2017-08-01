package com.tokopedia.posapp.view.viewmodel.outlet;

import java.util.List;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletViewModel {
    private List<OutletItemViewModel> outletList;
    private String nextUri;
    private String prevUri;

    public List<OutletItemViewModel> getOutletList() {
        return outletList;
    }

    public void setOutletList(List<OutletItemViewModel> outletList) {
        this.outletList = outletList;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    public String getPrevUri() {
        return prevUri;
    }

    public void setPrevUri(String prevUri) {
        this.prevUri = prevUri;
    }
}
