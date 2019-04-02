package com.tokopedia.loyalty;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.loyalty.common.PopUpNotif;
import com.tokopedia.loyalty.common.TokoPointDrawerData;

import rx.Observable;

/**
 * @author Aghny A. Putra on 3/4/18
 */

public interface LoyaltyRouter {

    Intent getPromoDetailIntent(Context context, String slug);

    Observable<TokoPointDrawerData> getTokopointUseCase();
  
    Intent getPromoListIntent(Activity activity);

    DialogFragment getLoyaltyTokoPointNotificationDialogFragment(PopUpNotif popUpNotif);

    BroadcastReceiver getTokoPointBroadcastReceiver();

    void openTokoPoint(Context context, String url);
}