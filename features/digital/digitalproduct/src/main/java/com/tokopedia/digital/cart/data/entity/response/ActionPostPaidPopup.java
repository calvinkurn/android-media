package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionPostPaidPopup {
        @SerializedName("YES")
        @Expose
        private ActionConfirmPostPaidPopup confirmAction;

    public ActionConfirmPostPaidPopup getConfirmAction() {
        return confirmAction;
    }
}
