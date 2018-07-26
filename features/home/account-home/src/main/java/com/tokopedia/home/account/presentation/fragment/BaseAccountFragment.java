package com.tokopedia.home.account.presentation.fragment;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.RouteManager;

/**
 * @author okasurya on 7/26/18.
 */
public abstract class BaseAccountFragment extends TkpdBaseV4Fragment {
    protected void openApplink(String applink) {
        if(getContext() != null && !TextUtils.isEmpty(applink)) {
            RouteManager.route(getContext(), applink);
        }
    }
}
