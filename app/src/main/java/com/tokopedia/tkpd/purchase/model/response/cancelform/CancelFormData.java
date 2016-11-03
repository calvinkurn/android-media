package com.tokopedia.tkpd.purchase.model.response.cancelform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 24/05/2016.
 */
public class CancelFormData {
    private static final String TAG = CancelFormData.class.getSimpleName();

    @SerializedName("form")
    @Expose
    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
