package com.tokopedia.core.router.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.ActionInterfaces.ActionCreator;
import com.tokopedia.abstraction.ActionInterfaces.ActionUIDelegate;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.linker.model.LinkerData;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {

    void gotToProductDetail(Context context);

    void goToProductDetail(Context context, String productUrl);

    void goToProductDetail(Context context, ProductPass productPass);

    void goToProductDetail(Context context, LinkerData shareData);

    void goToAddProductDetail(Context context);

    Fragment getProductDetailInstanceDeeplink(Context context, @NonNull ProductPass productPass);

    void goToProductDetailForResult(Fragment fragment,
                                    String productId,
                                    int adapterPosition,
                                    int requestCode);

    void openImagePreview(Context context, ArrayList<String> images, ArrayList<String>
            imageDesc, int position);

    void openImagePreview(Context context, ArrayList<String> images, int position);

    void openImagePreviewFromChat(Context context, ArrayList<String> images,
                                  ArrayList<String> imageDesc, String title, String date);

    Intent getProductReputationIntent(Context context, String productId, String productName);

    Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment);

    Intent getCartIntent(Activity activity);

    Intent getCheckoutIntent(Activity activity);

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    void goToCreateTopadsPromo(Context context, String productId, String shopId, String sourceCreateTopadsManageProduct);

    int getCartCount(Context context);

    Intent getProductTalk(Context context, String productId);

    void eventClickFilterReview(Context context,
                                String filterName,
                                String productId);

    void eventImageClickOnReview(Context context,
                                 String productId,
                                 String reviewId);

    void getDynamicShareMessage(Context dataObj, ActionCreator<String, Integer> actionCreator, ActionUIDelegate<String, String> actionUIDelegate);
}