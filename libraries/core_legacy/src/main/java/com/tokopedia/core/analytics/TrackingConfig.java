package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.container.GTMContainer;
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
}
