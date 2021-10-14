package com.tokopedia.home.beranda.helper;

import android.text.TextUtils;

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemDataModel;

/**
 * Created by henrypriyono on 09/02/18.
 */

public class DynamicLinkHelper {

    public static String getActionLink(DynamicHomeIcon.DynamicIcon item) {
        return getAvailableLink(item.getApplinks(), item.getUrl());
    }

    public static String getActionLink(SpotlightItemDataModel item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(DynamicHomeChannel.Header item) {
        return getAvailableLink(item.getApplink(), item.getUrl());
    }

    public static String getActionLink(DynamicHomeChannel.Grid item) {
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
