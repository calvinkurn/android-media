package com.tokopedia.logisticaddaddress.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressViewModel implements Visitable<AddressTypeFactory> {

    @Override
    public int type(AddressTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
