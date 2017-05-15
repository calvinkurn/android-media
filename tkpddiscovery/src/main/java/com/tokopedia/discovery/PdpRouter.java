package com.tokopedia.discovery;

import android.content.Context;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {
    void goToProductDetail(Context context, String productUrl);

    void goToProductDetail(Context context, ProductPass productPass);
}
