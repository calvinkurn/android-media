package com.tokopedia.home.beranda.helper;

import android.text.TextUtils;

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightItemViewModel;

/**
 * Created by henrypriyono on 09/02/18.
 */

public class DynamicLinkHelper {

    public static String getActionLink(HomeIconItem item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(SpotlightItemViewModel item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(DynamicHomeChannel.Header item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(DynamicHomeChannel.Grid item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(DynamicHomeChannel.Hero item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    private static String getAvailableLink(String applink, String url) {
        if (!TextUtils.isEmpty(applink)) {
            return applink;
        } else {
            return url;
        }
    }
}
