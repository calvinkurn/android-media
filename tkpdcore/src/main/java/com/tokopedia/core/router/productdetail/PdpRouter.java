package com.tokopedia.core.router.productdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

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

    void goToPreviewProduct(Context context, Bundle bundle);
}
