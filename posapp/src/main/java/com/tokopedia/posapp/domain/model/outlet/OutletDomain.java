package com.tokopedia.posapp.domain.model.outlet;

import java.util.List;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletDomain {
    private List<DataOutletDomain> listOutlet;
    private String uriNext;
    private String uriPrevious;

    public List<DataOutletDomain> getListOutlet() {
        return listOutlet;
    }

    public void setListOutlet(List<DataOutletDomain> listOutlet) {
        this.listOutlet = listOutlet;
    }

    public String getUriNext() {
        return uriNext;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    public String getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }
}
