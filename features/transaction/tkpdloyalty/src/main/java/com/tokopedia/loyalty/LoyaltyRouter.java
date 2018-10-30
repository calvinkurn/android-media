package com.tokopedia.loyalty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.router.loyaltytokopoint.ILoyaltyRouter;

import rx.Observable;

/**
 * @author Aghny A. Putra on 3/4/18
 */

public interface LoyaltyRouter extends ILoyaltyRouter {

    Intent getPromoDetailIntent(Context context, String slug);

    Observable<TokoPointDrawerData> getTokopointUseCase();
  
    Intent getPromoListIntent(Activity activity);
}