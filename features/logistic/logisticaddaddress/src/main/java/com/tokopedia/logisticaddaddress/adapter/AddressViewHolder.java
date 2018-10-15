package com.tokopedia.logisticaddaddress.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.logisticaddaddress.R;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressViewHolder extends AbstractViewHolder<AddressViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.logistic_item_manage_people_address;

    public AddressViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(AddressViewModel element) {

    }
}
