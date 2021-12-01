package com.tokopedia.promocheckout.list.view.adapter;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.promocheckout.R;

/**
 * @author kulomady on 1/24/17.
 */

public class PromoCheckoutListEmptyViewHolder extends EmptyViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_list_promo_checkout_empty;

    public PromoCheckoutListEmptyViewHolder(View itemView) {
        super(itemView);
    }

    public PromoCheckoutListEmptyViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }
}
