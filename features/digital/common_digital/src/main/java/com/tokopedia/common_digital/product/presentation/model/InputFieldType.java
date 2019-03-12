package com.tokopedia.common_digital.product.presentation.model;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        InputFieldType.TYPE_TEXT,
        InputFieldType.TYPE_NUMERIC,
        InputFieldType.TYPE_TEL,
        InputFieldType.TYPE_SELECT,
        InputFieldType.TYPE_RADIO,
        InputFieldType.TYPE_CHECKBOX,
        InputFieldType.TYPE_SELECT_CARD,
        InputFieldType.TYPE_SELECT_LIST,
        InputFieldType.TYPE_NUMERIC_STICKY,

})
public @interface InputFieldType {
    String NAME_OPERATOR_ID = "operator_id";
    String NAME_PRODUCT_ID = "product_id";

    String TYPE_TEXT = "text";
    String TYPE_NUMERIC = "numeric";
    String TYPE_TEL = "tel";
    String TYPE_SELECT = "select";
    String TYPE_RADIO = "radio";
    String TYPE_CHECKBOX = "checkbox";
    String TYPE_SELECT_CARD = "select_card";
    String TYPE_SELECT_LIST = "select_list";
    String TYPE_NUMERIC_STICKY = "numeric_sticky";
}
