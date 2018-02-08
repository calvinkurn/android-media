package com.tokopedia.core.analytics.fingerprint.data;

import android.content.Context;

import com.tokopedia.core.analytics.fingerprint.data.source.FingerprintDiskDataStore;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class FingerprintDataStoreFactory {

    private Context context;

    public FingerprintDataStoreFactory(Context ctx){
        context = ctx;
    }

    public FingerprintDataStore createDiskFingerprintDataStore(){
        return new FingerprintDiskDataStore();
    }

}
