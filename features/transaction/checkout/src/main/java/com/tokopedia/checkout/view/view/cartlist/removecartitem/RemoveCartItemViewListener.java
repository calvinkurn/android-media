package com.tokopedia.checkout.view.view.cartlist.removecartitem;

/**
 * @author Irfan Khoirul on 24/05/18.
 */

public interface RemoveCartItemViewListener {

    void onSingleItemCheckChanged(int position);

    void onAllItemCheckChanged(boolean checked);
}
