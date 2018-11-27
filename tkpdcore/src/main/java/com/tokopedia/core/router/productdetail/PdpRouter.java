package com.tokopedia.core.router.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartResult;

import rx.Observable;

import java.util.ArrayList;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {

    void gotToProductDetail(Context context);

    void goToProductDetail(Context context, String productUrl);

    void goToProductDetail(Context context, ProductPass productPass);

    void goToProductDetail(Context context, ShareData shareData);

    void goToAddProductDetail(Context context);

    Fragment getProductDetailInstanceDeeplink(Context context, @NonNull ProductPass productPass);

    void goToProductDetailForResult(Fragment fragment,
                                    String productId,
                                    int adapterPosition,
                                    int requestCode);

    void openImagePreview(Context context, ArrayList<String> images, ArrayList<String>
            imageDesc, int position);

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
}