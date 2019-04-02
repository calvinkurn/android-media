package com.tokopedia.nps;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Created by meta on 08/11/18.
 */
public interface NpsRouter {

    void showAdvancedAppRatingDialog(Activity activity,
                                     DialogInterface.OnDismissListener dismissListener);

    void showSimpleAppRatingDialog(Activity activity);
}
