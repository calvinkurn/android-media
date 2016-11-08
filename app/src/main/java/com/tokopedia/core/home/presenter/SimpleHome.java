package com.tokopedia.core.home.presenter;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface SimpleHome {
    String TAG = "MNORMANSYAH";
    String messageTAG = "SimpleHomeView : ";

    void fetchExtras(Intent intent);

    void saveDataBeforeRotate(Bundle outstate);

    void fetchDataAfterRotate(Bundle instate);

    void initDataInstance();


}
