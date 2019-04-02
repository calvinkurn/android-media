package com.tokopedia.topads.dashboard.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef.NO_SUGGESTION;
import static com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_IMPLEMENTED;
import static com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef.SUGGESTION_NOT_IMPLEMENTED;

/**
 * @author normansyahputa on 5/29/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({NO_SUGGESTION, SUGGESTION_NOT_IMPLEMENTED, SUGGESTION_IMPLEMENTED})
public @interface TopAdsSuggestionBidInteractionTypeDef {
    String NO_SUGGESTION = "-1";
    String SUGGESTION_NOT_IMPLEMENTED = "0";
    String SUGGESTION_IMPLEMENTED = "1";
}
