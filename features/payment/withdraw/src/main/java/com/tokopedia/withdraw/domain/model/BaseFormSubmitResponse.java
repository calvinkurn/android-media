package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

public class BaseFormSubmitResponse {
    @SerializedName("richieSubmitWithdrawal")
    FormSubmitResponse formSubmitResponse;

    public FormSubmitResponse getFormSubmitResponse() {
        return formSubmitResponse;
    }

    public void setFormSubmitResponse(FormSubmitResponse formSubmitResponse) {
        this.formSubmitResponse = formSubmitResponse;
    }
}
