package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormSubmitResponse {

    @SerializedName("message")
    List<String> message;
    @SerializedName("message_error")
    String messageError;
    @SerializedName("status")
    String status;
}
