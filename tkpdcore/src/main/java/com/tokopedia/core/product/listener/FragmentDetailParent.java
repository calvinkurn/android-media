package com.tokopedia.core.product.listener;

import android.os.Bundle;

/**
 * @author madi on 5/12/17.
 *         this class only for remove casting on DeepLinkActivity.class
 */

public interface FragmentDetailParent {
    void onErrorAction(Bundle bundle, int resultCode);

    void onSuccessAction(Bundle bundle, int resultCode);
}
