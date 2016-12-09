package com.tokopedia.core.myproduct.customview.wholesale;

/**
 * Created by sebastianuskh on 12/5/16.
 */

public interface WholesaleViewHolder {

    void onQtyOneError(String error);
    void onQtyTwoError(String error);
    void onQtyPriceError(String error);

    CharSequence getQtyOneError();

    CharSequence getQtyTwoError();

    CharSequence getQtyPriceError();
}
