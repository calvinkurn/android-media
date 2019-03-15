package com.tokopedia.navigation.presentation.adapter;

import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.RecomTitle;
import com.tokopedia.navigation.domain.model.Recomendation;

/**
 * Author errysuprayogi on 13,March,2019
 */
public interface InboxTypeFactory {

    int type(Inbox inbox);

    int type(Recomendation recomendation);

    int type(RecomTitle recomTitle);

}
