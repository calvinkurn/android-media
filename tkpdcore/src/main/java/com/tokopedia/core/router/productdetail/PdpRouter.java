package com.tokopedia.core.router.productdetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

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

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);
}