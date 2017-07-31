package com.tokopedia.posapp.view.viewmodel.outlet;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletViewModel {
    private String outletId;
    private String outletName;
    private String outletAddres;
    private String outletPhone;

    public OutletViewModel(String id, String name, String address, String phone) {
        this.outletId = id;
        this.outletName = name;
        this.outletAddres = address;
        this.outletPhone = phone;
    }

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
