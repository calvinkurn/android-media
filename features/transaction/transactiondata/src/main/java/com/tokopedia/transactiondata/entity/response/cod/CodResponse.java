package com.tokopedia.transactiondata.entity.response.cod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class CodResponse {

    @SerializedName("validate_checkout_cod")
    @Expose
    private ValidateCheckoutCod validateCheckoutCod;

    public CodResponse() {
    }

    public CodResponse(ValidateCheckoutCod validateCheckoutCod) {
        this.validateCheckoutCod = validateCheckoutCod;
    }

    public ValidateCheckoutCod getValidateCheckoutCod() {
        return validateCheckoutCod;
    }

    public void setValidateCheckoutCod(ValidateCheckoutCod validateCheckoutCod) {
        this.validateCheckoutCod = validateCheckoutCod;
    }
}
