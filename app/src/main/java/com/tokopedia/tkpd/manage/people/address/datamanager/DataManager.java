package com.tokopedia.tkpd.manage.people.address.datamanager;

import android.content.Context;

import java.util.Map;

/**
 * Created on 5/19/16.
 */
public interface DataManager {

    void initAddressList(Context context, Map<String, String> params);

    void disconnectNetworkConnection(Context context);

    void loadMoreAddressList(Context context, Map<String, String> params);

}
