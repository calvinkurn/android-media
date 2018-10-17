package com.tokopedia.logisticaddaddress.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressTypeFactory extends BaseAdapterTypeFactory {

    private AddressViewHolder.ManageAddressListener addressListener;

    public AddressTypeFactory(AddressViewHolder.ManageAddressListener addressListener) {
        this.addressListener = addressListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == AddressViewHolder.LAYOUT) {
            return new AddressViewHolder(parent, addressListener);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(AddressViewModel addressModel) {
        return AddressViewHolder.LAYOUT;
    }

}
