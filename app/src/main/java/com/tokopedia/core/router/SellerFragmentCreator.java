package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.Context;

import com.tokopedia.core.var.FragmentCreatorConstant;

/**
 * Created by ricoharisin on 11/10/16.
 */

public class SellerFragmentCreator {

    public static final String FRAGMENT_SELLING_NEW_ORDER = "com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder";

    public static Fragment getFragmentSellingNewOrder(Context context) {
        return Fragment.instantiate(context, FRAGMENT_SELLING_NEW_ORDER);
    }
}
