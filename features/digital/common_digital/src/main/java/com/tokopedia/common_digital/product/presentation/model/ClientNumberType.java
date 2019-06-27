package com.tokopedia.common_digital.product.presentation.model;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        ClientNumberType.TYPE_INPUT_TEL,
        ClientNumberType.TYPE_INPUT_NUMERIC,
        ClientNumberType.TYPE_INPUT_ALPHANUMERIC,
        ClientNumberType.DEFAULT_TYPE_CONTRACT
})
public @interface ClientNumberType {

    String TYPE_INPUT_TEL = "tel";
    String TYPE_INPUT_NUMERIC = "numeric";
    String TYPE_INPUT_ALPHANUMERIC = "tel";
    String DEFAULT_TYPE_CONTRACT = "client_number";

}
