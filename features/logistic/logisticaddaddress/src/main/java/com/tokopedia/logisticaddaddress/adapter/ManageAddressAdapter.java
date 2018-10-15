package com.tokopedia.logisticaddaddress.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class ManageAddressAdapter extends BaseAdapter<AddressTypeFactory> {

    public ManageAddressAdapter(AddressTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

}
