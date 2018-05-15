package com.tokopedia.logisticuploadawb;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public class UploadAwbLogisticActivity extends BaseSimpleActivity {

    private static final String EXTRA_URL_UPLOAD = "EXTRA_URL_UPLOAD";

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, UploadAwbLogisticActivity.class);
        intent.putExtra(EXTRA_URL_UPLOAD, url);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance("");
    }
}
