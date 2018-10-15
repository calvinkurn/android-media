package com.tokopedia.logisticaddaddress.adapter;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressTypeFactory extends BaseAdapterTypeFactory {

    public int type(AddressViewModel addressViewModel) {
        return AddressViewHolder.LAYOUT;
    }
}
