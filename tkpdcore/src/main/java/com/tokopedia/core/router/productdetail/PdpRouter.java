package com.tokopedia.core.router.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest;
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {

    void openImagePreview(Context context, ArrayList<String> images, int position);

    Intent getProductReputationIntent(Context context, String productId, String productName);

    Intent getCartIntent(Activity activity);

    Intent getCheckoutIntent(Context context, ShipmentFormRequest shipmentFormRequest);

    Intent getExpressCheckoutIntent(Activity activity, AtcRequestParam atcRequestParam);

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    void goToCreateTopadsPromo(Context context, String productId, String shopId, String sourceCreateTopadsManageProduct);

    int getCartCount(Context context);

    Intent getProductTalk(Context context, String productId);

    Intent getCheckoutIntent(Context context, String deviceid);

    void eventClickFilterReview(Context context,
                                String filterName,
                                String productId);

    void eventImageClickOnReview(Context context,
                                 String productId,
                                 String reviewId);

    void getDynamicShareMessage(Context dataObj, ActionCreator<String, Integer> actionCreator, ActionUIDelegate<String, String> actionUIDelegate);
}