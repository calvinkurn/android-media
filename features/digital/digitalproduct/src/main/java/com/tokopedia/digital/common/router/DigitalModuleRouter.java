package com.tokopedia.digital.common.router;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;

/**
 * @author by alvarisi on 2/20/18.
 */

public interface DigitalModuleRouter {

    Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle);

    String getBranchAutoApply(Activity activity);

    String getTrackingClientId();

    CacheManager getGlobalCacheManager();
}
