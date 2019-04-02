package com.tokopedia.checkout.view.feature.removecartitem;

/**
 * @author Irfan Khoirul on 24/05/18.
 */
@Deprecated
public interface RemoveCartItemViewListener {

    void onSingleItemCheckChanged(boolean checked, int position);

    void onAllItemCheckChanged(boolean checked);
}
