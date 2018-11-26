package com.tokopedia.affiliate;

import android.app.Activity;

/**
 * @author by yfsx on 12/10/18.
 */
public interface AffiliateRouter {
    void actionApplinkFromActivity(Activity activity, String linkUrl);

    void openRedirectUrl(Activity activity, String url);
}
