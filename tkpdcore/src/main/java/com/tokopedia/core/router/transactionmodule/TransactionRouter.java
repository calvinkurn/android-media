package com.tokopedia.core.router.transactionmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartResult;

import rx.Observable;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    void goToUserPaymentList(Activity activity);

    void goToOrderHistory(Context context, String orderId, int userMode);

    Intent goToOrderDetail(Context context, String orderId);

    Intent getInboxReputationIntent(Context context);

    Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName);

    Intent getResolutionCenterIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest);

    void updateMarketplaceCartCounter(CartNotificationListener listener);

    interface CartNotificationListener {
        void onDataReady();
    }

}
