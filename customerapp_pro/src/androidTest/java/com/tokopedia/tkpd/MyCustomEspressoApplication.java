package com.tokopedia.tkpd;

import android.content.Context;
import androidx.multidex.MultiDex;

/**
 * Created by normansyahputa on 3/22/18.
 */

public class MyCustomEspressoApplication extends ConsumerMainApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
