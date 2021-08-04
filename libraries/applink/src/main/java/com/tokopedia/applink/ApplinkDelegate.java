package com.tokopedia.applink;

import android.app.Activity;
import android.content.Intent;
import androidx.core.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLinkResult;

/**
 * @author okasurya on 8/30/18.
 */
public interface ApplinkDelegate {

    DeepLinkResult dispatchFrom(Activity activity, Intent sourceIntent);

    Intent getIntent(Activity activity, String applink) throws Exception;

    TaskStackBuilder getTaskStackBuilder(Activity activity, String applink) throws Exception;

    boolean supportsUri(String uriString);
}
