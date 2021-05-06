package com.tokopedia.promocheckout.list.view.adapter;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder;
import com.tokopedia.promocheckout.R;

/**
 * @author kulomady on 1/24/17.
 */

public class PromoCheckoutListEmptyViewHolder extends BaseEmptyViewHolder<EmptyModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_list_promo_checkout_empty;

    public PromoCheckoutListEmptyViewHolder(View itemView) {
        super(itemView);
    }

    public PromoCheckoutListEmptyViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }

    @Override
    public void bind(EmptyModel element) {
        super.bind(element);
    }
}
