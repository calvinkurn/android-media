package com.tokopedia.mitratoppers.dashboard;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.mitratoppers.common.constant.MitraToppersBaseURL;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersDashboardActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
         return BaseSessionWebViewFragment.newInstance(getMitraToppersUrl());
    }

    private String getMitraToppersUrl() {
        return MitraToppersBaseURL.WEB_DOMAIN + MitraToppersBaseURL.PATH_MITRA_TOPPERS;
    }
}
