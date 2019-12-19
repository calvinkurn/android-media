package com.tokopedia.topads.keyword.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef.KEYWORD_STATUS_ACTIVE;
import static com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef.KEYWORD_STATUS_ALL;
import static com.tokopedia.topads.keyword.constant.KeywordStatusTypeDef.KEYWORD_STATUS_INACTIVE;

/**
 * @author normansyahputa on 5/29/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({KEYWORD_STATUS_ALL, KEYWORD_STATUS_ACTIVE, KEYWORD_STATUS_INACTIVE})
public @interface KeywordStatusTypeDef {
    int KEYWORD_STATUS_ALL = 0;
    int KEYWORD_STATUS_ACTIVE = 1;
    int KEYWORD_STATUS_INACTIVE = 3;
}
