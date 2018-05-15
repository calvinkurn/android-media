package com.tokopedia.logisticuploadawb;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * @author anggaprasetiyo on 15/05/18.
 */
public class UploadAwbLogisticActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance("");
    }
}
