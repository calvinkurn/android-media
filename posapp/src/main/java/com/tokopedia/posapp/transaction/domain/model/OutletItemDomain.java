package com.tokopedia.posapp.transaction.domain.model;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletItemDomain {
    private String outletId;
    private String outletName;
    private String outletAddres;
    private String outletPhone;

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletAddres() {
        return outletAddres;
    }

    public void setOutletAddres(String outletAddres) {
        this.outletAddres = outletAddres;
    }

    public String getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(String outletPhone) {
        this.outletPhone = outletPhone;
    }
}
