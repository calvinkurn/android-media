package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.analytics.container.IAppsflyerContainer;
import com.tokopedia.core.analytics.container.IGTMContainer;

/**
 * @author by alvarisi on 10/26/16.
 */
@Deprecated
public abstract class TrackingConfig {

    /**
     * Get GTM Container Instance
     *
     * @return GTM Container
     */
    static IGTMContainer getGTMEngine(Context context) {
        return GTMContainer.newInstance(context);
    }

    /**
     * Get Appsflyer Container Instance
     *
     * @return Appsflyer Instance
     */
    static IAppsflyerContainer getAFEngine(Context context) {
        return Jordan.init(context).getAFContainer();
    }
}
