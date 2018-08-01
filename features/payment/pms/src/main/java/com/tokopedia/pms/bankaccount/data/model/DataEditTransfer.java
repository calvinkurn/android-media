package com.tokopedia.pms.bankaccount.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class DataEditTransfer {

    @SerializedName("editTransfer")
    @Expose
    private EditTransfer editTransfer;

    public EditTransfer getEditTransfer() {
        return editTransfer;
    }

    public void setEditTransfer(EditTransfer editTransfer) {
        this.editTransfer = editTransfer;
    }
}
