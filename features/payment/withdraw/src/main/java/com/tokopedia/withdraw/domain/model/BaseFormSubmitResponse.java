package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

public class BaseFormSubmitResponse {
    @SerializedName("richieSubmitWithdrawal")
    FormSubmitResponse formSubmitResponse;
}
