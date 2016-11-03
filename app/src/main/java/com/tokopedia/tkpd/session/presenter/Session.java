package com.tokopedia.tkpd.session.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public interface Session {
    String TAG = "MNORMANSYAH";
    String messageTAG = "Session : ";

    String WHICH_FRAGMENT_KEY = "which_fragment_key";
    int WHICH_FRAGMENT_KEY_INVALID = -1;

    @Deprecated
    int setWhichFragmentKeyInvalid(Intent intent);

    void finishTo();

    void fetchExtras(Intent intent);

    void saveDataBeforeRotate(Bundle outstate);

    void fetchDataAfterRotate(Bundle instate);

    void initDataInstance();

    boolean isAfterRotate();

    int getWhichFragment();

    void setWhichFragment(int whichFragmentKey);
}
