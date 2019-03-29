package com.tokopedia.core.gcm.data;

import android.content.Context;

import com.tokopedia.core.gcm.data.source.CloudPushNotificationDataSource;
import com.tokopedia.core.gcm.data.source.DiskPushNotificationDataStore;

/**
 * Created by alvarisi on 2/21/17.
 */

public class PushNotificationDataStoreFactory {
    private Context mContext;
    public PushNotificationDataStoreFactory(Context context){
        mContext = context;
    }
    public PushNotificationDataStore createCloudPushNotificationDataStore(){
        return new CloudPushNotificationDataSource(mContext);
    }

    public PushNotificationDataStore createDiskPushNotificationDataStore(){
        return new DiskPushNotificationDataStore(mContext);
    }
}
