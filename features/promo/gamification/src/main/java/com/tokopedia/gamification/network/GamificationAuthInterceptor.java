package com.tokopedia.gamification.network;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GamificationAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public GamificationAuthInterceptor(@ApplicationContext Context context,
                                       AbstractionRouter abstractionRouter,
                                       UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }
}
