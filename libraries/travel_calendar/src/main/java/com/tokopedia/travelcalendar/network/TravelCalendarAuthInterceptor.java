package com.tokopedia.travelcalendar.network;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public TravelCalendarAuthInterceptor(@ApplicationContext Context context,
                                         AbstractionRouter abstractionRouter,
                                         UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }
}
