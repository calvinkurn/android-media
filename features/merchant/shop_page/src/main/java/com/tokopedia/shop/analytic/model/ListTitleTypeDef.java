package com.tokopedia.shop.analytic.model;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.analytic.model.ListTitleTypeDef.ETALASE;
import static com.tokopedia.shop.analytic.model.ListTitleTypeDef.HIGHLIGHTED;
import static com.tokopedia.shop.analytic.model.ListTitleTypeDef.SEARCH_RESULT;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({HIGHLIGHTED, ETALASE, SEARCH_RESULT})
public @interface ListTitleTypeDef {
    String HIGHLIGHTED = "highlighted";
    String ETALASE = "etalase";
    String SEARCH_RESULT = " search result";
}
