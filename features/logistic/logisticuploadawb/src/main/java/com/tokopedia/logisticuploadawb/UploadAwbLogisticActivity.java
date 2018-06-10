package com.tokopedia.logisticuploadawb;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public class UploadAwbLogisticActivity extends BaseSimpleActivity {

    private static final String EXTRA_URL_UPLOAD = "EXTRA_URL_UPLOAD";

    private static final String FLAVOR_STAGING = "staging";
    private static final String HOST_LITE_LIVE_DOMAIN = "m.tokopedia.com";
    private static final String HOST_LITE_STAGING_DOMAIN = "m-staging.tokopedia.com";

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, UploadAwbLogisticActivity.class);
        intent.putExtra(EXTRA_URL_UPLOAD, url);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String urlUpload = getIntent().getStringExtra(EXTRA_URL_UPLOAD);
        if (getApplication() instanceof ILogisticUploadAwbRouter) {
            String buildFlavor = ((ILogisticUploadAwbRouter) getApplication())
                    .logisticUploadRouterGetApplicationBuildFlavor();
            if (buildFlavor.equalsIgnoreCase(FLAVOR_STAGING)) {
                //noinspection ResultOfMethodCallIgnored
                urlUpload.replace(HOST_LITE_LIVE_DOMAIN, HOST_LITE_STAGING_DOMAIN);
            }
        }
        return UploadAwbLogisticFragment.newInstance(urlUpload);
    }
}
