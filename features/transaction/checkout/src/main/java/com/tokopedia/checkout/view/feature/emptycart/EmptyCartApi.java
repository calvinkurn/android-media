package com.tokopedia.checkout.view.feature.emptycart;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Irfan Khoirul on 02/11/18.
 */

@IntDef({EmptyCartApi.CART_LIST, EmptyCartApi.WISH_LIST, EmptyCartApi.LAST_SEEN, EmptyCartApi.SUGGESTION})
@Retention(RetentionPolicy.SOURCE)
public @interface EmptyCartApi {
    int CART_LIST = 0;
    int WISH_LIST = 1;
    int LAST_SEEN = 2;
    int SUGGESTION = 3;
}
