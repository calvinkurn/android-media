package com.tokopedia.navigation_common.listener;

import android.os.Bundle;

/**
 * Created by Irfan Khoirul on 17/09/18.
 */

public interface EmptyCartListener {

    String ARG_CART_LIST_DATA = "ARG_CART_LIST_DATA";

    void onCartEmpty(String autoApplyMessage, String state, String titleDesc);

    void onCartNotEmpty(Bundle bundle);

}
