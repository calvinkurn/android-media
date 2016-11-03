package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo
 * on 30/06/2016.
 */
public class FormEditPaymentData {
    @SerializedName("form")
    @Expose
    private FormEdit form;

    public FormEdit getForm() {
        return form;
    }

    public void setForm(FormEdit form) {
        this.form = form;
    }
}
